package dec.docDecanat.data.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;


public class DAO {
	public static List<Session> listSessions = new ArrayList<Session>();
	
    private static final Logger log = Logger.getAnonymousLogger();
    private static final ThreadLocal session = new ThreadLocal();
    @SuppressWarnings("deprecation")
	private static final SessionFactory sessionFactory =
            new AnnotationConfiguration().configure().buildSessionFactory();

    protected DAO() {
    }

    public static Session getSession() {
        Session session = (Session) DAO.session.get();
        if (session == null) {
        	//if (listSessions.size()>0){
        	//	for (int i = 0; i < listSessions.size(); i++) {
        	//		listSessions.get(i).close();
        			
			//	}
        	//	listSessions.clear();
        	//}
            session = sessionFactory.openSession();
            DAO.session.set(session); 
            //listSessions.add(session);
        }
        //System.out.println("DAO str33 listSession size = " +listSessions.size());
        //System.out.println(" DAO str34 " + session);
        return session;
    }

    protected void begin() {
        getSession().beginTransaction();
    }

    protected void commit() {
        getSession().getTransaction().commit();
    }

    protected void rollback() {
        try {
            getSession().getTransaction().rollback();
        } catch (HibernateException e) {
            log.log(Level.WARNING, "Невозможно откатить", e);
        }
        try {
            getSession().close();
        } catch (HibernateException e) {
            log.log(Level.WARNING, "Невозможно закрыть", e);
        }
        DAO.session.set(null);
    }

    public static void close() {
        getSession().close();
        DAO.session.set(null);
    }
}
