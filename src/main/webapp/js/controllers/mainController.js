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
    function run(prerunChartArr) {
        // if (params.property)
        // render(params.property);
        render(prerunChartArr);
    }

    function render(prerunChartArr) {
        function renderApp() {
            HB.compileAndInsert('app', 'beforeend', appTemplate, {
                inputFields: TEMPLATE.inputFields(),
                selectFields: TEMPLATE.selectFields()
            });
            $("#start-over-button").hide();
        }

        function renderLogArea() {
            HB.compileAndInsert('init-block', 'afterend', logAreaTemplate);
        }

        function renderChartWindow() {
            HB.compileAndInsert('main-block', 'afterend', 
                chartWindowTemplate, {prerunCharts: TEMPLATE.prerunCharts()});
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
            $.each(TEMPLATE.prerunCharts(), function (objKey, objValue) {
                const buttonId = "#" + objKey + "-button-id";
                const chartId = "#" + objKey + "-id";
                $(buttonId).click(function () {
                    $(chartId).toggle('slow');
                });
                $(chartId).toggle(1);
            });

            webSocketController.connectWs();
        }

        function drawPointChart(id, data) {
            var ctx = document.getElementById(id).getContext("2d");
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
        }

        function drawPrerunCharts(chartArr) {
            $.each(TEMPLATE.prerunCharts(), function (objKey, objValue) {
                var id = objKey + "-id";
                var name;
                var values;
                $.each(chartArr, function (arrIndex, arrValue) {
                    if (arrValue.name == objKey) {
                        name = arrValue.name;
                        values = arrValue.values;
                    }
                });
                drawPointChart(id, [{
                    label: name,
                    data: values
                }]);
            });
        }
        renderApp();
        renderLogArea();
        renderChartWindow();
        bindEvents();
        drawPrerunCharts(prerunChartArr);
    }


    return {
        run: run
    };
});