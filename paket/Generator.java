package paket;

import java.util.ArrayList;

public class Generator {

    private int donjaGranica;
    private int gornjaGranica;

    //////////

    public Generator(int donjaGranica, int gornjaGranica) {
        this.donjaGranica = donjaGranica;
        this.gornjaGranica = gornjaGranica;
    }

    public int rand(){
        return donjaGranica + (int)(Math.random() * 100) % (gornjaGranica - donjaGranica + 1);
    }

    public ArrayList<Integer> randNiz(int n) {
        if (n<=0) return null;
        ArrayList<Integer> niz = new ArrayList<>();
        for(int i = 0; i < n; i++){
            niz.add(rand());
        }
        return niz;
    }

    public static void main(String[] args) {
        Generator g = new Generator(1, 20);
        System.out.println(g.rand() + " ");
        System.out.println(g.rand() + " ");
        System.out.println(g.rand() + " ");
        System.out.println(g.rand() + " ");
        System.out.println(g.rand() + " ");
    }
}
