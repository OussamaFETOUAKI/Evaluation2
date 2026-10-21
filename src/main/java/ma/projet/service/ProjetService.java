package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ProjetService implements IDao<Projet> {

    @Override
    public boolean create(Projet o) {
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
    public boolean update(Projet o) {
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
    public boolean delete(Projet o) {
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
    public Projet getById(int id) {
        Session session = HibernateUtil.openSession();
        try {
            return session.get(Projet.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Projet> getAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Projet", Projet.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Affiche la liste des tâches planifiées pour un projet.
     */
    public void afficherTachesPlanifiees(Projet p) {
        if (p == null) {
            System.out.println("Projet inexistant.");
            return;
        }
        Session session = HibernateUtil.openSession();
        try {
            Query<Tache> query = session.createQuery(
                "FROM Tache t WHERE t.projet = :projet",
                Tache.class
            );
            query.setParameter("projet", p);
            List<Tache> taches = query.list();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Tâches planifiées pour le projet : " + p.getNom());
            System.out.printf("%-6s %-20s %-15s %-15s %-10s%n", "Num", "Nom", "Date Début", "Date Fin", "Prix");
            for (Tache t : taches) {
                System.out.printf("%-6d %-20s %-15s %-15s %-10.2f%n",
                    t.getId(),
                    t.getNom(),
                    sdf.format(t.getDateDebut()),
                    sdf.format(t.getDateFin()),
                    t.getPrix()
                );
            }
        } finally {
            session.close();
        }
    }

    /**
     * Affiche la liste des tâches réalisées avec les dates réelles selon le format attendu.
     */
    public void afficherTachesRealisees(Projet p) {
        if (p == null) {
            System.out.println("Projet inexistant.");
            return;
        }
        Session session = HibernateUtil.openSession();
        try {
            Query<EmployeTache> query = session.createQuery(
                "SELECT et FROM EmployeTache et JOIN et.tache t WHERE t.projet = :projet",
                EmployeTache.class
            );
            query.setParameter("projet", p);
            List<EmployeTache> list = query.list();

            SimpleDateFormat sdfHeader = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");

            String debutStr = (p.getDateDebut() != null) ? sdfHeader.format(p.getDateDebut()) : "";

            System.out.println("Projet : " + p.getId() + "      Nom : " + p.getNom() + "     Date début : " + debutStr);
            System.out.println("Liste des tâches:");
            System.out.printf("%-4s %-15s %-20s %-18s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
            for (EmployeTache et : list) {
                if (et.getTache() != null) {
                    System.out.printf("%-4d %-15s %-20s %-18s%n",
                        et.getTache().getId(),
                        et.getTache().getNom(),
                        sdfDate.format(et.getDateDebutReelle()),
                        sdfDate.format(et.getDateFinReelle())
                    );
                }
            }
        } finally {
            session.close();
        }
    }
}
