package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MariageService implements IDao<Mariage> {

    @Override
    public boolean create(Mariage o) {
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
    public boolean update(Mariage o) {
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
    public boolean delete(Mariage o) {
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
    public Mariage getById(int id) {
        Session session = HibernateUtil.openSession();
        try {
            return session.get(Mariage.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Mariage> getAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Mariage", Mariage.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Utilise l'API Criteria JPA pour compter le nombre d'hommes mariés à quatre femmes entre deux dates.
     */
    public long countHommesMariesQuatreFemmes(Date d1, Date d2) {
        Session session = HibernateUtil.openSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Homme> cq = cb.createQuery(Homme.class);
            Root<Mariage> root = cq.from(Mariage.class);

            // SELECT m.homme FROM Mariage m WHERE m.dateDebut BETWEEN :d1 AND :d2 GROUP BY m.homme HAVING COUNT(m) >= 4
            Predicate datePredicate = cb.between(root.get("dateDebut"), d1, d2);
            cq.select(root.get("homme"))
              .where(datePredicate)
              .groupBy(root.get("homme"))
              .having(cb.ge(cb.count(root), 4L));

            List<Homme> results = session.createQuery(cq).getResultList();
            return results.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            session.close();
        }
    }

    /**
     * Affiche les hommes mariés à 4 femmes entre deux dates (API Criteria).
     */
    public void afficherHommesMariesQuatreFemmes(Date d1, Date d2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        long count = countHommesMariesQuatreFemmes(d1, d2);
        System.out.println("Nombre d'hommes mariés à quatre femmes entre " + sdf.format(d1) + " et " + sdf.format(d2) + " (API Criteria) : " + count);
    }
}
