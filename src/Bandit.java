//package coltExpressAhmedJessica;

//Bibliothèques et classes utilisées
import java.awt.Font;
import java.util.Random;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
//import coltExpressAhmedJessica.Butin;
//import coltExpressAhmedJessica.Marshall;
//import coltExpressAhmedJessica.Train;
//import coltExpressAhmedJessica.VueControleur;
//import coltExpressAhmedJessica.VueDuTrain;
//import coltExpressAhmedJessica.VueGenerale;
//import coltExpressAhmedJessica.Wagon;

//Classe gérant les bandits
public class Bandit  {

    final static public int NB_BANDITS = 4;
    final static String[] nomsBandits = { "Akram", "Sylvain" ,"Pierre","Lucien"};
    static public Object[][] dataBandits;
    static public Bandit[] bandits;
    static private Random randButin = new Random();
    static JScrollPane scrollpaneBandits;
    static JTable tableauBandits;
    static Font font1;
    public int numero;
    public String nom, diminutif;
    private int numeroWagon, positionDansWagon, montant, nbBalles;

    static {
        bandits = new Bandit[NB_BANDITS];
        for (int i=0; i<NB_BANDITS; i++) Bandit.bandits[i] = new Bandit(i, Wagon.NB_WAGONS-1-i, 1, nomsBandits[i]);

        // Le tableau des bandits
        String[] columnNames = { "Nom", "Balles restantes", "Butin Amassé" };
        dataBandits = new Object[NB_BANDITS][3];
        for (int i=0; i<NB_BANDITS; i++) {
            dataBandits[i][0] = (Object) nomsBandits[i];
        }
        font1 = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        tableauBandits = new JTable(dataBandits, columnNames); tableauBandits.setEnabled(false);
        tableauBandits.setFont(font1);
        tableauBandits.getTableHeader().setFont(font1);
        tableauBandits.setRowHeight(3*font1.getSize());
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer = (DefaultTableCellRenderer) tableauBandits.getDefaultRenderer(String.class);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        tableauBandits.setDefaultRenderer(String.class, renderer);
        tableauBandits.setFillsViewportHeight(false);
        scrollpaneBandits = new JScrollPane(tableauBandits);

        initialiserUnePartie();
    }

    static void initialiserUnePartie() {
        for (int i=0; i<Bandit.NB_BANDITS; i++) {
            Bandit.bandits[i].setNumeroWagon(Wagon.NB_WAGONS-1-i);
            Bandit.bandits[i].setPositionDansWagon(1);
            dataBandits[i][1] = VueControleur.NB_BALLES;
            dataBandits[i][2] = (Object)0;
        }
    }

    Bandit(int numero2, int numeroWagon2, int positionDansWagon2, String nom1) {
        numero = numero2;
        numeroWagon = numeroWagon2;
        positionDansWagon = positionDansWagon2;
        montant = 0;
        nbBalles = VueControleur.NB_BALLES;
        nom = nom1;
        diminutif = nom.substring(0, 1);
    }

    public void lacheUnButin() {

        int[] liste=new int[Butin.nombreTotalButins]; int nbDansListe = 0;
        for (int i=0; i<Butin.nombreTotalButins; i++) {
            if (Butin.butins[i].getNumeroBandit() == numero) {
                liste[nbDansListe] = i; nbDansListe++;
            }

            if (nbDansListe>=1) {
                int butinChoisi = randButin.nextInt(nbDansListe);
                Butin.butins[butinChoisi].setNumeroBandit(-1);
                Butin.butins[butinChoisi].setNumeroWagon(numeroWagon);
                Butin.butins[butinChoisi].setPositionDansWagon(positionDansWagon);
                int montantButin = Butin.butins[butinChoisi].valeur;
                int ancienMontant = montant;

                System.out.println(nom + " l�che un butin dans le wagon " + numeroWagon);
                setMontant(ancienMontant-montantButin);
                break; // Le bandit ne l�che qu'un butin.
            }
        }
    }

    public void recupereButin(int numWagon1) {
        // On va enlever un butin au hasard dans le wagon "wagon1"...
        // Construction de la liste des butins pr�sents dans le wagon.
        int[] liste=new int[Butin.nombreTotalButins]; int nbDansListe = 0;
        for (int i=0; i<Butin.nombreTotalButins; i++) {
            if (Butin.butins[i].getNumeroWagon() == numWagon1 && Butin.butins[i].getPositionDansWagon() == positionDansWagon) {
                liste[nbDansListe] = i; nbDansListe++;
            }
        }
        // Choix d'un butin au hasard parmi ceux de la liste
        if (nbDansListe>=1) {
            int butinChoisi = randButin.nextInt(nbDansListe);
            int iButin = liste[butinChoisi];
            Butin.butins[iButin].setNumeroBandit(numero);
            Butin.butins[iButin].setNumeroWagon(-1);
            int montantButin = Butin.butins[iButin].valeur;
            int ancienMontant = montant;
            setMontant(ancienMontant+montantButin);

            System.out.println(nom + " recupere un butin dans le wagon " + numeroWagon);
        }
    }

    public void estConfronteAuMarshall() {
        System.out.println(nom + " est confronte au marshall dans le wagon " + numeroWagon);
    }

    public void neFaitRien() {
        System.out.println(nom + " ne fait rien.");
    }


    public void seDeplace(VueControleur.Direction direction) {
        String message1;
        switch(direction) {
            case AVANT:
                if (numeroWagon > 0) {
                    numeroWagon--;
                    if (numeroWagon !=0) message1 = "va vers l'avant du train, arrive dans le wagon " + numeroWagon + ".";
                    else message1 = "va vers l'avant du train, arrive a la locomotive.";
                }
                else message1 = "est deja a la locomotive.";
                break;
            case ARRIERE:
                if (numeroWagon < Wagon.NB_WAGONS-1) {
                    numeroWagon++;
                    message1 = "va vers l'arriere du train, arrive dans le wagon " + numeroWagon + ".";

                }
                else message1 = "est deja au dernier wagon.";
                break;
            case HAUT:
                if (positionDansWagon == 0) {
                    positionDansWagon = 1;
                    if (numeroWagon !=0) message1 = "grimpe sur le toit du wagon " + numeroWagon + ".";
                    else message1 = "grimpe sur le toit de la locomotive.";

                }
                else {
                    if (numeroWagon !=0) message1 = "est deja sur le toit du wagon " + numeroWagon + ".";
                    else message1 = "est deja sur le toit de la locomotive.";
                }
                break;
            case BAS:
                if (positionDansWagon == 1) {
                    positionDansWagon = 0;
                    if (numeroWagon !=0) message1 = "descend dans le wagon " + numeroWagon + ".";
                    else message1 = "descend dans la locomotive.";
                }
                else {
                    if (numeroWagon !=0) message1 = "est d�j� dans le wagon " + numeroWagon + ".";
                    else message1 = "est deja dans la locomotive.";
                }
                break;
            default: message1 = "ne fait rien.";
        }
        System.out.println(nom + " " + message1);
    }

    public void tire(VueControleur.Direction direction) {
        if (getNbBalles() <=0) return;
        String message1 = "plus de balles !";
        switch(direction) {
            case AVANT:
                if (numeroWagon > 0) {
                    int num1 = numeroBanditAleatoireVersLAvant();
                    if (num1 >= 0) {
                        message1 = "tire vers l'avant du train depuis le wagon " + numeroWagon + "sur le bandit " + bandits[num1].nom + ".";
                        bandits[num1].lacheUnButin();
                    }
                    else {
                        message1 = "tire vers l'avant du train depuis le wagon " + numeroWagon + ".";
                    }
                }
                else message1 = "tire vers l'avant du train depuis la locomotive.";
                setNbBalles(getNbBalles() - 1); // Vrai dans tous les cas.
                break;
            case ARRIERE:
                if (numeroWagon < Wagon.NB_WAGONS-1) {
                    int num1 = numeroBanditAleatoireVersLArriere();
                    if (num1 >= 0) { // Il y a un bandit vis�.
                        message1 = "tire vers l'arriere du train depuis le wagon " + numeroWagon + "sur le bandit " + bandits[num1].nom + ".";
                        bandits[num1].lacheUnButin();
                    }
                    else { // Pas de bandit vis�.
                        message1 = "tire vers l'arriere du train depuis le wagon " + numeroWagon + ".";
                    }
                }
                else message1 = "tire vers l'arri�re du train depuis le dernier wagon.";
                setNbBalles(getNbBalles() - 1); // Vrai dans tous les cas.
                break;
            case HAUT:
                if (positionDansWagon == 0) {
                    int num1 = numeroBanditAleatoireDessusOuDessous();
                    if (num1 >=0) {
                        bandits[num1].lacheUnButin();
                        if (numeroWagon !=0) message1 = "tire sur le bandit " + bandits[num1].nom + " sur le toit du wagon " + numeroWagon + ".";
                        else message1 = "tire sur le bandit " + bandits[num1].nom + " sur le toit de la locomotive.";
                    }
                }
                else {
                    int num1 = numeroBanditAleatoireALaMemePlace();
                    if (num1 >=0) {
                        bandits[num1].lacheUnButin();
                        if (numeroWagon !=0) message1 = "tire sur le bandit " + bandits[num1].nom + " sur le toit du wagon " + numeroWagon + ".";
                        else message1 = "tire sur le bandit " + bandits[num1].nom + " sur le toit de la locomotive.";
                    }
                }
                setNbBalles(getNbBalles() - 1);
                break;
            case BAS:
                if (positionDansWagon == 1) {
                    int num1 = numeroBanditAleatoireDessusOuDessous();
                    if (num1 >=0) {
                        bandits[num1].lacheUnButin();
                        if (numeroWagon !=0) message1 = "tire sur le bandit " + bandits[num1].nom + " dans le wagon " + numeroWagon + ".";
                        else message1 = "tire sur le bandit " + bandits[num1].nom + " dans la locomotive.";
                    }
                }
                else {
                    int num1 = numeroBanditAleatoireALaMemePlace();
                    if (num1 >=0) {
                        bandits[num1].lacheUnButin();
                        if (numeroWagon !=0) message1 = "tire sur le bandit " + bandits[num1].nom + " dans le wagon " + numeroWagon + ".";
                        else message1 = "tire sur le bandit " + bandits[num1].nom + " dans la locomotive.";
                    }
                }
                setNbBalles(getNbBalles() - 1);
                break;
            default: message1 = "ne fait rien.";
        }
        System.out.println(nom + " " + message1);
    }


    //Dit s'il y a un bandit ou pas en haut (si on y est) ou en bas (si on y est)
    int numeroBanditAleatoireALaMemePlace() {
        int[] liste=new int[NB_BANDITS]; int nbDansListe = 0;
        for (int i=0; i<NB_BANDITS; i++) {
            if (i==numero) continue;
            if (bandits[i].numeroWagon == numeroWagon && bandits[i].positionDansWagon == positionDansWagon) {
                liste[nbDansListe] = i; nbDansListe++;
            }
            // Ici, la liste est compl�te.
            if (nbDansListe == 0) return -1; // Pas d'autre bandit � la m�me position que le bandit en cours.
            int indChoisi = randButin.nextInt(nbDansListe);
            return indChoisi;
        }
        return 0;
    }

    //Pareil que la méthode plus haut mais lorsqu'on n'y est pas
    int numeroBanditAleatoireDessusOuDessous() {
        int[] liste=new int[NB_BANDITS]; int nbDansListe = 0;
        for (int i=0; i<NB_BANDITS; i++) {
            if (i==numero) continue;
            if (bandits[i].numeroWagon == numeroWagon && bandits[i].positionDansWagon == 1 - positionDansWagon) {
                liste[nbDansListe] = i; nbDansListe++;
            }
            // Ici, la liste est compl�te.
            if (nbDansListe == 0) return -1; // Pas d'autre bandit � la m�me position que le bandit en cours.
            int indChoisi = randButin.nextInt(nbDansListe);
            return indChoisi;
        }
        return 0;
    }

    //Pareil mais vers l'arriere
    int numeroBanditAleatoireVersLArriere() {
        int[] liste=new int[NB_BANDITS]; int nbDansListe = 0;
        for (int i=0; i<NB_BANDITS; i++) {
            if (i==numero) continue;
            if (bandits[i].numeroWagon == numeroWagon + 1 && bandits[i].positionDansWagon == positionDansWagon) {
                liste[nbDansListe] = i; nbDansListe++;
            }
            // Ici, la liste est compl�te.
            if (nbDansListe == 0) return -1; // Pas d'autre bandit � la m�me position que le bandit en cours.
            int indChoisi = randButin.nextInt(nbDansListe);
            return indChoisi;
        }
        return 0;
    }

    //Pareil mais vers l'avant
    int numeroBanditAleatoireVersLAvant() {
        int[] liste=new int[NB_BANDITS]; int nbDansListe = 0;
        for (int i=0; i<NB_BANDITS; i++) {
            if (i==numero) continue;
            if (bandits[i].numeroWagon == numeroWagon - 1 && bandits[i].positionDansWagon == positionDansWagon) {
                liste[nbDansListe] = i; nbDansListe++;
            }
            // Ici, la liste est compl�te.
            if (nbDansListe == 0) return -1; // Pas d'autre bandit � la m�me position que le bandit en cours.
            int indChoisi = randButin.nextInt(nbDansListe);
            return indChoisi;
        }
        return 0;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant1) {
        montant = montant1;
        dataBandits[numero][2] = montant;
    }

    public void setNumeroWagon(int numeroWagon1) {
        numeroWagon = numeroWagon1;
    }

    public int getNumeroWagon() {
        return numeroWagon;
    }

    public void setPositionDansWagon(int positionDansWagon1) {
        positionDansWagon = positionDansWagon1;
    }

    public int getPositionDansWagon() {
        return positionDansWagon;
    }

    public int getNbBalles() {
        return nbBalles;
    }

    public void setNbBalles(int nbBalles) {
        this.nbBalles = nbBalles;
    }
}

