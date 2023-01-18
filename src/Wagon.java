//package coltExpressAhmedJessica;

//Bibliothèques et classes utilisées
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
//import coltExpressAhmedJessica.Bandit;
//import coltExpressAhmedJessica.Butin;
//import coltExpressAhmedJessica.Marshall;
//import coltExpressAhmedJessica.Train;
//import coltExpressAhmedJessica.VueControleur;
//import coltExpressAhmedJessica.VueDuTrain;
//import coltExpressAhmedJessica.VueGenerale;

public class Wagon  {
    private int numero, // Num�ro du wagon dans le train. Locomotive = num�ro 0.
            longueurWagon, hauteurWagon, espaceInterWagons, marge, yBasDeCaisse; // En coordonn�es graphiques dans la zone de dessin du train.
    final static public int NB_WAGONS = 4; // Y compris locomotive, qui sera le wagon d'indice 0.
    public Wagon(int numero1) {
        numero = numero1;
    }

    public int nombreDeButins() {
        int nbButins = 0;
        for (int i=0; i<Butin.nombreTotalButins; i++) {
            if (Butin.butins[i].getNumeroWagon() == numero) nbButins++;
        }
        return nbButins;
    }

    public void dessiner(Graphics2D g2) {
        dessinerWagonMateriel(g2);
        dessinerBanditsMarshallButins(g2);
    }

    /**
     * On va dessiner le marshall et les bandits au-dessus des butins.
     * @param g2
     */
    public void dessinerBanditsMarshallButins(Graphics2D g2) {
        int x = marge + numero * (longueurWagon + espaceInterWagons); int y = yBasDeCaisse - hauteurWagon;
        int positionXBandit, positionYBandit, nbBanditsDessinesEnBas=0, nbBanditsDessinesEnHaut=0,
                positionXMarshall, positionYMarshall, indiceXButin=0, indiceYButin=0, positionXButin, positionYButin;
        // Dessin des bandits...
        for (int i=0; i<Bandit.NB_BANDITS; i++) {
            if (Bandit.bandits[i].getNumeroWagon() == this.numero) {
                if (Bandit.bandits[i].getPositionDansWagon() == 0) { // Cas du bandit en bas du wagon.
                    positionYBandit = y + hauteurWagon/2; // "y" est l'ordonn�e du toit du wagon.
                    positionXBandit = x+longueurWagon/40+(1+nbBanditsDessinesEnBas)*longueurWagon/6; // On laisse la place � gauche pour le marshall.
                    nbBanditsDessinesEnBas++;
                }
                else { // Cas du bandit en haut du wagon (sur le toit).
                    positionYBandit = y - hauteurWagon/2;
                    positionXBandit = x+longueurWagon/7+(1+nbBanditsDessinesEnHaut)*longueurWagon/8; // En r�servant une place � gauche, on �vite qu'un bandit soit dessin� sur la chemin�e.
                    nbBanditsDessinesEnHaut++;
                }
                // g2.drawString("B"+String.valueOf(i), positionXBandit, positionYBandit);
                g2.drawString(Bandit.bandits[i].diminutif, positionXBandit, positionYBandit);

            }
        }
        // Dessin du marshall
        for (int i=0; i<NB_WAGONS; i++) { /// ### Provisoire.
            if (VueGenerale.marshall.getNumeroWagon() == this.numero) {
                positionXMarshall = x + longueurWagon/40;
                positionYMarshall = y + hauteurWagon/2;
                g2.drawString("M", positionXMarshall, positionYMarshall);
            }
        }
        // Dessin des butins
        // Dessin des butins qui sont dans le wagon. On les met sur 2 lignes, car si tous les butins se
        // retrouvent dans un m�me wagon, on n'aura pas la place de les mettre sur une seule ligne.
        indiceXButin=0; indiceYButin=0; // Position du butin dans ou sur le wagon.
        for (int i=0; i<Butin.nombreTotalButins; i++) {
            if (Butin.butins[i].getNumeroWagon() == this.numero && Butin.butins[i].getPositionDansWagon()==0) {
                positionXButin = x + longueurWagon/20 + indiceXButin * longueurWagon/10;
                positionYButin = y + 16 * hauteurWagon/20 - indiceYButin * hauteurWagon/6;
                g2.setColor(Butin.butins[i].couleur);
                g2.fillRect(positionXButin, positionYButin, longueurWagon/12, hauteurWagon/12);
                indiceXButin++; if (indiceXButin>7) { indiceXButin=0; indiceYButin++; }
            }
        }
        // Dessin des butins qui sont dans sur le toit du wagon
        indiceXButin=0; indiceYButin=0;
        for (int i=0; i<Butin.nombreTotalButins; i++) {
            if (Butin.butins[i].getNumeroWagon() == this.numero && Butin.butins[i].getPositionDansWagon()==1) {
                positionXButin = x + longueurWagon/20 + indiceXButin * longueurWagon/10;
                positionYButin = y - hauteurWagon/6 - indiceYButin * hauteurWagon/6;
                g2.setColor(Butin.butins[i].couleur);
                g2.fillRect(positionXButin, positionYButin, longueurWagon/12, hauteurWagon/12);
                indiceXButin++; if (indiceXButin>7) { indiceXButin=0; indiceYButin++; }
            }
        }
    }

    public void mettreAJourDimensions(int longueurWagon1, int hauteurWagon1, int espaceInterWagons1, int marge1, int yBasDeCaisse1) {
        // Dimensions diverses
        longueurWagon = longueurWagon1; hauteurWagon = hauteurWagon1; espaceInterWagons = espaceInterWagons1;
        marge = marge1; yBasDeCaisse = yBasDeCaisse1;
    }

    public int getNumero() {
        return numero;
    }

    public void dessinerWagonMateriel(Graphics2D g2) {
        int x = marge + numero * (longueurWagon + espaceInterWagons);
        int y = yBasDeCaisse - hauteurWagon;

        // Le wagon...
        g2.setStroke(new BasicStroke(2));
        if (numero == 0 || numero == 2) g2.setColor(Color.pink);// Si locomotive.
        else g2.setColor(Color.magenta);
        g2.fillRect(x, y, longueurWagon, hauteurWagon);
        g2.setColor(Color.RED);
        g2.drawRect(x, y, longueurWagon, hauteurWagon);

        // Les roues
        g2.setColor(Color.pink); g2.fillOval(x+longueurWagon/10, yBasDeCaisse, hauteurWagon/2, hauteurWagon/2);
        g2.setColor(Color.yellow); g2.drawOval(x+longueurWagon/10, yBasDeCaisse, hauteurWagon/2, hauteurWagon/2);
        g2.setColor(Color.black); g2.drawOval(x+longueurWagon/7,220, hauteurWagon/4, hauteurWagon/4);

        g2.setColor(Color.magenta); g2.fillOval(x+longueurWagon/10+5*hauteurWagon/12, yBasDeCaisse, hauteurWagon/2, hauteurWagon/2);
        g2.setColor(Color.yellow); g2.drawOval(x+longueurWagon/10+5*hauteurWagon/12, yBasDeCaisse, hauteurWagon/2, hauteurWagon/2);
        g2.setColor(Color.black); g2.drawOval(x+longueurWagon/7+5*hauteurWagon/12, 220, hauteurWagon/4, hauteurWagon/4);

        g2.setColor(Color.pink); g2.fillOval(x+9*longueurWagon/10-hauteurWagon/3, yBasDeCaisse, hauteurWagon/2, hauteurWagon/2);
        g2.setColor(Color.YELLOW); g2.drawOval(x+9*longueurWagon/10-hauteurWagon/3, yBasDeCaisse, hauteurWagon/2, hauteurWagon/2);
        g2.setColor(Color.BLACK); g2.drawOval(x+1*longueurWagon-hauteurWagon/2, 220, hauteurWagon/4, hauteurWagon/4);

        g2.setColor(Color.magenta); g2.fillOval(x+9*longueurWagon/10-5*hauteurWagon/12-hauteurWagon/3, yBasDeCaisse, hauteurWagon/2, hauteurWagon/2);
        g2.setColor(Color.yellow); g2.drawOval(x+9*longueurWagon/10-5*hauteurWagon/12-hauteurWagon/3, yBasDeCaisse, hauteurWagon/2, hauteurWagon/2);
        g2.setColor(Color.BLACK); g2.drawOval(x+94*longueurWagon/100-5*hauteurWagon/12-hauteurWagon/3, 220, hauteurWagon/4, hauteurWagon/4);


        // La chemin�e de la locomotive
        if (numero == 0) {
            g2.setColor(Color.red); g2.fillRect(x, y-78*hauteurWagon/100, longueurWagon/4, 3*hauteurWagon/4);
            g2.setColor(Color.pink); g2.drawRect(x, y-78*hauteurWagon/100, longueurWagon/4, 3*hauteurWagon/4);


        }

        // L'attache entre les wagons
        if (numero != NB_WAGONS-1) {
            g2.setColor(Color.BLACK);
            g2.fillRect(x+longueurWagon, yBasDeCaisse-2*hauteurWagon/10, espaceInterWagons, hauteurWagon/10); // La barre horizontale entre 2 wagons.
            g2.fillRect(x+longueurWagon+espaceInterWagons/2, yBasDeCaisse-3*hauteurWagon/10, espaceInterWagons/5, hauteurWagon/8); // La cl�.
        }
    }



}
