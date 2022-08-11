package paket;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Polje extends Canvas {

    enum Status { SLOBODNO, IZABRANO, OZNACENO, POGODJENO, PROMASENO }

    private Mreza mojaMreza;
    private int broj;
    private Status status;

    public static int DIM_POLJA = 75;

    ///////////

    public Polje(Mreza mreza, int br) {
        mojaMreza =  mreza;
        broj = br;
        status = Status.SLOBODNO;
        setPreferredSize(new Dimension(DIM_POLJA, DIM_POLJA));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (mreza.jeZakljucana()) return;
                if (status == Status.SLOBODNO) postaviStatus(Status.IZABRANO);
                else if (status == Status.IZABRANO) postaviStatus(Status.SLOBODNO);
                revalidate();
                repaint();
            }
        });
    }

    public Integer dohvBroj() {
        return broj;
    }

    public synchronized Status dohvStatus() {
        return status;
    }

    public synchronized void postaviStatus(Status s){
        status = s;
        if (!mojaMreza.jeZakljucana()) mojaMreza.promenaStatusa(this, status);
        revalidate();
        repaint();
    }

    ///////////

    @Override
    public void paint(Graphics g) {
            setBackground(Color.ORANGE);

            int w = getWidth();
            int h = getHeight();
            DIM_POLJA = (w < h ? w : h);

            g.setFont(new Font("Sans Serif", Font.BOLD, DIM_POLJA / 3));

            switch (dohvStatus()) {
                case IZABRANO:
                    g.setColor(Color.BLUE);
                    g.fillOval(0, 0, w, h);
                    g.setColor(Color.WHITE);
                    break;
                case SLOBODNO:
                    g.setColor(Color.BLACK);
                    break;
                case OZNACENO:
                    g.setColor(Color.WHITE);
                    g.fillOval(0, 0, w, h);
                    g.setColor(Color.BLUE);
                    break;
                case POGODJENO:
                    g.setColor(Color.GREEN);
                    g.fillOval(0, 0, w, h);
                    g.setColor(Color.BLACK);
                    break;
                case PROMASENO:
                    g.setColor(Color.RED);
                    g.fillOval(0, 0, w, h);
                    g.setColor(Color.BLACK);
                    break;
            }


            /////////// Draw Centered String ////////////
            String tekst = new Integer(broj).toString();
            FontMetrics metrika = g.getFontMetrics();

            int x = (w - metrika.stringWidth(tekst)) / 2;
            int y = (h - metrika.getHeight()) / 2 + metrika.getAscent();

            g.drawString(tekst, x, y);
    }
}


