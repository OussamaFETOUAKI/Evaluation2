package ma.projet.test;

import ma.projet.util.HibernateUtil;

public class App {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("              ÉVALUATION PRATIQUE - HIBERNATE & JPA EN COUCHES                   ");
        System.out.println("                  ENSA / TOUBKALIT - ARCHITECTURE DAO / SERVICE                 ");
        System.out.println("================================================================================\n");

        try {
            // Lancement séquentiel des trois exercices
            System.out.println(">>> DÉMARRAGE DE L'EXERCICE 1...");
            TestEx1.main(new String[]{});

            System.out.println("\n\n>>> DÉMARRAGE DE L'EXERCICE 2...");
            TestEx2.main(new String[]{});

            System.out.println("\n\n>>> DÉMARRAGE DE L'EXERCICE 3...");
            TestEx3.main(new String[]{});

            System.out.println("\n================================================================================");
            System.out.println("   FÉLICITATIONS ! LES 3 EXERCICES DE L'ÉVALUATION ONT ÉTÉ VALIDÉS AVEC SUCCÈS  ");
            System.out.println("================================================================================");
        } catch (Exception e) {
            System.err.println("Une erreur s'est produite lors de l'exécution de l'application : " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
