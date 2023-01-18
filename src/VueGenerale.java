//package coltExpressAhmedJessica;

//Bibliothèques et classes utilisées
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JFrame;
//import coltExpressAhmedJessica.Bandit;
//import coltExpressAhmedJessica.Butin;
//import coltExpressAhmedJessica.Marshall;
//import coltExpressAhmedJessica.Train;
//import coltExpressAhmedJessica.VueControleur;
//import coltExpressAhmedJessica.VueDuTrain;
//import coltExpressAhmedJessica.Wagon;

//Classe Principale avec les composants et la fenetre graphique
public class VueGenerale extends JFrame {

    static VueGenerale vueGenerale;
    static VueDuTrain vueDuTrain;
    static VueControleur vueControleur;
    static Train train;
    static Marshall marshall;
    static int largeurFenetre;
    static int hauteurFenetre;
    static Graphics g;
    Font font1;
    Dimension screenSize;

    public VueGenerale() {
        super("Jeu Colt Express By Ahmed & Jessica");
        g = getGraphics();
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        largeurFenetre = screenSize.width/4; hauteurFenetre = screenSize.height/2;
        this.setLocation(screenSize.width/200, screenSize.height/4);
        this.setSize(largeurFenetre, hauteurFenetre);
        font1 = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        // La vue qui permet de dessiner les �l�ments du train...
        vueDuTrain = new VueDuTrain(); // A cr�er avant le train, car "train" est un observable pour "vueDuTrain".
        add(vueDuTrain);
        train = new Train();
        marshall = new Marshall(0); // Au d�but : dans la locomotive.
        vueControleur = new VueControleur(); // A cr�er en dernier.
        add(vueControleur);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }


    public void validate() {
        super.validate();
        largeurFenetre = this.getContentPane().getWidth();
        hauteurFenetre = this.getContentPane().getHeight();
        this.getContentPane().getInsets().set(0, 0, 0, 0);
        vueControleur.definirLimitesDuCadre(largeurFenetre/40, hauteurFenetre/40, 38*largeurFenetre/40, 22*hauteurFenetre/40);
        vueDuTrain.definirLimitesDuCadre(largeurFenetre/40, 24*hauteurFenetre/40, 38*largeurFenetre/40, 15*hauteurFenetre/40);
    }

    public static void main(String[] args) {
        vueGenerale = new VueGenerale();
        System.out.println("\n");

    }

}
