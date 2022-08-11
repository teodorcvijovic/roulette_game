package paket;

public class Izvlacenje implements Runnable {

    private Igra igra;
    private int ukupnoRundi;
    private int trenutnaRunda;

    Thread thread = null;

    /////////////////

    public Izvlacenje(Igra igra, int ukupnoRundi) {
        this.igra = igra;
        this.ukupnoRundi = ukupnoRundi;
    }

    public void pokreniIzvlacenje(){
        if (thread != null) return;
        this.trenutnaRunda = 1;
        thread = new Thread(this);
        thread.start();
    }

    public void zaustaviIzvlacenje(){
        if (thread != null) thread.interrupt();

        try{
            while (thread!=null) wait();
        } catch (InterruptedException e) {}
    }

    public int dohvUkupnoRundi() { return ukupnoRundi; }

    public synchronized int dohvTrenutnuRundu() {
        return trenutnaRunda;
    }

    public synchronized void uvecajRundu() {
        trenutnaRunda++;
    }

    @Override
    public void run() {
        igra.dohvMrezu().zakljucaj();

        //igra.istorija.resetuj();

        try {

            while (dohvTrenutnuRundu() <= ukupnoRundi) {

                igra.rulet.pokreniRulet();
                igra.rulet.sacekaj();

                int broj = igra.rulet.dohvBroj();

                igra.izvucenBroj(broj);

                uvecajRundu();
            }

        } catch (InterruptedException e) {}

        igra.rulet.zaustaviRulet();
        trenutnaRunda = 0;

        igra.dohvMrezu().otkljucaj();
        igra.dozvoliPolja();

        synchronized (this){
            thread = null;
            notify();
        }
    }

    public void postaviUkupnoRundi(int r) {
        if (thread != null) return;
        ukupnoRundi = r;
    }
}
