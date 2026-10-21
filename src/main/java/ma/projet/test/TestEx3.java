package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestEx3 {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("            EXERCICE 3 : GESTION DE L'ÉTAT CIVIL ET DES MARIAGES                ");
        System.out.println("================================================================================\n");

        HommeService hommeService = new HommeService();
        FemmeService femmeService = new FemmeService();
        MariageService mariageService = new MariageService();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM/yyyy");

            // 1. Création de 10 femmes et 5 hommes
            System.out.println(">>> 1. Création de 10 femmes et 5 hommes en base de données...");

            Homme h1 = new Homme("SAFI", "Said", "0661111111", "Casablanca", sdf.parse("1962-05-15"));
            Homme h2 = new Homme("CHRAIBI", "Omar", "0662222222", "Rabat", sdf.parse("1970-08-20"));
            Homme h3 = new Homme("TAHA", "Hassan", "0663333333", "Fès", sdf.parse("1975-11-03"));
            Homme h4 = new Homme("NACIRI", "Youssef", "0664444444", "Marrakech", sdf.parse("1980-01-25"));
            Homme h5 = new Homme("ELAMRANI", "Mehdi", "0665555555", "Tanger", sdf.parse("1985-07-14"));

            hommeService.create(h1);
            hommeService.create(h2);
            hommeService.create(h3);
            hommeService.create(h4);
            hommeService.create(h5);

            Femme f1 = new Femme("RAMI", "Salima", "0671111111", "Casablanca", sdf.parse("1965-02-10"));
            Femme f2 = new Femme("ALI", "Amal", "0672222222", "Casablanca", sdf.parse("1972-06-18"));
            Femme f3 = new Femme("ALAOUI", "Wafa", "0673333333", "Rabat", sdf.parse("1976-09-30"));
            Femme f4 = new Femme("ALAMI", "Karima", "0674444444", "Casablanca", sdf.parse("1968-12-05"));
            Femme f5 = new Femme("BENJELLOUN", "Latifa", "0675555555", "Fès", sdf.parse("1978-04-12"));
            Femme f6 = new Femme("TOUMI", "Samira", "0676666666", "Meknès", sdf.parse("1982-10-22"));
            Femme f7 = new Femme("BERADA", "Houda", "0677777777", "Marrakech", sdf.parse("1986-03-15"));
            Femme f8 = new Femme("KHALIL", "Meryem", "0678888888", "Tanger", sdf.parse("1988-08-08"));
            Femme f9 = new Femme("ZAHRA", "Fatima", "0679999999", "Agadir", sdf.parse("1990-11-20"));
            Femme f10 = new Femme("MANSOURI", "Zineb", "0670000000", "Oujda", sdf.parse("1993-01-05"));

            femmeService.create(f1);
            femmeService.create(f2);
            femmeService.create(f3);
            femmeService.create(f4);
            femmeService.create(f5);
            femmeService.create(f6);
            femmeService.create(f7);
            femmeService.create(f8);
            femmeService.create(f9);
            femmeService.create(f10);

            // Mariages pour SAFI Said (3 en cours, 1 échoué - Total 4 femmes entre 1989 et 2005)
            Mariage m1 = new Mariage(sdf.parse("1990-09-03"), null, 4, h1, f1); // Salima RAMI, 4 enfants
            Mariage m2 = new Mariage(sdf.parse("1995-09-03"), null, 2, h1, f2); // Amal ALI, 2 enfants
            Mariage m3 = new Mariage(sdf.parse("2000-11-04"), null, 3, h1, f3); // Wafa ALAOUI, 3 enfants
            Mariage m4 = new Mariage(sdf.parse("1989-09-03"), sdf.parse("1990-09-03"), 0, h1, f4); // Karima ALAMI, divorce

            // Mariage additionnel pour Karima ALAMI afin qu'elle soit mariée deux fois
            Mariage m5 = new Mariage(sdf.parse("1992-06-15"), null, 1, h2, f4); // Karima remariée avec Omar CHRAIBI

            // Autres mariages
            Mariage m6 = new Mariage(sdf.parse("2005-04-10"), null, 2, h3, f5);
            Mariage m7 = new Mariage(sdf.parse("2010-07-20"), null, 3, h4, f6);

            mariageService.create(m1);
            mariageService.create(m2);
            mariageService.create(m3);
            mariageService.create(m4);
            mariageService.create(m5);
            mariageService.create(m6);
            mariageService.create(m7);

            // 2. Afficher la liste des femmes
            System.out.println("\n>>> 2. Liste de toutes les femmes :");
            List<Femme> toutesFemmes = femmeService.getAll();
            System.out.printf("%-4s %-15s %-15s %-15s %-15s%n", "ID", "Nom", "Prénom", "Téléphone", "Date Naissance");
            for (Femme f : toutesFemmes) {
                System.out.printf("%-4d %-15s %-15s %-15s %-15s%n",
                    f.getId(), f.getNom(), f.getPrenom(), f.getTelephone(), sdfDisplay.format(f.getDateNaissance()));
            }

            // 3. Afficher la femme la plus âgée
            System.out.println("\n>>> 3. Affichage de la femme la plus âgée :");
            femmeService.afficherPlusAgee();

            // 4. Afficher les épouses d'un homme donné entre deux dates
            System.out.println("\n>>> 4. Afficher les épouses de Said SAFI entre 1988 et 1996 :");
            hommeService.afficherEpousesEntreDates(h1, sdf.parse("1988-01-01"), sdf.parse("1996-12-31"));

            // 5. Afficher le nombre d'enfants d'une femme entre deux dates
            System.out.println("\n>>> 5. Nombre d'enfants de Salima RAMI entre 1990 et 2000 (Requête native nommée) :");
            femmeService.afficherNombreEnfants(f1, sdf.parse("1990-01-01"), sdf.parse("2000-12-31"));

            // 6. Afficher les femmes mariées deux fois ou plus
            System.out.println("\n>>> 6. Femmes mariées deux fois ou plus (Requête nommée) :");
            femmeService.afficherFemmesMarieesAuMoinsDeuxFois();

            // 7. Afficher les hommes mariés à quatre femmes entre deux dates (API Criteria)
            System.out.println("\n>>> 7. Nombre d'hommes mariés à quatre femmes entre 1985 et 2010 (API Criteria) :");
            mariageService.afficherHommesMariesQuatreFemmes(sdf.parse("1985-01-01"), sdf.parse("2010-12-31"));

            // 8. Afficher les mariages d'un homme avec tous les détails
            System.out.println("\n>>> 8. Détails complets des mariages de Said SAFI (Format attendu) :");
            hommeService.afficherMariagesDetails(h1);

            System.out.println("\n[SUCCESS] Exercice 3 exécuté et validé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
