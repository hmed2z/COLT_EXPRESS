//package coltExpressAhmedJessica;

//Bibliothèques et classes utilisées
import java.util.Random;
//import coltExpressAhmedJessica.Bandit;
//import coltExpressAhmedJessica.Butin;
//import coltExpressAhmedJessica.Train;
//import coltExpressAhmedJessica.VueControleur;
//import coltExpressAhmedJessica.VueDuTrain;
//import coltExpressAhmedJessica.VueGenerale;
//import coltExpressAhmedJessica.Wagon;

public class Marshall {
    private int numeroWagon; // Le num�ro du wagon o� se trouve le marshall (jamais sur le toit).
    private static Random randMarshall = new Random();

    Marshall(int numeroWagon2) {
        numeroWagon = numeroWagon2;
    }

    public int getNumeroWagon() {
        return numeroWagon;
    }

    public void setNumeroWagon(int numeroWagon) {
        this.numeroWagon = numeroWagon;
    }

    public void seDeplace() {
        String message1; VueControleur.Direction direction;
        float testDeplacement = randMarshall.nextFloat();
        // Si le marshall a envie de bouger et qu'il est dans le dernier wagon, sa direction sera vers l'avant...

        if (numeroWagon == 0) {
            direction = VueControleur.Direction.ARRIERE;
        }
        // Si le marshall a envie de bouger et qu'il est dans la loco, sa direction sera vers l'arri�re...
        else if (numeroWagon == Wagon.NB_WAGONS-1) {
            direction = VueControleur.Direction.AVANT;
        }
        else { // Sinon, sa direction est al�atoire entre l'a�vant et l'arri�re...
            boolean direction2 = randMarshall.nextBoolean(); // Si true : vers l'avant, sinon, vers l'arri�re.
            if (direction2 == true) {
                direction = VueControleur.Direction.AVANT;
            }
            else direction = VueControleur.Direction.ARRIERE;
        }
        if (direction == VueControleur.Direction.AVANT) {
            if (numeroWagon > 0) {
                numeroWagon--;
                if (numeroWagon !=0) message1 = "Le marshall va vers l'avant du train, arrive dans le wagon " + numeroWagon + ".";
                else message1 = "Le marshall va vers l'avant du train, arrive � la locomotive.";

                System.out.println(message1);
            }
        }
        else {
            if (numeroWagon < Wagon.NB_WAGONS-1) {
                numeroWagon++; message1 = "Le marshall va vers l'arri�re du train, arrive dans le wagon " + numeroWagon + ".";
                System.out.println(message1);
            }
        }

    }


}

