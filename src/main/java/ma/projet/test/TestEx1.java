package ma.projet.test;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestEx1 {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("            EXERCICE 1 : GESTION DE STOCK DE PRODUITS INFORMATIQUES              ");
        System.out.println("================================================================================\n");

        CategorieService categorieService = new CategorieService();
        ProduitService produitService = new ProduitService();
        CommandeService commandeService = new CommandeService();
        LigneCommandeService ligneCommandeService = new LigneCommandeService();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 1. Création des catégories
            Categorie catInformatique = new Categorie("INFO", "Matériel Informatique");
            Categorie catBureautique = new Categorie("BUR", "Bureautique");
            categorieService.create(catInformatique);
            categorieService.create(catBureautique);

            // 2. Création des produits
            Produit p1 = new Produit("ES12", 120.0f, catInformatique);
            Produit p2 = new Produit("ZR85", 100.0f, catInformatique);
            Produit p3 = new Produit("EE85", 200.0f, catInformatique);
            Produit p4 = new Produit("CL20", 45.0f, catBureautique);
            Produit p5 = new Produit("SV90", 350.0f, catInformatique);

            produitService.create(p1);
            produitService.create(p2);
            produitService.create(p3);
            produitService.create(p4);
            produitService.create(p5);

            // 3. Création des commandes
            Date dCmd1 = sdf.parse("2013-03-14");
            Commande cmd1 = new Commande(dCmd1);
            commandeService.create(cmd1);

            Date dCmd2 = sdf.parse("2013-04-10");
            Commande cmd2 = new Commande(dCmd2);
            commandeService.create(cmd2);

            // 4. Création des lignes de commande
            LigneCommandeProduit lcp1 = new LigneCommandeProduit(7, p1, cmd1);
            LigneCommandeProduit lcp2 = new LigneCommandeProduit(14, p2, cmd1);
            LigneCommandeProduit lcp3 = new LigneCommandeProduit(5, p3, cmd1);
            LigneCommandeProduit lcp4 = new LigneCommandeProduit(3, p4, cmd2);
            LigneCommandeProduit lcp5 = new LigneCommandeProduit(2, p5, cmd2);

            ligneCommandeService.create(lcp1);
            ligneCommandeService.create(lcp2);
            ligneCommandeService.create(lcp3);
            ligneCommandeService.create(lcp4);
            ligneCommandeService.create(lcp5);

            System.out.println(">>> 1. Liste des produits par catégorie :");
            produitService.afficherProduitsParCategorie(catInformatique);

            System.out.println("\n>>> 2. Produits commandés entre deux dates (01/01/2013 et 31/03/2013) :");
            produitService.afficherProduitsCommandesEntreDates(sdf.parse("2013-01-01"), sdf.parse("2013-03-31"));

            System.out.println("\n>>> 3. Produits commandés dans une commande donnée (Exemple attendu) :");
            produitService.afficherProduitsParCommande(cmd1);

            System.out.println("\n>>> 4. Produits dont le prix est supérieur à 100 DH (Requête nommée) :");
            produitService.afficherProduitsPrixSuperieur100();

            System.out.println("\n[SUCCESS] Exercice 1 exécuté et validé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
