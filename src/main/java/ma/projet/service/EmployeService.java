package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class EmployeService implements IDao<Employe> {

    @Override
    public boolean create(Employe o) {
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
    public boolean update(Employe o) {
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
    public boolean delete(Employe o) {
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
    public Employe getById(int id) {
        Session session = HibernateUtil.openSession();
        try {
            return session.get(Employe.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Employe> getAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Employe", Employe.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Affiche la liste des tâches réalisées par un employé donné.
     */
    public void afficherTachesRealisees(Employe e) {
        if (e == null) {
            System.out.println("Employé inexistant.");
            return;
        }
        Session session = HibernateUtil.openSession();
        try {
            Query<EmployeTache> query = session.createQuery(
                "FROM EmployeTache et WHERE et.employe = :employe",
                EmployeTache.class
            );
            query.setParameter("employe", e);
            List<EmployeTache> list = query.list();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Tâches réalisées par l'employé : " + e.getPrenom() + " " + e.getNom());
            System.out.printf("%-6s %-20s %-18s %-18s%n", "Num", "Nom Tâche", "Date Début Réelle", "Date Fin Réelle");
            for (EmployeTache et : list) {
                if (et.getTache() != null) {
                    System.out.printf("%-6d %-20s %-18s %-18s%n",
                        et.getTache().getId(),
                        et.getTache().getNom(),
                        sdf.format(et.getDateDebutReelle()),
                        sdf.format(et.getDateFinReelle())
                    );
                }
            }
        } finally {
            session.close();
        }
    }

    /**
     * Affiche la liste des projets gérés par un employé (en tant que chef de projet).
     */
    public void afficherProjetsGeres(Employe e) {
        if (e == null) {
            System.out.println("Employé inexistant.");
            return;
        }
        Session session = HibernateUtil.openSession();
        try {
            Query<Projet> query = session.createQuery(
                "FROM Projet p WHERE p.chefProjet = :employe",
                Projet.class
            );
            query.setParameter("employe", e);
            List<Projet> projets = query.list();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Projets gérés par " + e.getPrenom() + " " + e.getNom() + " :");
            System.out.printf("%-6s %-25s %-15s %-15s%n", "ID", "Nom Projet", "Date Début", "Date Fin");
            for (Projet p : projets) {
                String dFin = (p.getDateFin() != null) ? sdf.format(p.getDateFin()) : "-";
                System.out.printf("%-6d %-25s %-15s %-15s%n",
                    p.getId(),
                    p.getNom(),
                    sdf.format(p.getDateDebut()),
                    dFin
                );
            }
        } finally {
            session.close();
        }
    }
}
