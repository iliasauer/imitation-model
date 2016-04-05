define([
    "jquery",
    "handlebars",
    "Chart",
    "Scatter",
    "text!../../templates/app.hbs",
    "text!../../templates/logArea.hbs",
    "text!../../templates/chartWindow.hbs",
    "../util/handlebarsShortcuts",
    "../util/templateConstants",
    "./webSocketController"
], function ($,
             Handlebars,
             Chart,
             Scatter,
             appTemplate,
             logAreaTemplate,
             chartWindowTemplate,
             HB,
             TEMPLATE,
             webSocketController) {
    function run(aChart) {
        // if (params.property)
        // render(params.property);
        render(aChart);
    }

    function render(aChart) {
        function renderApp() {
            HB.compileAndInsert('app', 'beforeend', appTemplate, {
                inputFields: TEMPLATE.inputFields(),
                selectFields: TEMPLATE.selectFields()
            });
            $("#start-over-button").hide();
        }

        function renderLogArea() {
            HB.compileAndInsert('init-block', 'afterend', logAreaTemplate, {numberOfJobs: ""});
        }

        function renderChartWindow() {
            HB.compileAndInsert('main-block', 'afterend', chartWindowTemplate, {numberOfJobs: ""});
        }

        function bindEvents() {
            function runButtonEvent() {
                $("#run-button").prop("disabled", true);
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
                        webSocketController.sendWsMessageRequest('stopLog');
                    });
                $(formIdString).trigger("reset");
                $("#log").val("");
                webSocketController.sendWsMessageRequest('startLog')
            }
            ////////////////////////////////////
            $("#run-button").click(function () {
                runButtonEvent();
            });
            $("#toggle-chart-1").click(function () {
                $("#myChart").toggle('slow');
            });
            webSocketController.connectWs();
        }

        var drawChart = function (data) {
            var ctx = document.getElementById("myChart").getContext("2d");
            new Chart(ctx).Scatter(data, {
                datasetStroke: false,
                responsive: true,
                hoverMode: 'single',
                scales: {
                    xAxes: [{
                        gridLines: {
                            zeroLineColor: "rgba(0,0,0,1)"
                        }
                    }]
                }
            });
        };
        renderApp();
        renderLogArea();
        renderChartWindow();
        bindEvents();
        drawChart([{
            label: 'My First dataset',
            data: aChart.values
        }]);

    }


    return {
        run: run
    };
});