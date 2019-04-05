package modelos;

import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Manejador {
    private SessionFactory factory;
    private static Manejador manejador = null;

    public Manejador() {
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch( Throwable ex){
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Manejador getInstancia(){
        if(manejador == null){
            manejador = new Manejador();
        }

        return manejador;
    }

    public int guardar(Object objeto){
        Session session = factory.openSession();
        Transaction tx = null;
        int codigo = -1;

        try{
            tx = session.beginTransaction();
            codigo = (int) session.save(objeto);
            tx.commit();
            return codigo;
        }catch (HibernateError e){
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return codigo;
    }

    public void actualizar(Object objeto){
        Session session = factory.openSession();
        Transaction tx = null;
        int codigo = -1;

        try{
            tx = session.beginTransaction();
            session.update(objeto);
            tx.commit();
        }catch (HibernateError e){
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public SessionFactory getFactory() {
        return factory;
    }

    public void cerrar(){

    }
}
