package paket;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class Mreza extends Panel {

    private Igra mojaIgra;
    private Polje matrica[][];
    private ArrayList<Polje> izabranaPolja = new ArrayList<>();
    int vrste, kolone;

    private boolean zakljucano = false;

    /////////////

    public synchronized boolean jeZakljucana() { return zakljucano; }
    public synchronized void zakljucaj() { zakljucano = true; }
    public synchronized void otkljucaj() { zakljucano = false; }

    Mreza(Igra mojaIgra){
        this(4, 5, mojaIgra);
    }

    Mreza(int vrste, int kolone, Igra mojaIgra) {

        setLayout(new GridLayout(vrste, kolone, 3, 3));
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(kolone * Polje.DIM_POLJA, vrste * Polje.DIM_POLJA));

        //////////

        this.mojaIgra = mojaIgra;
        this.vrste = vrste;
        this.kolone = kolone;
        this.matrica = new Polje[vrste][kolone];

        int broj = 0;
        for (int i = 0; i < vrste; i++)
            for (int j = 0; j < kolone; j++){
                matrica[i][j] = new Polje(this, broj++);
                add(matrica[i][j]);
            }

    }

    public synchronized ArrayList<Polje> dohvIzabranaPolja(){ return izabranaPolja; }

    public synchronized HashSet<Integer> dohvIzabraneBrojeve(){
        HashSet<Integer> skup = new HashSet<>();
        for(Polje p : izabranaPolja) skup.add(p.dohvBroj());
        return skup;
    }

    public Polje dohvPolje(int vrednost){
        for(int i = 0; i<vrste; i++)
            for(int j = 0; j<kolone; j++)
                if(matrica[i][j].dohvBroj()==vrednost) return matrica[i][j];
        return null;
    }

    public synchronized int brojIzabranihPolja(){ return dohvIzabraneBrojeve().size(); }

    public synchronized void promenaStatusa(Polje polje, Polje.Status status) {
        if(status == Polje.Status.IZABRANO) izabranaPolja.add(polje);
        else izabranaPolja.remove(polje);
        mojaIgra.promenaStatusa();
    }

    public void izaberiPolja(ArrayList<Integer> brojevi) {
        if (jeZakljucana()) return;
        izabranaPolja = new ArrayList<>();
        for(int i = 0; i<vrste; i++)
            for(int j = 0; j<kolone; j++)
                if (brojevi.contains(matrica[i][j].dohvBroj())) {
                    matrica[i][j].postaviStatus(Polje.Status.IZABRANO);
                    izabranaPolja.add(matrica[i][j]);
                }
                else
                    matrica[i][j].postaviStatus(Polje.Status.SLOBODNO);
    }
}
