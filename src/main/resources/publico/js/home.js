let indexedDB = window.indexedDB || window.mozIndexedDB || window.webkitIndexedDB || window.msIndexedDB;
let request = indexedDB.open("encuestadb", 1);

var latitud = 0;
var longitud = 0;
function getLocation() {
    if(navigator.geolocation){
        navigator.geolocation.getCurrentPosition(locationt);
    }else{
        alert("Geolocation is not supported by this browser");
    }
}

function locationt(posicion) {
    latitud = posicion.coords.latitude;
    longitud = posicion.coords.longitude;
}
getLocation();


request.onupgradeneeded = function (e) {
    var db = e.target.result;

    if(!db.objectStoreNames.contains('encuestadb')){
        var os = db.createObjectStore('encuestadb', {keyPath: 'codigo', autoIncrement: true});
        os.createIndex('nombre', 'nombre', {unique: false});
    }
}

//Success
request.onsuccess = function (e) {
    console.log('Success: Base de datos Abierta');
    db = e.target.result;

    //Buscar encuesta
    encuestas();
}

request.onerror = function () {
    console.log('Error: No pudo abrir la base de datos');
}

//busca las encuestas que estan guardadas en el browser
function encuestas() {
    let transaction = db.transaction(["encuestadb"], "readonly");

    let store = transaction.objectStore("encuestadb");
    let index = store.index("nombre");

    let output = '';

    index.openCursor().onsuccess = function (e) {
        let cursor = e.target.result;
        if(cursor){
            output += "<tr>";
            output += "<td>"+cursor.value.codigo+"</td>";
            output += "<td style='cursor: pointer'><span class='encuesta' contenteditable='true' data-field='nombre' data-id='"+cursor.value.codigo+"'>"+cursor.value.nombre+"</span></td>";
            output += "<td style='cursor: pointer'><span class='encuesta' contenteditable='true' data-field='sector' data-id='"+cursor.value.codigo+"'>"+cursor.value.sector+"</span></td>";
            output += "<td style='cursor: pointer'><span class='encuesta' contenteditable='true' data-field='nivelEscolar' data-id='"+cursor.value.codigo+"'>"+cursor.value.nivelEscolar+"</span></td>";
            output += "<td>"+cursor.value.latitud+"</td>";
            output += "<td>"+cursor.value.longitud+"</td>";
            output += "<td><a style='cursor:pointer;' onclick='sincronizar("+cursor.value.codigo+")'>Sincronizar</a> | <a style='cursor:pointer;' onclick='eliminarEncuesta("+cursor.value.codigo+")'>Borrar</a></td>";
            output += "</tr>";
            cursor.continue();
        }
        $.get('/encuestas', function (datos) {
            datos.forEach(function (t) {
                output += "<tr>";
                output += "<td>"+t.codigo+"</td>";
                output += "<td style='cursor: pointer'>"+t.nombre+"</td>";
                output += "<td style='cursor: pointer'>"+t.sector+"</td>";
                output += "<td style='cursor: pointer'>"+t.nivelEscolar+"</td>";
                output += "<td>"+t.latitud+"</td>";
                output += "<td>"+t.longitud+"</td>";
                output += "<td>Guardado</td>";
                output += "</tr>";
            });
            $('#encuestaTable').html(output);
        });

    }

}

function eliminarEncuesta(codigo) {
    var transaction = db.transaction(["encuestadb"], "readwrite");
    let store = transaction.objectStore("encuestadb");

    let request = store.delete(codigo);

    request.onsuccess = function () {
        console.log("Borrado Exitosamente");
        window.location.href="/";
    }
}

//sincronizar datos
function sincronizar(codigo) {
    var transaction = db.transaction(["encuestadb"], "readwrite");
    let store = transaction.objectStore("encuestadb");

    let request = store.get(codigo);

    request.onsuccess = function () {
        let data = request.result;
        $.post('/crear', JSON.stringify(data), function (elemento) {
            console.log("entro");

        });

        eliminarEncuesta(codigo);
    }
}

//actualizar los datos
$('#encuestaTable').on('blur', '.encuesta', function () {
    let nuevoTexto = $(this).html();

    let campo = $(this).data('field');
    let codigo = $(this).data('id');

    var transaction = db.transaction(["encuestadb"], "readwrite");
    let store = transaction.objectStore("encuestadb");

    let request = store.get(codigo);

    request.onsuccess = function () {
        let data = request.result;
        if(campo == 'nombre'){
            data.nombre = nuevoTexto;
        }else if(campo == 'sector'){
            data.sector = nuevoTexto;
        }else if(campo == 'nivelEscolar'){
            data.nivelEscolar = nuevoTexto;
        }

        //actualizamos los datos
        let requestActualizar  = store.put(data);

        requestActualizar.onsuccess = function () {
            console.log('La encuesta ha sido actualizada')
        }

        requestActualizar.onerror = function () {
            console.log('Error: La encuesta no puso ser actualizada');
        }
    }
});


function cancelar() {
    $("#nombre").val("");
    $("#sector").val("");
}

function guardar() {
    let nombre = $("#nombre");
    let nivelEscolar = $("#nivele");
    let sector = $("#sector");

    if(estado === "Online"){
        $.post('/crear',
            `{'nombre': '${nombre.val()}', 'nivelEscolar': '${nivelEscolar.val()}', 'sector': '${sector.val()}', 'latitud':${latitud}, 'longitud':${longitud}}`,
            function (data, status, j) {
            console.log('Status:' + status + ', data:' + data);
        }).done(function () {
            console.log("nannana");
        });
        cancelar();
        encuestas();
    }

    if(estado === "Offline"){
        console.log("Ahora mismo estás Offline! La información será guardada temporalmente en el browser");

        let transaction = db.transaction(["encuestadb"], "readwrite");

        //Ask for ObjectStore
        let store = transaction.objectStore("encuestadb");

        let encuesta = {
            nombre: nombre.val(),
            sector: sector.val(),
            nivelEscolar: nivelEscolar.val(),
            latitud: latitud,
            longitud: longitud
        }

        let request = store.add(encuesta);

        //success
        request.onsuccess = function (e) {
            window.location.href="/";
        }

        request.onerror = function (e) {
            console.log('Error', e.target.error.name);
        }

    }



}


