package paket;

import java.util.ArrayList;

public class Rulet implements Runnable {

    private Mreza mreza;
    private Thread thread = null;
    private int broj; // poslednji reanimirani broj

    ///////////////////

    public Rulet(Mreza mreza) {
        this.mreza = mreza;
    }

    public synchronized int dohvBroj() { return broj; }

    public void pokreniRulet(){
        zaustaviRulet();

        thread = new Thread(this);
        thread.start();
    }

    public synchronized void zaustaviRulet(){
        if (thread != null) thread.interrupt();

        try{
            while (thread != null) wait();
        } catch (InterruptedException e) {}
    }

    @Override
    public void run() {

        int donja = (int) (mreza.kolone * mreza.vrste * 0.3);
        int gornja = (int) (mreza.kolone * mreza.vrste * 0.7);

        int velicina = new Generator(donja, gornja).rand();
        int cekanje = 200;

        ArrayList<Integer> brojevi = new Generator(0, mreza.kolone * mreza.vrste - 1).randNiz(velicina);

        try{
            for(int i = 0; i<velicina; i++){

                synchronized (this) {
                    broj = brojevi.get(i);
                }

                Polje p = mreza.dohvPolje(broj);
                Polje.Status s = p.dohvStatus();

                p.postaviStatus(Polje.Status.OZNACENO);

                Thread.sleep(cekanje);
                cekanje += (int) (cekanje * 0.1);

                p.postaviStatus(s);
            }

        }catch (InterruptedException e){}

        synchronized (this){
            thread = null;
            notify();
        }

    }

    public void sacekaj() {
        if (thread!=null)
            try { thread.join(); } catch (InterruptedException e) { }
    }
}
