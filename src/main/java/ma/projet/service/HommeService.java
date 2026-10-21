package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HommeService implements IDao<Homme> {

    @Override
    public boolean create(Homme o) {
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
    public boolean update(Homme o) {
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
    public boolean delete(Homme o) {
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
    public Homme getById(int id) {
        Session session = HibernateUtil.openSession();
        try {
            return session.get(Homme.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Homme> getAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Homme", Homme.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    /**
     * Affiche les épouses d'un homme entre deux dates.
     */
    public List<Femme> getEpousesEntreDates(Homme h, Date d1, Date d2) {
        if (h == null) return Collections.emptyList();
        Session session = HibernateUtil.openSession();
        try {
            Query<Femme> query = session.createQuery(
                "SELECT DISTINCT m.femme FROM Mariage m WHERE m.homme = :homme AND m.dateDebut BETWEEN :d1 AND :d2",
                Femme.class
            );
            query.setParameter("homme", h);
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

    public void afficherEpousesEntreDates(Homme h, Date d1, Date d2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<Femme> epouses = getEpousesEntreDates(h, d1, d2);
        System.out.println("Épouses de " + h.getPrenom() + " " + h.getNom() + " entre " + sdf.format(d1) + " et " + sdf.format(d2) + " :");
        for (Femme f : epouses) {
            System.out.println("- " + f.getPrenom() + " " + f.getNom() + " (Née le " + sdf.format(f.getDateNaissance()) + ")");
        }
    }

    /**
     * Affiche les mariages d'un homme avec le détail complet des mariages en cours et échoués.
     * Respecte le format exact attendu par l'énoncé.
     */
    public void afficherMariagesDetails(Homme h) {
        if (h == null) {
            System.out.println("Homme inexistant.");
            return;
        }

        Session session = HibernateUtil.openSession();
        try {
            Query<Mariage> query = session.createQuery(
                "FROM Mariage m WHERE m.homme = :homme ORDER BY m.dateDebut ASC",
                Mariage.class
            );
            query.setParameter("homme", h);
            List<Mariage> mariages = query.list();

            List<Mariage> enCours = new ArrayList<>();
            List<Mariage> echoues = new ArrayList<>();

            for (Mariage m : mariages) {
                if (m.getDateFin() == null) {
                    enCours.add(m);
                } else {
                    echoues.add(m);
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            System.out.println("Nom : " + h.getNom().toUpperCase() + " " + h.getPrenom().toUpperCase());
            System.out.println("Mariages En Cours :");
            if (enCours.isEmpty()) {
                System.out.println("Aucun mariage en cours.");
            } else {
                int i = 1;
                for (Mariage m : enCours) {
                    String femmeNom = (m.getFemme() != null)
                        ? (m.getFemme().getPrenom().toUpperCase() + " " + m.getFemme().getNom().toUpperCase())
                        : "-";
                    System.out.printf("%d. Femme : %-15s Date Début : %-12s Nbr Enfants : %d%n",
                        i++,
                        femmeNom,
                        sdf.format(m.getDateDebut()),
                        m.getNbrEnfant()
                    );
                }
            }

            System.out.println("\nMariages échoués :");
            if (echoues.isEmpty()) {
                System.out.println("Aucun mariage échoué.");
            } else {
                int i = 1;
                for (Mariage m : echoues) {
                    String femmeNom = (m.getFemme() != null)
                        ? (m.getFemme().getPrenom().toUpperCase() + " " + m.getFemme().getNom().toUpperCase())
                        : "-";
                    System.out.printf("%d. Femme : %-15s Date Début : %-12s%n",
                        i++,
                        femmeNom,
                        sdf.format(m.getDateDebut())
                    );
                    System.out.printf("   Date Fin : %-12s Nbr Enfants : %d%n",
                        sdf.format(m.getDateFin()),
                        m.getNbrEnfant()
                    );
                }
            }
        } finally {
            session.close();
        }
    }
}
