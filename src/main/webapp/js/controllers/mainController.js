define([
    "jquery",
    "Chart",
    "Scatter",
    "text!../../templates/app.hbs",
    "text!../../templates/logArea.hbs",
    "text!../../templates/chartWindow.hbs",
    "../util/handlebarsUtil",
    "../util/templateUtil",
    "../util/cssUtil",
    "../util/chartUtil",
    "./webSocketController"
], function ($,
             Chart,
             Scatter,
             appTemplate,
             logAreaTemplate,
             chartWindowTemplate,
             hbUtil,
             templateUtil,
             cssUtil,
             chartUtil,
             webSocketController) {

    const jqId = templateUtil.jqId;
    const jqElem = templateUtil.jqElem;

    function run(prerunChartArr) {
        render(prerunChartArr);
    }

    function render(prerunChartArr) {
        function renderApp() {
            hbUtil.compileAndInsertInside(jqId(['app']), appTemplate, {
                inputFields: templateUtil.inputFields(),
                selectFields: templateUtil.selectFields()
            });
            cssUtil.hide(jqId(['start', 'over', 'button']));
        }

        function renderLogArea() {
            hbUtil.compileAndInsertAfter(jqId(['init', 'block']), logAreaTemplate);
        }

        function renderChartWindow() {
            hbUtil.compileAndInsertAfter(jqId(['main', 'block']), chartWindowTemplate,
                {prerunCharts: templateUtil.prerunCharts()});
        }

        function bindEvents() {

            const runButtonId = jqId(['run', 'button']);
            const runButtonSignId = jqId(['run', 'button', 'sign']);
            const formId = jqId(['main', 'form']);
            
            function runButtonEvent() {
                cssUtil.disable(runButtonId);
                function fillSuitableParagraphs(fieldTemplateObj, fieldType) {
                    $.each(fieldTemplateObj, function (key) {
                        const pElem = jqElem([key, 'id']);
                        const fieldElem = jqElem([key, fieldType, 'id']);
                        pElem.text(pElem.text().split(':')[0] + ": " + fieldElem.val());
                    });
                }
                fillSuitableParagraphs(templateUtil.inputFields(), templateUtil.fieldTypes().INPUT);
                fillSuitableParagraphs(templateUtil.selectFields(), templateUtil.fieldTypes().SELECT);
                
                $(runButtonSignId).text('Wait...');
                $.post("/run", $(formId).serialize())
                    .done(function (data) {
                        $(runButtonSignId).text(data.status);
                        cssUtil.enable(runButtonId);
                        webSocketController.sendWsMessageRequest('stopLog');
                    });
                $(formId).trigger('reset');
                $(jqId(['log'])).val('');
                webSocketController.sendWsMessageRequest('startLog')
            }
            ////////////////////////////////////
            $(runButtonId).click(function () {
                runButtonEvent();
            });
            $.each(templateUtil.prerunCharts(), function (objKey) {
                const buttonId = jqId([objKey, 'button', 'id']);
                const chartId = jqId([objKey, 'id']);
                $(buttonId).click(function () {
                    $(chartId).toggle('slow');
                });
                $(chartId).toggle(1);
            });
        }

        renderApp();
        renderLogArea();
        renderChartWindow();
        bindEvents();
        webSocketController.connectWs();
        chartUtil.drawPrerunCharts(prerunChartArr);
    }


    return {
        run: run
    };
});