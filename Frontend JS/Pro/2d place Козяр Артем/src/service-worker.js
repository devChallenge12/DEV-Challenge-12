const APP_CACHE_NAME = 'final-app';

const FILES_TO_CACHE = [
  // '/',
  // '/index.html',
  // '/404.html',

  // General App
  '/runtime.js',
  '/polyfills.js',

  '/vendor.js', // todo dev
  '/styles.js', // todo dev

  // '/styles.css', // todo prod

  '/main.js',
  '/service-worker.js',
  '/favicon.ico',
  '/assets/icons/icon-72x72.png',
  '/assets/icons/icon-96x96.png',
  '/assets/icons/icon-128x128.png',
  '/assets/icons/icon-144x144.png',
  '/assets/icons/icon-152x152.png',
  '/assets/icons/icon-192x192.png',
  '/assets/icons/icon-256x256.png',
  '/assets/icons/icon-384x384.png',
  '/assets/icons/icon-512x512.png'
];

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(APP_CACHE_NAME)
      .then((cache) => cache.addAll(FILES_TO_CACHE).then(() => self.skipWaiting()))
  );
});

self.addEventListener('activate', (event) => {
  return self.clients.claim();
});

self.addEventListener('fetch', (event) => {
  if (event.request.cache === 'only-if-cached' && event.request.mode !== 'same-origin') {
    return;
  }
  event.respondWith(
    caches.match(event.request)
      .then((response) => {
        return response || fetch(event.request);
      })
  );
});
