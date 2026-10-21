package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FemmeService implements IDao<Femme> {

    @Override
    public boolean create(Femme o) {
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
    public boolean update(Femme o) {
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
    public boolean delete(Femme o) {
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
    public Femme getById(int id) {
        Session session = HibernateUtil.openSession();
        try {
            return session.get(Femme.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Femme> getAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Femme", Femme.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Exécute une requête native nommée retournant le nombre d'enfants d'une femme entre deux dates.
     */
    public int countEnfantsEntreDates(Femme f, Date d1, Date d2) {
        if (f == null) return 0;
        Session session = HibernateUtil.openSession();
        try {
            Query<?> query = session.createNamedQuery("Femme.countEnfantsEntreDates");
            query.setParameter("femmeId", f.getId());
            query.setParameter("dateDebut", d1);
            query.setParameter("dateFin", d2);
            Object result = query.getSingleResult();
            if (result instanceof Number) {
                return ((Number) result).intValue();
            }
            return 0;
        } catch (Exception e) {
            // Fallback via HQL si la requête SQL native varie selon le dialecte
            try {
                Query<Long> hql = session.createQuery(
                    "SELECT COALESCE(SUM(m.nbrEnfant), 0) FROM Mariage m " +
                    "WHERE m.femme = :femme AND m.dateDebut BETWEEN :d1 AND :d2",
                    Long.class
                );
                hql.setParameter("femme", f);
                hql.setParameter("d1", d1);
                hql.setParameter("d2", d2);
                Long sum = hql.uniqueResult();
                return sum != null ? sum.intValue() : 0;
            } catch (Exception ex) {
                ex.printStackTrace();
                return 0;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Affiche le nombre d'enfants d'une femme entre deux dates.
     */
    public void afficherNombreEnfants(Femme f, Date d1, Date d2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int count = countEnfantsEntreDates(f, d1, d2);
        System.out.println("Nombre d'enfants pour " + f.getPrenom() + " " + f.getNom() +
                " entre " + sdf.format(d1) + " et " + sdf.format(d2) + " : " + count);
    }

    /**
     * Exécute une requête nommée retournant les femmes mariées au moins deux fois.
     */
    public List<Femme> getFemmesMarieesAuMoinsDeuxFois() {
        Session session = HibernateUtil.openSession();
        try {
            Query<Femme> query = session.createNamedQuery("Femme.findFemmesMarieesAuMoinsDeuxFois", Femme.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Affiche les femmes mariées deux fois ou plus.
     */
    public void afficherFemmesMarieesAuMoinsDeuxFois() {
        List<Femme> list = getFemmesMarieesAuMoinsDeuxFois();
        System.out.println("Femmes mariées deux fois ou plus (Requête nommée) :");
        for (Femme f : list) {
            System.out.println("- " + f.getPrenom() + " " + f.getNom() + " (Téléphone : " + f.getTelephone() + ")");
        }
    }

    /**
     * Retourne la femme la plus âgée (date de naissance la plus ancienne).
     */
    public Femme getPlusAgee() {
        Session session = HibernateUtil.openSession();
        try {
            Query<Femme> query = session.createQuery(
                "FROM Femme f ORDER BY f.dateNaissance ASC",
                Femme.class
            );
            query.setMaxResults(1);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    /**
     * Affiche la femme la plus âgée.
     */
    public void afficherPlusAgee() {
        Femme f = getPlusAgee();
        if (f != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Femme la plus âgée : " + f.getPrenom() + " " + f.getNom() +
                    " (Née le " + sdf.format(f.getDateNaissance()) + ")");
        } else {
            System.out.println("Aucune femme enregistrée.");
        }
    }
}
