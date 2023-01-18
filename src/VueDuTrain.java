//package coltExpressAhmedJessica;

//Bibliothèques et classes utilisées
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
//import coltExpressAhmedJessica.Bandit;
//import coltExpressAhmedJessica.Butin;
//import coltExpressAhmedJessica.Marshall;
//import coltExpressAhmedJessica.Train;
//import coltExpressAhmedJessica.VueControleur;
//import coltExpressAhmedJessica.VueGenerale;
//import coltExpressAhmedJessica.Wagon;

//Arrière plan du Train
public class VueDuTrain extends JPanel  {
    public static Graphics2D g2d;
    public static final int WAGON = 0;
    public static final int VUE = 1;
    public int largeurCadreDessin, hauteurCadreDessin;
    public int longueurWagon1, hauteurWagon1, espaceInterWagons1, marge1, yBasDeCaisse1;
    Font font4;

    public void definirLimitesDuCadre(int offsetXCadreDessin1, int offsetYCadreDessin1, int largeurCadreDessin1, int hauteurCadreDessin1) {
        largeurCadreDessin = largeurCadreDessin1; hauteurCadreDessin = hauteurCadreDessin1;
        setBounds(offsetXCadreDessin1, offsetYCadreDessin1, largeurCadreDessin, hauteurCadreDessin);
    }

    //Affichage du Train
    public void validate() {
        super.validate();
        largeurCadreDessin = this.getWidth();
        hauteurCadreDessin = this.getHeight();
        int longueurPlusEspace = 9*largeurCadreDessin/10/Wagon.NB_WAGONS;
        marge1 = largeurCadreDessin/10/2;
        espaceInterWagons1 = longueurPlusEspace/6;
        longueurWagon1 = 5*longueurPlusEspace/6;
        hauteurWagon1 = longueurWagon1/3;
        yBasDeCaisse1 = 4*hauteurCadreDessin/5;
        setFont(new Font(Font.SANS_SERIF, Font.BOLD, 4*hauteurWagon1/9));
    }

    //Affichage de l'arrière-plan derrière le Train
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D)g;
        setBackground(Color.GREEN);
        VueGenerale.train.mettreAJourDimensions(longueurWagon1, hauteurWagon1, espaceInterWagons1, marge1, yBasDeCaisse1);
        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 4*hauteurWagon1/9));
        VueGenerale.train.dessiner((Graphics2D)g);
    }
}
