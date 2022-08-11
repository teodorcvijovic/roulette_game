package paket;

import java.util.ArrayList;
import java.util.HashSet;

public class GeneratorJedinstvenih extends Generator {

    public GeneratorJedinstvenih(int donjaGranica, int gornjaGranica) {
        super(donjaGranica, gornjaGranica);
    }

    public ArrayList<Integer> randNiz(int n){
        HashSet<Integer> niz = new HashSet<>();

        for(int i = 0; i<n; ){
            int broj = rand();
            if (niz.contains(broj)) continue;
            i++;
        }

        return new ArrayList<>(niz);
    }
}
