package paket;

import java.awt.*;

public class Istorija extends Panel {

    private TextArea[] stavke;
    private int kap;
    private int i;

    ///////

    public Istorija(int kap) {
        setPreferredSize(new Dimension(200, 0));
        this.kap = kap;
        i = 0;

        stavke = new TextArea[kap];

        setLayout(new GridLayout(kap, 0));
    }

    public void resetuj(){
        removeAll();
        i = 0;
        stavke = new TextArea[kap];
    }

    public void dodajStavku(int runda, int broj, int ulog, double dobitak, Color boja){
        StringBuilder sb = new StringBuilder();

        sb.append("Runda: ").append(runda).append('\n');
        sb.append("Broj: ").append(broj).append('\n');
        sb.append("U: ").append(ulog).append('\n');
        sb.append("D: ").append(dobitak).append('\n');

        TextArea ta = new TextArea(sb.toString(), 0, 0, TextArea.SCROLLBARS_NONE);

        ta.setBackground(boja);

        /////////////////

        stavke[i] = ta;
        i = (i + 1) % kap;

        removeAll();
        for(int j = 0; j < kap; j++) if (stavke[j] != null) add(stavke[j]);

        revalidate();
    }

}
