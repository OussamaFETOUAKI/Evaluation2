package ma.projet.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.Properties;

/**
 * Classe utilitaire pour la gestion du cycle de vie de la SessionFactory Hibernate.
 * Charge automatiquement la configuration depuis application.properties avec
 * basculement automatique sur base embarquée H2 en cas d'indisponibilité de MySQL.
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration cfg = new Configuration();

            // Chargement depuis application.properties si disponible
            Properties props = new Properties();
            try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (input != null) {
                    props.load(input);
                    cfg.setProperties(props);
                } else {
                    cfg.configure("hibernate.cfg.xml");
                }
            } catch (Exception e) {
                cfg.configure("hibernate.cfg.xml");
            }

            // Enregistrement de toutes les classes annotées
            registerAnnotatedClasses(cfg);

            return cfg.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Connexion MySQL échouée ou serveur indisponible (" + ex.getMessage() + ").");
            System.err.println("Basculement automatique sur la base de données en mémoire H2 (mode MySQL)...");
            try {
                Configuration h2Config = new Configuration();
                h2Config.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
                h2Config.setProperty("hibernate.connection.url", "jdbc:h2:mem:evaluation_db;DB_CLOSE_DELAY=-1;MODE=MySQL");
                h2Config.setProperty("hibernate.connection.username", "sa");
                h2Config.setProperty("hibernate.connection.password", "");
                h2Config.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
                h2Config.setProperty("hibernate.hbm2ddl.auto", "update");
                h2Config.setProperty("hibernate.show_sql", "false");
                h2Config.setProperty("hibernate.format_sql", "false");

                registerAnnotatedClasses(h2Config);
                return h2Config.buildSessionFactory();
            } catch (Throwable innerEx) {
                System.err.println("Échec critique de l'initialisation de SessionFactory : " + innerEx);
                throw new ExceptionInInitializerError(innerEx);
            }
        }
    }

    private static void registerAnnotatedClasses(Configuration cfg) {
        // Exercice 1
        cfg.addAnnotatedClass(ma.projet.classes.Categorie.class);
        cfg.addAnnotatedClass(ma.projet.classes.Produit.class);
        cfg.addAnnotatedClass(ma.projet.classes.Commande.class);
        cfg.addAnnotatedClass(ma.projet.classes.LigneCommandeProduit.class);

        // Exercice 2
        cfg.addAnnotatedClass(ma.projet.classes.Employe.class);
        cfg.addAnnotatedClass(ma.projet.classes.Projet.class);
        cfg.addAnnotatedClass(ma.projet.classes.Tache.class);
        cfg.addAnnotatedClass(ma.projet.classes.EmployeTache.class);

        // Exercice 3
        cfg.addAnnotatedClass(ma.projet.beans.Personne.class);
        cfg.addAnnotatedClass(ma.projet.beans.Homme.class);
        cfg.addAnnotatedClass(ma.projet.beans.Femme.class);
        cfg.addAnnotatedClass(ma.projet.beans.Mariage.class);
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
