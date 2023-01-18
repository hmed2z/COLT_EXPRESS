//package coltExpressAhmedJessica;

//Bibliothèques et classes utilisées
import java.awt.Color;
import java.util.Random;
//import coltExpressAhmedJessica.Bandit;
//import coltExpressAhmedJessica.Marshall;
//import coltExpressAhmedJessica.Train;
//import coltExpressAhmedJessica.VueControleur;
//import coltExpressAhmedJessica.VueDuTrain;
//import coltExpressAhmedJessica.VueGenerale;
//import coltExpressAhmedJessica.Wagon;

public class Butin {
    static final int MAGOT = 0, BOURSE = 1, BIJOU = 2;
    static final Color COULEUR_BOURSE = Color.blue, COULEUR_BIJOU = Color.red, COULEUR_MAGOT = Color.green;
    public int numero, // Le num�ro du butin. Le num�ro z�ro est le magot.
            valeur, // En $.
            type; // 0 pour bourse, 1 pour bijou, 2 pour magot.
    public Color couleur;
    private int numeroWagon, //-1 si nulle part
    // positionDansWagon, // La position dans le wagon, en partant de la gauche (4 positions possibles).
    numeroBandit, // Le num�ro du bandit qui a pris le butin. Si =-1, le butin n'est avec aucun bandit.
            positionDansWagon;// 0 si dans le wagon, 1 si sur le toit.

    static public int nombreMaxButinsInitialParWagon = 4;
    static public int nombreTotalButins = 1 + nombreMaxButinsInitialParWagon * (Wagon.NB_WAGONS-1);
    static Butin butins[];


    static {
        initialiserUnePartie();
    }

    static void initialiserUnePartie() {
        Random rand = new Random();
        // Il faut pr�voir que tous les butins peuvent se retrouver dans un m�me wagon...
        butins = new Butin[nombreTotalButins];
        // Le magot (butin d'indice 0)...
        butins[0] = new Butin(0, // Num�ro du butin dans la liste de tous les butins.
                0, // Num�ro du wagon dans lequel se trouve le butin d'index i.
                -1, // Num�ro du bandit qui a pris le butin d'indice i.
                1000, // Valeur du magot (butin d'indice 0).
                MAGOT, // Type.
                0);
        // Les butins dans les wagons...
        for (int i=1; i<nombreTotalButins; i++) {
            boolean estBourse3 = rand.nextBoolean();
            int valeur3;
            if (estBourse3) valeur3 = rand.nextInt(501); // Valeur (entre 0 (inclus) et 500 (inclus)) du butin d'indice i.
            else valeur3 = 500; // Cas du bijou.
            butins[i] = new Butin(i, // Num�ro du butin dans la liste de tous les butins.
                    -1, // Num�ro du wagon dans lequel se trouve le butin d'index i (valeur provisoire).
                    -1, // Num�ro du bandit qui a pris le butin d'indice i (aucun, au d�part).
                    valeur3, // Valeur du butin d'indice i.
                    1+rand.nextInt(2), // Type de 1 (inclus) � 2 (inclus).
                    0); //Position de base en bas
        }
        //Distribution dans les wagons hors locomotive
        int indiceButinADistribuer = 1, nombreButinsDansWagon = 0;
        for (int iWagon=1; iWagon<Wagon.NB_WAGONS; iWagon++) {
            nombreButinsDansWagon = 1+rand.nextInt(nombreMaxButinsInitialParWagon); //Entre 1 et 15
            for (int j=0; j<nombreButinsDansWagon; j++) {
                butins[indiceButinADistribuer].numeroWagon = iWagon;
                indiceButinADistribuer++;
            }
        }

    }

    Butin(int numero2, int numeroWagon2, int numeroBandit2, int valeur2, int type2, int positionDansWagon2) {
        numero = numero2;
        numeroWagon = numeroWagon2;
        numeroBandit = numeroBandit2;
        valeur = valeur2;
        type = type2;
        positionDansWagon = positionDansWagon2;
        attribuerCouleur();
    }

    private void attribuerCouleur() {
        if (estBourse()) couleur = COULEUR_BOURSE;
        else if (estBijou()) couleur = COULEUR_BIJOU;
        else couleur = COULEUR_MAGOT; // Le magot.
    }

    public int getNumeroWagon() {
        return numeroWagon;
    }
    public void setNumeroWagon(int numeroWagon1) {
        this.numeroWagon = numeroWagon1;
    }
    public int getNumeroBandit() {
        return numeroBandit;
    }

    public void setNumeroBandit(int numeroBandit) {
        this.numeroBandit = numeroBandit;
    }

    public boolean estBourse() {
        return (type == BOURSE);
    }

    public boolean estBijou() {
        return (type == BIJOU);
    }

    public boolean estMagot() {
        return (type == MAGOT);
    }

    public int getPositionDansWagon() {
        return positionDansWagon;
    }

    public void setPositionDansWagon(int positionDansWagon) {
        this.positionDansWagon = positionDansWagon;
    }
}
