package ma.projet;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class EvaluationTest {

    private static SimpleDateFormat sdf;

    @BeforeClass
    public static void setUp() {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @AfterClass
    public static void tearDown() {
        HibernateUtil.shutdown();
    }

    @Test
    public void testExercice1Stock() throws Exception {
        CategorieService cs = new CategorieService();
        ProduitService ps = new ProduitService();
        CommandeService cmdService = new CommandeService();
        LigneCommandeService lcs = new LigneCommandeService();

        Categorie cat = new Categorie("TEST_CAT", "Test Categorie");
        assertTrue(cs.create(cat));

        Produit p1 = new Produit("REF_1", 150.0f, cat);
        Produit p2 = new Produit("REF_2", 80.0f, cat);
        assertTrue(ps.create(p1));
        assertTrue(ps.create(p2));

        Commande cmd = new Commande(sdf.parse("2024-01-15"));
        assertTrue(cmdService.create(cmd));

        LigneCommandeProduit l1 = new LigneCommandeProduit(5, p1, cmd);
        assertTrue(lcs.create(l1));

        List<Produit> parCat = ps.getProduitsParCategorie(cat);
        assertFalse(parCat.isEmpty());

        List<Produit> sup100 = ps.getProduitsPrixSuperieur100();
        assertFalse(sup100.isEmpty());
    }

    @Test
    public void testExercice2Projets() throws Exception {
        EmployeService es = new EmployeService();
        ProjetService ps = new ProjetService();
        TacheService ts = new TacheService();
        EmployeTacheService ets = new EmployeTacheService();

        Employe emp = new Employe("TEST_NOM", "Test_Prenom", "0600000000");
        assertTrue(es.create(emp));

        Projet proj = new Projet("Projet Test", sdf.parse("2024-01-01"), sdf.parse("2024-12-31"), emp);
        assertTrue(ps.create(proj));

        Tache t = new Tache("Tache 1", sdf.parse("2024-02-01"), sdf.parse("2024-02-15"), 1500.0, proj);
        assertTrue(ts.create(t));

        EmployeTache et = new EmployeTache(sdf.parse("2024-02-02"), sdf.parse("2024-02-14"), emp, t);
        assertTrue(ets.create(et));

        List<Tache> tachesSup1000 = ts.getTachesPrixSuperieur1000();
        assertFalse(tachesSup1000.isEmpty());
    }

    @Test
    public void testExercice3EtatCivil() throws Exception {
        HommeService hs = new HommeService();
        FemmeService fs = new FemmeService();
        MariageService ms = new MariageService();

        Homme h = new Homme("HOMME_TEST", "Ali", "0611111111", "Rabat", sdf.parse("1970-01-01"));
        assertTrue(hs.create(h));

        Femme f1 = new Femme("FEMME_TEST1", "Fatima", "0622222222", "Rabat", sdf.parse("1975-01-01"));
        Femme f2 = new Femme("FEMME_TEST2", "Zineb", "0633333333", "Rabat", sdf.parse("1980-01-01"));
        Femme f3 = new Femme("FEMME_TEST3", "Amina", "0644444444", "Rabat", sdf.parse("1985-01-01"));
        Femme f4 = new Femme("FEMME_TEST4", "Samia", "0655555555", "Rabat", sdf.parse("1990-01-01"));

        assertTrue(fs.create(f1));
        assertTrue(fs.create(f2));
        assertTrue(fs.create(f3));
        assertTrue(fs.create(f4));

        assertTrue(ms.create(new Mariage(sdf.parse("1995-01-01"), null, 2, h, f1)));
        assertTrue(ms.create(new Mariage(sdf.parse("1998-01-01"), null, 1, h, f2)));
        assertTrue(ms.create(new Mariage(sdf.parse("2001-01-01"), null, 3, h, f3)));
        assertTrue(ms.create(new Mariage(sdf.parse("2004-01-01"), null, 0, h, f4)));

        Femme oldest = fs.getPlusAgee();
        assertNotNull(oldest);

        long count4 = ms.countHommesMariesQuatreFemmes(sdf.parse("1990-01-01"), sdf.parse("2010-01-01"));
        assertTrue(count4 >= 1);
    }
}
