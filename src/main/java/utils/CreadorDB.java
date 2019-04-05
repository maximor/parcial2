package utils;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreadorDB {
    public CreadorDB() {
        InializadorDB.getInstancia().probarConexion();
    }

    public static void Init() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    public static void stopDb() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }

    public static void crearTablas() throws SQLException {
         Connection con = InializadorDB.getInstancia().getConexion();

        String sql = "CREATE TABLE IF NOT EXISTS ENCUESTA (" +
                " CODIGO INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL," +
                " NOMBRE VARCHAR(100) NOT NULL," +
                " SECTOR VARCHAR(100) NOT NULL," +
                " NIVEL_ESCOLAR VARCHAR(100) NOT NULL," +
                " LATITUD FLOAT NOT NULL," +
                " LONGITUD FLOAT NOT NULL," +
                "UNIQUE KEY CODIGO(CODIGO)" +
                ");";

        con = InializadorDB.getInstancia().getConexion();
        Statement stm = con.createStatement();
        stm.execute(sql);

        stm.close();
        con.close();
    }
}
