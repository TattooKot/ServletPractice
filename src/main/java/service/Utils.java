package service;

import model.Event;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Utils {
    private static final Configuration configuration = new Configuration();
    private static SessionFactory sessionFactory;

    private Utils(){

    }

    private static void config(){
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Event.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    public static Session getNewSession(){
        if(sessionFactory == null){
            config();
        }
        return sessionFactory.openSession();
    }

    public static boolean isInit(){
        return sessionFactory == null;
    }
}
