package modelos;

import entidades.Encuesta;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EncuestaModelo {
    public EncuestaModelo() {
    }

    public int crearEncuesta(Encuesta encuesta){
        return Manejador.getInstancia().guardar(encuesta);
    }

    public void actualizar(Encuesta encuesta){
        Manejador.getInstancia().actualizar(encuesta);
    }

    public List<Encuesta> encuestas(){
        Session session = Manejador.getInstancia().getFactory().openSession();
        List encuestas = null;
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            encuestas = session.createQuery("from entidades.Encuesta").list();
            tx.commit();
            return encuestas;
        }catch (HibernateError e){
            if(tx != null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }

        return null;
    }
}
