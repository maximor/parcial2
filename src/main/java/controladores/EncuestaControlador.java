package controladores;

import com.google.gson.Gson;
import entidades.Encuesta;
import modelos.EncuestaModelo;

import static spark.Spark.post;
import static spark.Spark.get;

public class EncuestaControlador {
    public EncuestaControlador() {
        EncuestaModelo em = new EncuestaModelo();

        post("/crear", (req, res)->{
            res.type("application/json");
            Encuesta encuesta = new Gson().fromJson(req.body(), Encuesta.class);
            em.crearEncuesta(encuesta);
            return "200 OK";
        });

        get("/encuestas", (req, res)->{
            res.type("application/json");
            return new Gson().toJson(new Gson().toJsonTree(em.encuestas()));
        });

    }
}
