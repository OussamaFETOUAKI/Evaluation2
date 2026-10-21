package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TacheService implements IDao<Tache> {

    @Override
    public boolean create(Tache o) {
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
    public boolean update(Tache o) {
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
    public boolean delete(Tache o) {
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
    public Tache getById(int id) {
        Session session = HibernateUtil.openSession();
        try {
            return session.get(Tache.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Tache> getAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Tache", Tache.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Utilise une requête nommée pour retourner les tâches dont le prix est supérieur à 1000 DH.
     */
    public List<Tache> getTachesPrixSuperieur1000() {
        Session session = HibernateUtil.openSession();
        try {
            Query<Tache> query = session.createNamedQuery("Tache.findByPrixSuperieur1000", Tache.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Affiche les tâches dont le prix est supérieur à 1000 DH.
     */
    public void afficherTachesPrixSuperieur1000() {
        List<Tache> taches = getTachesPrixSuperieur1000();
        System.out.println("Tâches dont le prix est supérieur à 1000 DH (Requête nommée) :");
        System.out.printf("%-6s %-20s %-12s %-25s%n", "Num", "Nom", "Prix", "Projet");
        for (Tache t : taches) {
            String pNom = (t.getProjet() != null) ? t.getProjet().getNom() : "-";
            System.out.printf("%-6d %-20s %-12.2f %-25s%n", t.getId(), t.getNom(), t.getPrix(), pNom);
        }
    }

    /**
     * Retourne les tâches réalisées entre deux dates.
     */
    public List<EmployeTache> getTachesRealiseesEntreDates(Date d1, Date d2) {
        Session session = HibernateUtil.openSession();
        try {
            Query<EmployeTache> query = session.createQuery(
                "FROM EmployeTache et WHERE et.dateDebutReelle >= :d1 AND et.dateFinReelle <= :d2",
                EmployeTache.class
            );
            query.setParameter("d1", d1);
            query.setParameter("d2", d2);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Affiche les tâches réalisées entre deux dates.
     */
    public void afficherTachesRealiseesEntreDates(Date d1, Date d2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<EmployeTache> list = getTachesRealiseesEntreDates(d1, d2);
        System.out.println("Tâches réalisées entre " + sdf.format(d1) + " et " + sdf.format(d2) + " :");
        System.out.printf("%-6s %-20s %-18s %-18s %-20s%n", "Num", "Nom Tâche", "Date Début Réelle", "Date Fin Réelle", "Employé");
        for (EmployeTache et : list) {
            String emp = (et.getEmploye() != null) ? (et.getEmploye().getPrenom() + " " + et.getEmploye().getNom()) : "-";
            String tNom = (et.getTache() != null) ? et.getTache().getNom() : "-";
            int tId = (et.getTache() != null) ? et.getTache().getId() : 0;
            System.out.printf("%-6d %-20s %-18s %-18s %-20s%n",
                tId,
                tNom,
                sdf.format(et.getDateDebutReelle()),
                sdf.format(et.getDateFinReelle()),
                emp
            );
        }
    }
}
