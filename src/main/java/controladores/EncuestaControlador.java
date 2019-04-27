package controladores;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

            return new Gson().toJson("{'mensaje':'Ok, 200'}");
        });

        get("/encuestas", (req, res)->{
            res.type("application/json");
            return new Gson().toJson(new Gson().toJsonTree(em.encuestas()));
        });

    }
}
