
define([
    "jquery",
    "handlebars",
    "text!../../templates/app.hbs",
    "../util/handlebarsShortcuts"
], function(
    $,
    Handlebars,
    appTemplate,
    HB
) {
    function run(params) {
        // if (params.property)
        // render(params.property);
        render(params);
    }

    function render(property) {
        function renderApp(property) {
            const appHtml = HB.compile(appTemplate, { numberOfJobs: property });
            HB.insert("app", 'afterbegin', appHtml)
        }
        renderApp(property);
    }

    return {
        run: run
    };
});