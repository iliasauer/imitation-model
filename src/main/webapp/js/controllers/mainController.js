define([
    "jquery",
    "handlebars",
    "text!../../templates/app.hbs",
    "text!../../templates/logArea.hbs",
    "text!../../templates/chartWindow.hbs",
    "../util/handlebarsShortcuts",
    "../util/templateConstants"
], function ($,
             Handlebars,
             appTemplate,
             logAreaTemplate,
             chartWindowTemplate,
             HB,
             TEMPLATE) {
    function run(params) {
        // if (params.property)
        // render(params.property);
        render(params);
    }

    function render(property) {
        function renderApp(property) {
            HB.compileAndInsert('app', 'beforeend', appTemplate, {
                inputFields: TEMPLATE.inputFields(),
                selectFields: TEMPLATE.selectFields()
            });
        }

        function renderLogArea(property) {
            HB.compileAndInsert('init-block', 'afterend', logAreaTemplate, {numberOfJobs: property});
        }

        function renderChartWindow(property) {
            HB.compileAndInsert('main-block', 'afterend', chartWindowTemplate, {numberOfJobs: property});
        }

        function bindEvents() {
            function runButtonEvent() {
                $.post("/run", $("#main-form").serialize())
                    .done(function (data) {
                        
                        const currentValue = $("#jobs-id").text();
                        $("#jobs-id").text(currentValue + data.property);
                    });
            }
            $("#run-button").click(function () {
                runButtonEvent();
            });
        }

        renderApp(property);
        renderLogArea(property);
        renderChartWindow(property);
        bindEvents();
    }

    return {
        run: run
    };
});