//package coltExpressAhmedJessica;

//Bibliothèques et classes utilisées
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JViewport;
//import coltExpressAhmedJessica.Bandit;
//import coltExpressAhmedJessica.Butin;
//import coltExpressAhmedJessica.Marshall;
//import coltExpressAhmedJessica.Train;
//import coltExpressAhmedJessica.VueDuTrain;
//import coltExpressAhmedJessica.VueGenerale;
//import coltExpressAhmedJessica.Wagon;

//Gestion de la partie supérieure de l'interface graphique
public class VueControleur extends JPanel {

    enum Direction {AVANT,ARRIERE,HAUT,BAS};
    enum Phase {PLANIFICATION,ACTION};
    int largeurFenetre, hauteurFenetre;
    Font font1, font5, fontPetite;
    JButton boutonPlanification, boutonAction, boutonNouvellePartie, boutonCorrectionPlanification, boutonsEnregistrerPrendreButin, boutonsEnregistrerNeRienFaire;
    JButton[] boutonsJoueur, boutonsEnregistrerDeplacement, boutonsEnregistrerTir;
    Action[][] actionJoueurPlanificationEnregistree; // 1er indice : joueur. 2ème indice : étape (n° d'action).
    JLabel texteLigneChoixDuJoueurPlanificateur, textePlanificationAction, texteLigneBoutonsPlanificationDeplacement,
            texteLigneBoutonsPlanificationTir, texteLignePlanificationEnregistree, textePlanificationPrendre,
            textePlanificationNeRienFaire, textePlanificationEnregistree;

    Phase phase;
    int positionCurseurActionsEnregistrees = -1;
    int joueurEnCoursDePlanification = 0;
    static final int NB_ACTIONS = 4;
    static final int NB_JOUEURS = 4;
    static final int NB_BALLES = 4;
    //static final int TABLEAU_DES_BANDITS = 2;
    static String[] symbolesDeDirection = { "<", ">", "^", "V" };
    static String[] tooltipDeDirection = { "vers la gauche.", "vers la droite.", "vers le haut.", "vers le bas." };
    Dimension fixedSize;
    JViewport viewport = new JViewport();
    Controleur controleur;

    /**
     * Constructeur.
     */
    VueControleur() {
        controleur = new Controleur();
        font1 = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        font5 = new Font(Font.SANS_SERIF, Font.BOLD, 60);
        fontPetite = new Font(Font.SANS_SERIF, Font.BOLD, 10);
        // Le bouton "Planification/Action"...
        boutonPlanification=new JButton("Planification/Action");
        add(boutonPlanification);
        boutonPlanification.addActionListener(controleur);
        // Le bouton "Action !"...
        boutonAction=new JButton("Action !");
        add(boutonAction);
        boutonAction.addActionListener(controleur);
        // Le bouton "Nouvelle partie"...
        boutonNouvellePartie=new JButton("Nouvelle partie");
        add(boutonNouvellePartie);
        boutonNouvellePartie.addActionListener(controleur);
        // Le tableau d'affichage des bandits...
        Bandit.scrollpaneBandits.setFont(font1);
        add(Bandit.scrollpaneBandits, BorderLayout.CENTER); // Il faut utiliser un scrollPanel si on veut avoir facilement les titres des colonnes.
        // Les boutons de planification...
        boutonsJoueur = new JButton[NB_JOUEURS];
        boutonsEnregistrerDeplacement = new JButton[NB_ACTIONS];
        boutonsEnregistrerTir = new JButton[NB_ACTIONS];
        for (int i=0; i<NB_ACTIONS; i++) {
            boutonsEnregistrerDeplacement[i] = new JButton(symbolesDeDirection[i]);
            boutonsEnregistrerDeplacement[i].setToolTipText("Aller vers "+ tooltipDeDirection[i]);
            add(boutonsEnregistrerDeplacement[i]);
            boutonsEnregistrerDeplacement[i].addActionListener(controleur);
            boutonsEnregistrerTir[i] = new JButton(symbolesDeDirection[i]);
            boutonsEnregistrerTir[i].setToolTipText("Tirer vers "+ tooltipDeDirection[i]);
            add(boutonsEnregistrerTir[i]);
            boutonsEnregistrerTir[i].addActionListener(controleur);
        }
        for (int i=0; i<NB_JOUEURS; i++) {
            // boutonsJoueur[i] = new JButton(String.valueOf(i));
            boutonsJoueur[i] = new JButton(Bandit.bandits[i].diminutif);
            boutonsJoueur[i].setToolTipText(Bandit.bandits[i].nom);
            add(boutonsJoueur[i]);
            boutonsJoueur[i].addActionListener(controleur);
            boutonsJoueur[i].setBackground(i == 0 ? Color.blue : Color.white);
            boutonsJoueur[i].repaint();
        }
        // Le bouton pour prendre un butin (dans le wagon ou sur le toit)...
        boutonsEnregistrerPrendreButin = new JButton("✓");
        boutonsEnregistrerPrendreButin.setToolTipText("✨ Prendre le butin trouvé ✨");
        add(boutonsEnregistrerPrendreButin);
        boutonsEnregistrerPrendreButin.addActionListener(controleur);
        // Le bouton pour ne rien faire...
        boutonsEnregistrerNeRienFaire = new JButton("❌");
        boutonsEnregistrerNeRienFaire.setToolTipText("Ne rien faire ✘");
        add(boutonsEnregistrerNeRienFaire);
        boutonsEnregistrerNeRienFaire.addActionListener(controleur);
        // Le bouton permettant de corriger la planification...
        boutonCorrectionPlanification=new JButton("⌫");
        boutonCorrectionPlanification.setToolTipText("Effacer la dernière instruction.");
        add(boutonCorrectionPlanification);
        boutonCorrectionPlanification.addActionListener(controleur);
        // Le texte qui indique si on est en phase de planification ou en phase d'action...
        textePlanificationAction = new JLabel();
        //phase = Phase.PLANIFICATION;
        //textePlanificationAction.setText(phase == Phase.PLANIFICATION ? "Phase planification" : "Phase action");
        textePlanificationAction.setFont(font1);
        add(textePlanificationAction);

        // Les textes qui désignent la ligne des boutons de déplacement et celle des boutons de tir...
        texteLigneChoixDuJoueurPlanificateur = new JLabel("🧍 Joueur 🧍 :");
        texteLigneChoixDuJoueurPlanificateur.setFont(font1);
        add(texteLigneChoixDuJoueurPlanificateur);
        texteLigneBoutonsPlanificationDeplacement = new JLabel("Vos Déplacements ☛ :");
        texteLigneBoutonsPlanificationDeplacement.setFont(font1);
        add(texteLigneBoutonsPlanificationDeplacement);
        texteLigneBoutonsPlanificationTir = new JLabel("✓ Tirs ✓ :");
        texteLigneBoutonsPlanificationTir.setFont(font1);
        add(texteLigneBoutonsPlanificationTir);
        textePlanificationPrendre = new JLabel(" ✨ Prendre butin ✨ :");
        textePlanificationPrendre.setFont(font1);
        add(textePlanificationPrendre);
        textePlanificationNeRienFaire = new JLabel("❌ Ne rien faire ❌: ");
        textePlanificationNeRienFaire.setFont(font1);
        add(textePlanificationNeRienFaire);
        texteLignePlanificationEnregistree = new JLabel("Les instructions choisies ☛ :");
        texteLignePlanificationEnregistree.setFont(font1);
        add(texteLignePlanificationEnregistree);
        textePlanificationEnregistree = new JLabel("- - - -");
        textePlanificationEnregistree.setFont(font1);
        add(textePlanificationEnregistree);
        // Initialisation des actions enregistrées par chaque joueur...
        actionJoueurPlanificationEnregistree = new Action[NB_JOUEURS][NB_ACTIONS];
        controleur = new Controleur();
        initialiserUnePartie();
    }

    void initialiserUnePartie() {
        for (int i=0; i<NB_JOUEURS; i++) {
            for (int j=0; j<NB_ACTIONS; j++) {
                actionJoueurPlanificationEnregistree[i][j] = new Action(0, 'X', Direction.AVANT); // Action nulle.
            }
        }
        phase = Phase.PLANIFICATION;
        textePlanificationAction.setText(phase == Phase.PLANIFICATION ? "Phase planification" : "Phase action");
        Bandit.initialiserUnePartie();
        VueGenerale.marshall.setNumeroWagon(0);
        Butin.initialiserUnePartie();
    }

    public void definirLimitesDuCadre(int offsetXCadreDessin1, int offsetYCadreDessin1, int largeurCadreDessin1, int hauteurCadreDessin1) {
        largeurFenetre = largeurCadreDessin1; hauteurFenetre = hauteurCadreDessin1;
        setBounds(offsetXCadreDessin1, offsetYCadreDessin1, largeurFenetre, hauteurFenetre);
    }

    public void validate() {
        super.validate();

        setFont(font1);
        largeurFenetre = this.getWidth();
        hauteurFenetre = this.getHeight();
    }

    //Méthode
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.RED);
        g.setFont(font5);
        g.drawString("🔥ⒸⓄⓁⓉ-ⒺⓍⓅⓇⒺⓈⓈ🔥", 60, 60);
        g.setFont(font1);
        g.drawString("By Ahmed & Jessica", 90,90);
        int w = largeurFenetre/60, h = hauteurFenetre/40; // Unités pour décrire les composants de cette vue.
        // Pour le setFont, on tient compte de la hauteur et de la largeur de l'unité, pour
        // prendre en compte les changements de taille horizontale et de taille verticale...
        Font font3 = new Font(Font.MONOSPACED, Font.ITALIC, Math.min(3*h/2, w));
        Insets in1 = new Insets(0, 0, 0, 0); // Marges entre les bords du bouton et le texte à l'intérieur.

        //Tableau des bandits
        Bandit.tableauBandits.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Bandit.scrollpaneBandits.setBounds(40*w, h, 20*w, 16*h);
        int largeurTotale = 20*w;
        Bandit.tableauBandits.setFont(font3); Bandit.tableauBandits.getTableHeader().setFont(font3);
        Bandit.tableauBandits.getColumnModel().getColumn(0).setPreferredWidth(largeurTotale/3);
        Bandit.tableauBandits.getColumnModel().getColumn(1).setPreferredWidth(largeurTotale/3);
        Bandit.tableauBandits.getColumnModel().getColumn(2).setPreferredWidth(largeurTotale/3);
        int rowHeight1 = 3*font3.getSize()/2;
        Bandit.tableauBandits.setRowHeight(rowHeight1);
        int hauteurTable = NB_JOUEURS*rowHeight1;
        Bandit.tableauBandits.setSize(largeurTotale, hauteurTable);
        int hauteurEnTete = Bandit.tableauBandits.getTableHeader().getHeight();
        Bandit.scrollpaneBandits.setSize(largeurTotale, hauteurEnTete + hauteurTable);
        fixedSize = Bandit.tableauBandits.getSize();
        viewport.setOpaque(false);
        viewport.setView(Bandit.tableauBandits);
        viewport.setPreferredSize(fixedSize);
        viewport.setMaximumSize(fixedSize);
        Bandit.scrollpaneBandits.setViewport(viewport);
        Bandit.scrollpaneBandits.setFont(font3);

        // Texte et bouton de basculement planification/action
        configurerLabel(textePlanificationAction, 40*w, 0, 12*w, 30*h, font3);
        configurerBouton(boutonPlanification, 40*w, 16*h, 12*w, 4*h, in1, font3);
        configurerBouton(boutonAction, 40*w, 21*h, 8*w, 4*h, in1, font3);
        configurerBouton(boutonNouvellePartie, 40*w, 26*h, 12*w, 4*h, in1, font3);

        // Les textes utilisés pour la planification
        configurerLabel(texteLigneChoixDuJoueurPlanificateur, w, 17*h, 16*w, 2*h, font3);
        configurerLabel(texteLigneBoutonsPlanificationDeplacement, w, 21*h, 16*w, 2*h, font3);
        configurerLabel(texteLigneBoutonsPlanificationTir, w, 25*h, 16*w, 2*h, font3);
        configurerLabel(textePlanificationPrendre, w, 29*h, 16*w, 2*h, font3);
        configurerLabel(textePlanificationNeRienFaire, w, 33*h, 16*w, 2*h, font3);
        configurerLabel(texteLignePlanificationEnregistree, w, 37*h, 16*w, 2*h, font3);
        configurerLabel(textePlanificationEnregistree, 16*w, 37*h, 16*w, 2*h, font3);

        // Les boutons utilisés pour la planification
        for (int i=0; i<NB_JOUEURS; i++) {
            configurerBouton(boutonsJoueur[i], 8*w+8*w+i*4*w, 16*h, 4*w, 4*h, in1, font3);
        }

        for (int i=0; i<NB_ACTIONS; i++) {
            configurerBouton(boutonsEnregistrerDeplacement[i], 8*w+8*w+i*4*w, 20*h, 4*w, 4*h, in1, font3);
            configurerBouton(boutonsEnregistrerTir[i], 8*w+8*w+i*4*w, 24*h, 4*w, 4*h, in1, font3);
        }
        configurerBouton(boutonsEnregistrerPrendreButin, 16*w, 28*h, 4*w, 4*h, in1, font3);
        configurerBouton(boutonsEnregistrerNeRienFaire, 16*w, 32*h, 4*w, 4*h, in1, font3);
        configurerBouton(boutonCorrectionPlanification, 24*w, 36*h, 4*w, 4*h, in1, font3);
    }

    void configurerBouton(JButton composant, int x, int y, int largeur, int hauteur, Insets in1, Font font4) {
        composant.setBounds(x, y, largeur, hauteur);
        composant.setMargin(in1); composant.setFont(font4);
    }

    void configurerLabel(JLabel composant, int x, int y, int largeur, int hauteur, Font font4) {
        composant.setBounds(x, y, largeur, hauteur); composant.setFont(font4);
    }

    /**
     * Définition de l'action.
     */
    static class Action {
        int joueur;
        char type; // 'D' pour déplacement, 'T' pour tir, 'P' pour prendre un butin, 'X' pour ne rien faire.
        int direction;

        Action(int joueur1, char type1, Direction direction1) {
            joueur = joueur1; type = type1; direction = direction1.ordinal();
        }

        public String texte() {
            String string1 = String.valueOf(type);
            if (type == 'X' || type == 'P') return string1 + ' '; // On ne met pas de symbole de direction si on choisit l'action "Prendre" ou l'action "!!!!!!!!ne rien faire".
            return string1 + symbolesDeDirection[direction];
        }
    }
    /**
     * Le contrôleur : fait l'interface utilisateur et assure la logique du jeu.
     *
     */
    public class Controleur implements ActionListener{

        /**
         * Constructeur.
         */
        Controleur() {

        }

        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==boutonNouvellePartie) {
                initialiserUnePartie();
                VueGenerale.vueGenerale.repaint();
                return;
            }

            if(e.getSource()==boutonPlanification) {
                phase = (phase == Phase.PLANIFICATION ? Phase.ACTION : Phase.PLANIFICATION);
                textePlanificationAction.setText(phase == Phase.PLANIFICATION ? "Phase planification" : "Phase action");
                textePlanificationAction.repaint();
                return;
            }
            if (phase == Phase.PLANIFICATION) {
                if (e.getSource()==boutonCorrectionPlanification) {
                    if (positionCurseurActionsEnregistrees>=0) {
                        actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][positionCurseurActionsEnregistrees].type = 'V'; // Pour "Vide".
                        positionCurseurActionsEnregistrees--;
                        String texte2 = "";
                        for (int i=0; i<=positionCurseurActionsEnregistrees; i++) {
                            texte2 += actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][i].texte() + "  ";
                        }
                        textePlanificationEnregistree.setText(texte2);
                        textePlanificationEnregistree.repaint();
                    }
                    return;
                }
                else {
                    for (int i=0; i<NB_ACTIONS; i++) {
                        if (e.getSource()==boutonsEnregistrerDeplacement[i]) {
                            if (positionCurseurActionsEnregistrees<NB_ACTIONS-1) {
                                positionCurseurActionsEnregistrees++;
                                actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][positionCurseurActionsEnregistrees].type = 'D';
                                actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][positionCurseurActionsEnregistrees].direction = i;
                                String texte2 = "";
                                for (int j=0; j<=positionCurseurActionsEnregistrees; j++) {
                                    texte2 += actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][j].texte() + "  ";
                                }
                                textePlanificationEnregistree.setText(texte2);
                                textePlanificationEnregistree.repaint();
                            }
                            return;
                        }
                        else if (e.getSource()==boutonsEnregistrerTir[i]) {
                            if (positionCurseurActionsEnregistrees<NB_ACTIONS-1) {
                                positionCurseurActionsEnregistrees++;
                                actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][positionCurseurActionsEnregistrees].type = 'T';
                                actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][positionCurseurActionsEnregistrees].direction = i;
                                String texte2 = "";
                                for (int j=0; j<=positionCurseurActionsEnregistrees; j++) {
                                    texte2 += actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][j].texte() + "  ";
                                }
                                textePlanificationEnregistree.setText(texte2);
                                textePlanificationEnregistree.repaint();
                            }
                            return;
                        }
                    }

                    for (int i=0; i<NB_JOUEURS; i++) {
                        if (e.getSource()==boutonsJoueur[i]) {
                            joueurEnCoursDePlanification = i;
                            positionCurseurActionsEnregistrees = -1;
                            textePlanificationEnregistree.setText("");
                            textePlanificationEnregistree.repaint();
                            for (int k=0; k<NB_JOUEURS; k++) {
                                boutonsJoueur[k].setBackground(k == i ? Color.blue : Color.gray);
                                boutonsJoueur[k].repaint();
                            }
                            return;
                        }
                    }

                    if (e.getSource()==boutonsEnregistrerPrendreButin) {
                        if (positionCurseurActionsEnregistrees<NB_ACTIONS-1) {
                            positionCurseurActionsEnregistrees++;
                            actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][positionCurseurActionsEnregistrees].type = 'P';
                            actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][positionCurseurActionsEnregistrees].direction = 0;
                            String texte2 = "";
                            for (int j=0; j<=positionCurseurActionsEnregistrees; j++) {
                                texte2 += actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][j].texte() + "  ";
                            }
                            textePlanificationEnregistree.setText(texte2);
                            textePlanificationEnregistree.repaint();
                        }
                        return;
                    }
                    if (e.getSource()==boutonsEnregistrerNeRienFaire) {
                        if (positionCurseurActionsEnregistrees<NB_ACTIONS-1) {
                            positionCurseurActionsEnregistrees++;
                            actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][positionCurseurActionsEnregistrees].type = 'X';
                            actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][positionCurseurActionsEnregistrees].direction = 0;
                            String texte2 = "";
                            for (int j=0; j<=positionCurseurActionsEnregistrees; j++) {
                                texte2 += actionJoueurPlanificationEnregistree[joueurEnCoursDePlanification][j].texte() + "  ";
                            }
                            textePlanificationEnregistree.setText(texte2);
                            textePlanificationEnregistree.repaint();
                        }
                        return;
                    }
                }
            }
            else { // Cas de la phase "Action"...
                if(e.getSource()==boutonAction){ // On a appuyé sur le bouton "Action !".
                    // JOptionPane.showMessageDialog(new Frame(), null, "Vu !", JOptionPane.WARNING_MESSAGE); // ### Pour debug.
                    Direction[] dirConvert = new Direction[4];
                    dirConvert[0] = Direction.AVANT;	dirConvert[1] = Direction.ARRIERE; dirConvert[2] = Direction.HAUT; dirConvert[3] = Direction.BAS;

                    // On réinitialise tout ce qui concerne l'affichage de la planification
                    joueurEnCoursDePlanification = 0;
                    positionCurseurActionsEnregistrees = -1;
                    textePlanificationEnregistree.setText("");
                    textePlanificationEnregistree.repaint();

                    // Traitement des actions
                    VueGenerale.marshall.seDeplace();
                    for (int i = 0; i<NB_ACTIONS; i++) {
                        for (int j = 0; j<NB_JOUEURS; j++) {
                            // Extraction des infos nécessaires à la gestion de l'action enregistrée par le joueur...
                            if (actionJoueurPlanificationEnregistree[j][i].type == 'D') {
                                int dir1 = actionJoueurPlanificationEnregistree[j][i].direction;
                                Bandit.bandits[j].seDeplace(dirConvert[dir1]);
                                // Examen des situations produites suite au déplacement...
                                // Cas où le bandit et le marshall sont dans le même wagon, en bas...
                                if (Bandit.bandits[j].getNumeroWagon() == VueGenerale.marshall.getNumeroWagon() && Bandit.bandits[0].getPositionDansWagon() == 0) {
                                    //To Do  Afficher un texte dans la console pour dire que le bandit a rencontré le marshall.
                                    Bandit.bandits[j].estConfronteAuMarshall();
                                    Bandit.bandits[j].lacheUnButin(); Bandit.bandits[j].seDeplace(Direction.HAUT);
                                }
                                // Cas où le bandit arrive en bas d'un wagon, sans que le marshall y soit : il ne
                                // se passe rien, on attend que le bandit choisisse l'action "Récupérer un butin".

                            }
                            else if (actionJoueurPlanificationEnregistree[j][i].type == 'T') {
                                int dir1 = actionJoueurPlanificationEnregistree[j][i].direction;
                                Bandit.bandits[j].tire(dirConvert[dir1]);
                            }
                            else if (actionJoueurPlanificationEnregistree[j][i].type == 'P') {
                                Bandit.bandits[j].recupereButin(Bandit.bandits[j].getNumeroWagon());
                            }
                            else { // Cas où le joueur veut ne rien faire.
                                Bandit.bandits[j].neFaitRien();
                            }
                            VueGenerale.vueDuTrain.paintImmediately(0, 0, VueGenerale.vueDuTrain.largeurCadreDessin, VueGenerale.vueDuTrain.hauteurCadreDessin);
                            Bandit.scrollpaneBandits.paintImmediately(0, 0, Bandit.scrollpaneBandits.getBounds().width, Bandit.scrollpaneBandits.getBounds().height);
                            long tempsDebutAttente = System.currentTimeMillis();
                            long dureeAttente = 500;
                            while(System.currentTimeMillis() < tempsDebutAttente+dureeAttente){
                            }
                        }
                    }
                }
                // On prépare la prochaine étape : on sera en planification.
                phase = Phase.PLANIFICATION;
                textePlanificationAction.setText(phase == Phase.PLANIFICATION ? "Phase planification" : "Phase action");
            }
        }
    }
}

