package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProduitService implements IDao<Produit> {

    @Override
    public boolean create(Produit o) {
        Session session = HibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Produit o) {
        Session session = HibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Produit o) {
        Session session = HibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Produit getById(int id) {
        Session session = HibernateUtil.openSession();
        try {
            return session.get(Produit.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Produit> getAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Produit", Produit.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Retourne la liste des produits appartenant à une catégorie donnée.
     */
    public List<Produit> getProduitsParCategorie(Categorie c) {
        Session session = HibernateUtil.openSession();
        try {
            Query<Produit> query = session.createQuery(
                "FROM Produit p WHERE p.categorie = :categorie", Produit.class
            );
            query.setParameter("categorie", c);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Affiche la liste des produits appartenant à une catégorie.
     */
    public void afficherProduitsParCategorie(Categorie c) {
        List<Produit> produits = getProduitsParCategorie(c);
        System.out.println("Catégorie : " + (c != null ? c.getLibelle() : "Toutes"));
        System.out.printf("%-12s %-12s%n", "Référence", "Prix");
        for (Produit p : produits) {
            System.out.printf("%-12s %-12s%n", p.getReference(), (int) p.getPrix() + " DH");
        }
    }

    /**
     * Retourne la liste des produits commandés entre deux dates.
     */
    public List<Produit> getProduitsCommandesEntreDates(Date dateDebut, Date dateFin) {
        Session session = HibernateUtil.openSession();
        try {
            Query<Produit> query = session.createQuery(
                "SELECT DISTINCT lcp.produit FROM LigneCommandeProduit lcp " +
                "WHERE lcp.commande.date BETWEEN :dateDebut AND :dateFin",
                Produit.class
            );
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Affiche les produits commandés entre deux dates.
     */
    public void afficherProduitsCommandesEntreDates(Date dateDebut, Date dateFin) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
        List<Produit> produits = getProduitsCommandesEntreDates(dateDebut, dateFin);
        System.out.println("Produits commandés entre " + sdf.format(dateDebut) + " et " + sdf.format(dateFin) + " :");
        System.out.printf("%-12s %-12s%n", "Référence", "Prix");
        for (Produit p : produits) {
            System.out.printf("%-12s %-12s%n", p.getReference(), (int) p.getPrix() + " DH");
        }
    }

    /**
     * Affiche les produits commandés pour une commande donnée.
     * Respecte le format exact attendu par l'énoncé.
     */
    public void afficherProduitsParCommande(Commande commande) {
        if (commande == null) {
            System.out.println("Commande inexistante.");
            return;
        }

        Session session = HibernateUtil.openSession();
        try {
            Query<LigneCommandeProduit> query = session.createQuery(
                "FROM LigneCommandeProduit lcp WHERE lcp.commande = :commande",
                LigneCommandeProduit.class
            );
            query.setParameter("commande", commande);
            List<LigneCommandeProduit> lignes = query.list();

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            String dateFormatted = (commande.getDate() != null) ? sdf.format(commande.getDate()) : "";

            System.out.println("Commande : " + commande.getId() + "     Date : " + dateFormatted);
            System.out.println("Liste des produits :");
            System.out.printf("%-12s %-8s %-10s%n", "Référence", "Prix", "Quantité");
            for (LigneCommandeProduit lcp : lignes) {
                if (lcp.getProduit() != null) {
                    System.out.printf("%-12s %-8s %-10d%n",
                        lcp.getProduit().getReference(),
                        (int) lcp.getProduit().getPrix() + " DH",
                        lcp.getQuantite()
                    );
                }
            }
        } finally {
            session.close();
        }
    }

    public void afficherProduitsParCommande(int commandeId) {
        CommandeService cs = new CommandeService();
        Commande cmd = cs.getById(commandeId);
        afficherProduitsParCommande(cmd);
    }

    /**
     * Utilise une requête nommée pour retourner les produits dont le prix est supérieur à 100 DH.
     */
    public List<Produit> getProduitsPrixSuperieur100() {
        Session session = HibernateUtil.openSession();
        try {
            Query<Produit> query = session.createNamedQuery("Produit.findByPrixSuperieur100", Produit.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Affiche les produits dont le prix est supérieur à 100 DH (requête nommée).
     */
    public void afficherProduitsPrixSuperieur100() {
        List<Produit> produits = getProduitsPrixSuperieur100();
        System.out.println("Liste des produits dont le prix est supérieur à 100 DH (Requête nommée) :");
        System.out.printf("%-12s %-12s %-20s%n", "Référence", "Prix", "Catégorie");
        for (Produit p : produits) {
            String cat = (p.getCategorie() != null) ? p.getCategorie().getLibelle() : "-";
            System.out.printf("%-12s %-12s %-20s%n", p.getReference(), (int) p.getPrix() + " DH", cat);
        }
    }
}
