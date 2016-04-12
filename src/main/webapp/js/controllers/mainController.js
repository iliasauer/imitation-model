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

        var inputArr = [];
        var outputArr = [];
        var chartsArr = [];

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
                    {prerunCharts: templateUtil.prerunCharts(),
                     postrunCharts: templateUtil.postrunCharts()});
            }

            function bindEvents() {

                const runButtonId = jqId(['run', 'button']);
                const runButtonSignId = jqId(['run', 'button', 'sign']);
                const formId = jqId(['main', 'form']);
                const outputSelectId = jqId(['output','select','id']);

                function fillParagraphsWithFields(fieldTemplateObj, fieldType) {
                    $.each(fieldTemplateObj, function (key) {
                        const pElem = jqElem([key, 'id']);
                        const fieldElem = jqElem([key, fieldType, 'id']);
                        pElem.text(pElem.text().split(':')[0] + ": " + fieldElem.val());
                    });
                }

                function fillParagraphsWithObj(fieldTemplateObj, inputObj) {
                    inputObj['runs'] = 1;
                    inputObj['seed'] = '';
                    $.each(fieldTemplateObj, function (key) {
                        const pElem = jqElem([key, 'id']);
                        pElem.text(pElem.text().split(':')[0] + ": " + inputObj[key]);
                    });
                }

                function runButtonEvent() {
                    cssUtil.disable(runButtonId);
                    fillParagraphsWithFields(templateUtil.inputFields(), templateUtil.fieldTypes().INPUT);
                    fillParagraphsWithFields(templateUtil.selectFields(), templateUtil.fieldTypes().SELECT);
                    $(runButtonSignId).text('Wait...');
                    $.post('/run', $(formId).serialize())
                        .done(function (ioObj) {
                            const status = ioObj['status'];
                            inputArr = ioObj['input'];
                            outputArr = ioObj['output'];
                            chartsArr = ioObj['charts'];
                            $(runButtonSignId).text(ioObj.status);
                            cssUtil.enable(runButtonId);
                            webSocketController.sendWsMessageRequest('stopLog');
                            cssUtil.enable(outputSelectId);
                            const outputSelect = $(outputSelectId);
                            outputSelect.empty();
                            $.each(outputArr, function (index) {
                                const option = $('<option/>');
                                option.text(index + 1);
                                outputSelect.append(option);
                            });
                            $(outputSelectId + "  option:last").prop('selected', true);
                            output(outputArr[outputArr.length - 1]);
                            $.each(chartsArr, function (index, chartObj) {
                                // chartUtil.drawIndexLineChart(plainId([chartObj.name,'id']), chartObj.values);
                            });
                            $.each(templateUtil.postrunCharts(), function (objKey) {
                                const buttonId = jqId([objKey, 'button', 'id']);
                                cssUtil.enable(buttonId);
                            });
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
                $.each(templateUtil.postrunCharts(), function (objKey) {
                    const buttonId = jqId([objKey, 'button', 'id']);
                    const chartId = jqId([objKey, 'id']);
                    $(buttonId).click(function () {
                        $(chartId).toggle('slow');
                    });
                    cssUtil.disable(buttonId);
                });

                const outputSelect = $(outputSelectId);
                outputSelect.on('change', function() {
                    const index = outputSelect.val();
                    output(outputArr[index - 1]);
                    fillParagraphsWithObj(templateUtil.inputFields(), inputArr[index - 1]);
                    fillParagraphsWithObj(templateUtil.selectFields(), inputArr[index - 1]);
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