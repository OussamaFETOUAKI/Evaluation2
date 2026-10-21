package ma.projet.test;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestEx2 {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("            EXERCICE 2 : APPLICATION DE GESTION DE PROJETS                      ");
        System.out.println("================================================================================\n");

        EmployeService employeService = new EmployeService();
        ProjetService projetService = new ProjetService();
        TacheService tacheService = new TacheService();
        EmployeTacheService employeTacheService = new EmployeTacheService();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 1. Création des employés
            Employe emp1 = new Employe("ALAMI", "Ahmed", "0611223344");
            Employe emp2 = new Employe("BENNANI", "Karim", "0622334455");
            Employe emp3 = new Employe("IDRISSI", "Fatima", "0633445566");

            employeService.create(emp1);
            employeService.create(emp2);
            employeService.create(emp3);

            // 2. Création des projets (emp1 chef de projet)
            Date dDebProjet = sdf.parse("2013-01-14");
            Date dFinProjet = sdf.parse("2013-12-31");
            Projet p1 = new Projet("Gestion de stock", dDebProjet, dFinProjet, emp1);
            Projet p2 = new Projet("Portail RH", sdf.parse("2013-03-01"), sdf.parse("2013-09-30"), emp2);

            projetService.create(p1);
            projetService.create(p2);

            // 3. Création des tâches planifiées
            Tache t1 = new Tache("Analyse", sdf.parse("2013-02-01"), sdf.parse("2013-02-28"), 1200.0, p1);
            Tache t2 = new Tache("Conception", sdf.parse("2013-03-01"), sdf.parse("2013-03-31"), 1500.0, p1);
            Tache t3 = new Tache("Développement", sdf.parse("2013-04-01"), sdf.parse("2013-05-31"), 2800.0, p1);
            Tache t4 = new Tache("Documentation", sdf.parse("2013-06-01"), sdf.parse("2013-06-15"), 600.0, p1);

            tacheService.create(t1);
            tacheService.create(t2);
            tacheService.create(t3);
            tacheService.create(t4);

            // 4. Création des assignations réelles
            EmployeTache et1 = new EmployeTache(sdf.parse("2013-02-10"), sdf.parse("2013-02-20"), emp2, t1);
            EmployeTache et2 = new EmployeTache(sdf.parse("2013-03-10"), sdf.parse("2013-03-15"), emp2, t2);
            EmployeTache et3 = new EmployeTache(sdf.parse("2013-04-10"), sdf.parse("2013-04-25"), emp3, t3);

            employeTacheService.create(et1);
            employeTacheService.create(et2);
            employeTacheService.create(et3);

            System.out.println(">>> 1. Liste des projets gérés par l'employé Ahmed ALAMI :");
            employeService.afficherProjetsGeres(emp1);

            System.out.println("\n>>> 2. Liste des tâches réalisées par Karim BENNANI :");
            employeService.afficherTachesRealisees(emp2);

            System.out.println("\n>>> 3. Liste des tâches planifiées pour le projet 'Gestion de stock' :");
            projetService.afficherTachesPlanifiees(p1);

            System.out.println("\n>>> 4. Liste des tâches réalisées avec les dates réelles (Exemple attendu) :");
            projetService.afficherTachesRealisees(p1);

            System.out.println("\n>>> 5. Tâches dont le prix est supérieur à 1000 DH (Requête nommée) :");
            tacheService.afficherTachesPrixSuperieur1000();

            System.out.println("\n>>> 6. Tâches réalisées entre deux dates (01/02/2013 et 31/03/2013) :");
            tacheService.afficherTachesRealiseesEntreDates(sdf.parse("2013-02-01"), sdf.parse("2013-03-31"));

            System.out.println("\n[SUCCESS] Exercice 2 exécuté et validé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
