define(['jquery',
        'text!../../templates/app.hbs',
        'text!../../templates/outputArea.hbs',
        'text!../../templates/chartWindow.hbs',
        '../util/handlebarsUtil',
        '../util/templateUtil',
        '../util/cssUtil',
        '../util/chartUtil',
        './webSocketController'],
    function ($,
              appTemplate,
              outputAreaTemplate,
              chartWindowTemplate,
              hbUtil,
              templateUtil,
              cssUtil,
              chartUtil,
              webSocketController) {

        const plainId = templateUtil.plainId;
        const jqId = templateUtil.jqId;
        const jqElem = templateUtil.jqElem;

        var outputsArr = [];

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

            function renderOutputArea() {
                hbUtil.compileAndInsertAfter(jqId(['init', 'block']), outputAreaTemplate);
                cssUtil.disable(jqId(['output','select','id']));
            }

            function renderChartWindow() {
                hbUtil.compileAndInsertAfter(jqId(['main', 'block']), chartWindowTemplate,
                    {prerunCharts: templateUtil.prerunCharts()});
            }

            function bindEvents() {

                const runButtonId = jqId(['run', 'button']);
                const runButtonSignId = jqId(['run', 'button', 'sign']);
                const formId = jqId(['main', 'form']);
                const outputSelectId = jqId(['output','select','id']);

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
                        .done(function (outputObj) {
                            $(runButtonSignId).text(outputObj.status);
                            cssUtil.enable(runButtonId);
                            webSocketController.sendWsMessageRequest('stopLog');
                            outputsArr.push(outputObj);
                            cssUtil.enable(outputSelectId);
                            const outputSelect = $(outputSelectId);
                            outputSelect.empty();
                            $.each(outputsArr, function (index) {
                                const option = $('<option/>');
                                option.text(index + 1);
                                outputSelect.append(option);
                            });
                            output(outputObj);
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

                const outputSelect = $(outputSelectId);
                outputSelect.on('change', function() {
                    const index = outputSelect.val();
                    output(outputsArr[index - 1]);
                });

            }

            function drawPrerunCharts() {
                chartUtil.drawPrerunPointCharts(prerunChartArr);
                const barChartObj = prerunChartArr[2];
                const lineChartObj = prerunChartArr[3];
                chartUtil.drawBarChart(plainId(['barChart','id']), barChartObj.intervalsLabels,
                    'histogram', barChartObj.values,
                    'density', lineChartObj.values);
            }

            renderApp();
            renderOutputArea();
            renderChartWindow();
            bindEvents();
            drawPrerunCharts();

            webSocketController.connectWs();
        }
        
        function output(outputObj) {
            const outputBlock = $(jqId(['output', 'block']));
            outputBlock.empty();
            $.each(templateUtil.outputs(), function (key, value) {
                const paragraph = $('<p/>');
                cssUtil.addId(plainId([key, 'id']), paragraph);
                outputBlock.append(paragraph);
                paragraph.text('The ' + value + ': ' + outputObj[key]);
            })
        }

        return {
            run: run
        };
    });