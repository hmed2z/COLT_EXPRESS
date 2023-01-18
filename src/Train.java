//package coltExpressAhmedJessica;

//Bibliothèques et classes utilisées
import java.awt.Graphics2D;
//import coltExpressAhmedJessica.Bandit;
//import coltExpressAhmedJessica.Butin;
//import coltExpressAhmedJessica.Marshall;
//import coltExpressAhmedJessica.VueControleur;
//import coltExpressAhmedJessica.VueDuTrain;
//import coltExpressAhmedJessica.VueGenerale;
//import coltExpressAhmedJessica.Wagon;

public class Train {
    public static Wagon[] wagons;

    public Train() {
        wagons = new Wagon[Wagon.NB_WAGONS];
        for (int i=0; i<Wagon.NB_WAGONS; i++) wagons[i] = new Wagon(i);
    }

    public void mettreAJourDimensions(int longueurWagon1, int hauteurWagon1, int espaceInterWagons1, int marge1, int yBasDeCaisse1) {
        for (int i=0; i<Wagon.NB_WAGONS; i++)
            wagons[i].mettreAJourDimensions(longueurWagon1, hauteurWagon1, espaceInterWagons1, marge1, yBasDeCaisse1);
    }

    public void dessiner(Graphics2D g2) {
        for (int i=0; i<Wagon.NB_WAGONS; i++) {
            wagons[i].dessiner(g2);
        }
    }
}
