/**
 * Contains environment specific configuration of the application.
 *
 * Assigns __env to the root window object.
 */
(function(window) {
    window.__env = window.__env || {};

    // REST services
    window.__env.locationServiceUrl = 'http://192.168.99.100:8081';
    window.__env.menuServiceUrl = 'http://192.168.99.100:8082';
    window.__env.orderServiceUrl = 'http://192.168.99.100:8082';

    // Logging
    window.__env.enableDebug = true;

}(this));
