// // //Service Worker
// self.addEventListener('install', function(e) {
//     e.waitUntil(
//         caches.open('pweb-v1').then(function(cache) {
//             return cache.addAll([
//                 '/',
//                 '/mapa',
//                 '../plugins/jquery/dist/jquery.min.js',
//                 '../plugins/bootstrap/dist/js/bootstrap.min.js',
//                 '../plugins/geolocator-2.1.5/dist/geolocator.min.js',
//                 '../js/adminlte.js',
//                 '../js/home.js',
//                 '../js/mapa.js',
//                 '../js/cache.js',
//                 '../plugins/bootstrap/dist/css/bootstrap.min.css',
//                 '../plugins/font-awesome/css/font-awesome.min.css',
//                 '../plugins/Ionicons/css/ionicons.min.css',
//                 '../css/AdminLTE.css',
//                 '../css/skins/skin-blue.min.css'
//
//             ]);
//         })
//     );
// });
//
// self.addEventListener('fetch', function(event) {
//     event.respondWith(
//         caches.match(event.request) //To match current request with cached request it
//             .then(function(response) {
//                 //If response found return it, else fetch again.
//                 return response || fetch(event.request);
//             })
//             .catch(function(error) {
//                 console.error("Error: ", error);
//             })
//     );
// });