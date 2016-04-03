//  Load RequireJS configuration before any other actions
require(["./requirejs/config"], function() {
    //  App entry point
    require([
        "jquery",
    ], function($) {
        //  get all properties from runTimeConfig
        alert("It works!");
    });
});
