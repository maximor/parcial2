
import controladores.EncuestaControlador;
import entidades.Encuesta;
import modelos.EncuestaModelo;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import utils.CreadorDB;
import utils.InializadorDB;

import java.sql.SQLException;
import java.util.HashMap;

import static spark.Spark.*;

public class app {

    public static void main(String[] args){
        staticFileLocation("publico");
        port(5693);

        try{
            CreadorDB.Init();
            InializadorDB.getInstancia().probarConexion();
            CreadorDB.crearTablas();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        before("/*", (req, res) ->{

        });

        get("/", (req, res) -> {
            HashMap<String, Object> contenido =  new HashMap<>();
            EncuestaModelo em = new EncuestaModelo();
            contenido.put("activomain", "active");
            return new ModelAndView(contenido, "plantillas/home.vtl");
        }, new VelocityTemplateEngine());

        get("/mapa", (req, res) -> {
            HashMap<String, Object> contenido = new HashMap<>();
            contenido.put("activomap", "active");
            return new ModelAndView(contenido, "plantillas/mapa.vtl");
        }, new VelocityTemplateEngine());

        EncuestaControlador ec = new EncuestaControlador();
    }
}
