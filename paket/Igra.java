package paket;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static java.awt.event.KeyEvent.VK_K;

public class Igra extends Frame {

    public static final String format(String tekst, Double broj){
        return String.format("%s %.2f     ",tekst , broj);
    }

    ////////// Panel za upravljanje //////////

    private double balans = 0;
    private Label labelaBalans = new Label(format("Balans:", balans));

    private Label labelaUlog = new Label("Ulog:");
    private int ulog = 100;
    private TextField poljeUlog = new TextField("100");

    private Label labelaRunde = new Label("Broj rundi:");
    private int runde = 4;
    private TextField poljeRunde = new TextField("4");

    private double kvota = 0;
    private Label labelaKvota = new Label(format("Kvota:", kvota));

    private double dobitak = 0;
    private Label labelaDobitak = new Label(format("Dobitak:", dobitak));

    private Button dugmeIgraj = new Button("Igraj");

    //////////////// Mreza ///////////////////

    private Mreza mreza = new Mreza(this);
    private Generator generator = new Generator(0, mreza.vrste*mreza.kolone-1);
    Rulet rulet = new Rulet(mreza);
    private Izvlacenje izvlacenje = new Izvlacenje(this, 4);

    Istorija istorija = new Istorija(3);

    //////////// Statusna traka //////////////

    Label generisanBroj = new Label("                            ");

    {
        generisanBroj.setAlignment(Label.CENTER);
    }

    Panel statusnaTraka = new Panel(new FlowLayout(FlowLayout.LEFT));

    //////////////////////////////////////////

    Igra(){
        setBounds(700, 200, 700, 500);
        setResizable(true);
        setTitle("Igra");

        populateWindow();

        pack();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // sve niti treba da budu zaustavljene
                dispose();
            }
        });

        setVisible(true);
    }

    private void populateWindow() {
        Panel centralniPanel = new Panel(new BorderLayout());

        centralniPanel.add(istorija, BorderLayout.WEST);

        centralniPanel.add(mreza, BorderLayout.CENTER);
        dodajPanelZaUpravljanje(centralniPanel);

        add(centralniPanel, BorderLayout.CENTER);

        statusnaTraka.add(generisanBroj);
        statusnaTraka.setBackground(Color.GRAY);
        statusnaTraka.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(statusnaTraka, BorderLayout.SOUTH);

        addMenuBar();
    }

    private void addMenuBar() {
        MenuBar mb = new MenuBar();

        Menu m = new Menu("Izbor");

        MenuItem mi = new MenuItem("Par");
        mi.addActionListener(ae->{
            ArrayList<Integer> polja = new ArrayList<>();
            for(int i = 0; i < mreza.vrste * mreza.kolone; i++)
                if (i % 2 == 0) polja.add(i);
            mreza.izaberiPolja(polja);
        });
        m.add(mi);

        mi = new MenuItem("NePar");
        mi.addActionListener(ae->{
            ArrayList<Integer> polja = new ArrayList<>();
            for(int i = 0; i < mreza.vrste * mreza.kolone; i++)
                if (i % 2 != 0) polja.add(i);
            mreza.izaberiPolja(polja);
        });
        m.add(mi);

        m.addSeparator();

        mi = new MenuItem("Brisi");
        mi.addActionListener(ae->{
            ArrayList<Integer> polja = new ArrayList<>();
            mreza.izaberiPolja(polja);
        });
        m.add(mi);

        m.addSeparator();

        mi = new MenuItem("Kraj igre");
        mi.addActionListener(ae->{
            dispose();
        });
        m.add(mi);

        MenuShortcut ms = new MenuShortcut(VK_K);
        mi.setShortcut(ms);

        mb.add(m);

        setMenuBar(mb);
    }

    ////////////////

    private void dodajPanelZaUpravljanje(Panel p){
        Panel panel = new Panel(new GridLayout(6,1)), tpanel;
        panel.setBackground(Color.lightGray);

        ///////
        tpanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        tpanel.add(labelaBalans);
        panel.add(tpanel);
        ///////
        tpanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        tpanel.add(labelaUlog);

        poljeUlog.setColumns(5);
        poljeUlog.addTextListener(te -> {
           azurirajDobitak();
        });
        tpanel.add(poljeUlog);

        panel.add(tpanel);
        ///////
        tpanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        tpanel.add(labelaRunde);

        poljeRunde.setColumns(3);
        poljeRunde.addTextListener(te -> {
            izvlacenje.postaviUkupnoRundi(Integer.parseInt(poljeRunde.getText()));
        });
        tpanel.add(poljeRunde);

        panel.add(tpanel);

        ///////
        tpanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        tpanel.add(labelaKvota);
        panel.add(tpanel);
        ///////
        tpanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        tpanel.add(labelaDobitak);
        panel.add(tpanel);
        ///////
        tpanel = new Panel(new FlowLayout(FlowLayout.RIGHT));

        dugmeIgraj.addActionListener(ae->{
            if(mreza.brojIzabranihPolja()!= 0) novaRunda();
        });
        tpanel.add(dugmeIgraj);

        panel.add(tpanel);
        ///////

        p.add(panel, BorderLayout.EAST);
    }

    /////////////////////

    public void promenaStatusa() {
        int vrste = mreza.vrste;
        int kolone = mreza.kolone;
        int izabrano = mreza.brojIzabranihPolja();

        kvota = ( izabrano!=0 ? vrste * kolone * 1.0 / izabrano : 0.0 );
        labelaKvota.setText(format("Kvota:", kvota));

        azurirajDobitak();
    }

    private void azurirajDobitak(){
        int x;
        try {
            x = Integer.parseInt(poljeUlog.getText());
        } catch (NumberFormatException e) {
            labelaDobitak.setText("Neispravan ulog ");
            return;
        }
        if (x < 0) {
            labelaDobitak.setText("Neispravan ulog ");
            return;
        }
        else ulog = x;
        dobitak = kvota * ulog;
        labelaDobitak.setText(format("Dobitak:", dobitak));
    }

    private void novaRunda() {
        zabraniPolja();
        izvlacenje.pokreniIzvlacenje();
    }

    public Mreza dohvMrezu() { return mreza; }

    public static void main(String[] args) {
        new Igra();
    }

    public void izvucenBroj(int broj) throws InterruptedException {
        Color boja;
        Polje polje = mreza.dohvPolje(broj);
        Polje.Status stariStatus = polje.dohvStatus();

        if (mreza.dohvIzabraneBrojeve().contains(broj)) {
            boja = Color.GREEN;
            balans=+dobitak;
            polje.postaviStatus(Polje.Status.POGODJENO);
            generisanBroj.setText("Pogodak: " + new Integer(broj).toString()+" ");
        }
        else {
            boja = Color.RED;
            balans=-dobitak;
            polje.postaviStatus(Polje.Status.PROMASENO);
            generisanBroj.setText("Promasaj: " + new Integer(broj).toString()+" ");
        }

        statusnaTraka.setBackground(boja);
        generisanBroj.setBackground(boja);
        //generisanBroj.setText(new Integer(broj).toString()+" ");
        generisanBroj.setFont(new Font("Carier", Font.BOLD, 15));

        labelaBalans.setText(format("Balans:", balans));

        istorija.dodajStavku(izvlacenje.dohvTrenutnuRundu(), broj, ulog, dobitak, boja);

        Thread.sleep(1000);

        polje.postaviStatus(stariStatus);
    }

    void zabraniPolja(){
        poljeRunde.setEnabled(false);
        poljeUlog.setEnabled(false);
    }

    void dozvoliPolja(){
        poljeRunde.setEnabled(true);
        poljeUlog.setEnabled(true);
    }
}
