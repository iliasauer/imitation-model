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
            $("#start-over-button").hide();
        }

        function renderLogArea(property) {
            HB.compileAndInsert('init-block', 'afterend', logAreaTemplate, {numberOfJobs: property});
        }

        function renderChartWindow(property) {
            HB.compileAndInsert('main-block', 'afterend', chartWindowTemplate, {numberOfJobs: property});
        }

        function bindEvents() {
            function runButtonEvent() {
                $("#run-button").prop("disabled",true);
                const formIdString = "#main-form";
                $.each(TEMPLATE.inputFields(), function (key) {
                    var pElem = $("#" + key + "-id");
                    var inputElem = $("#" + key + "-input-id");
                    pElem.text(pElem.text().split(':')[0] + ": " + inputElem.val());
                });
                $.each(TEMPLATE.selectFields(), function (key) {
                    var pElem = $("#" + key + "-id");
                    var selectElem = $("#" + key + "-select-id");
                    pElem.text(pElem.text().split(':')[0] + ": " + selectElem.val());
                });
                $("#run-button-sign").text("Wait...");
                $.post("/run", $(formIdString).serialize())
                    .done(function (data) {
                        $("#run-button-sign").text(data.status);
                        $("#run-button").prop("disabled", false);
                    });
                $(formIdString).trigger("reset");
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