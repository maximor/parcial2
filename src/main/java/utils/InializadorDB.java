package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InializadorDB {
    private static InializadorDB instancia = null;
    private static String URL = "jdbc:h2:tcp://localhost/~/encuesta";
    private InializadorDB(){
        cargarDriver();
    }

    public static InializadorDB getInstancia(){
        if(instancia == null){
            return new InializadorDB();
        }

        return instancia;
    }

    public Connection getConexion() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public void cargarDriver(){
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void probarConexion(){
        try {
            getConexion().close();
            System.out.println("Conexión realizado con exito...");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
