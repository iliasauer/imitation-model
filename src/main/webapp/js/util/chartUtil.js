define([
        'jquery',
        'handlebars',
        'Chart',
        'Scatter',
        'Chart2',
        './templateUtil'],
    function ($,
              Handlebars,
              Chart,
              Scatter,
              Chart2,
              templateUtil) {

        function drawPointChart(id, label, values) {
            const ctx = document.getElementById(id).getContext("2d");
            new Chart(ctx).Scatter([{
                label: label,
                data: values
            }], {
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

        function drawLineChart() {
        }

        const overlayData = {
            labels: ["January", "February", "March", "April", "May", "Jun", "July"],
            datasets: [{
                label: "My First dataset",
                type: "bar",
                yAxesGroup: "1",
                fillColor: "rgba(151,137,200,0.5)",
                strokeColor: "rgba(151,137,200,0.8)",
                highlightFill: "rgba(151,137,200,0.75)",
                highlightStroke: "rgba(151,137,200,1)",
                data: [28, 48, 40, 19, 86, 27, 90]
            }, {
                label: "My Second dataset",
                type: "line",
                yAxesGroup: "2",
                fillColor: "rgba(151,187,205,0.5)",
                strokeColor: "rgba(151,187,205,0.8)",
                highlightFill: "rgba(151,187,205,0.75)",
                highlightStroke: "rgba(151,187,205,1)",
                data: [8, 38, 30, 29, 46, 67, 80]
            }],
            yAxes: [{
                name: "1",
                scalePositionLeft: false,
                scaleFontColor: "rgba(151,137,200,0.8)"
            }, {
                name: "2",
                scalePositionLeft: true,
                scaleFontColor: "rgba(151,187,205,0.8)"
            }]
        };

        function drawBarChart(id, xLabels, barLabel, barValues, lineLabel, lineValues) {
            const overlayData = {
                labels: xLabels,
                datasets: [{
                    label: barLabel,
                    type: "bar",
                    yAxesGroup: "1",
                    fillColor: "rgba(151,137,200,0.5)",
                    strokeColor: "rgba(151,137,200,0.8)",
                    highlightFill: "rgba(151,137,200,0.75)",
                    highlightStroke: "rgba(151,137,200,1)",
                    data: barValues
                }, {
                    label: lineLabel,
                    type: "line",
                    yAxesGroup: "2",
                    fillColor: "rgba(151,187,205,0.5)",
                    strokeColor: "rgba(151,187,205,0.8)",
                    highlightFill: "rgba(151,187,205,0.75)",
                    highlightStroke: "rgba(151,187,205,1)",
                    data: lineValues
                }],
                yAxes: [{
                    name: "1",
                    scalePositionLeft: false,
                    scaleFontColor: "rgba(151,137,200,0.8)"
                }, {
                    name: "2",
                    scalePositionLeft: true,
                    scaleFontColor: "rgba(151,187,205,0.8)"
                }]
            };
            const ctx = document.getElementById(id).getContext("2d");
            new Chart2(ctx).Overlay(overlayData, {
                populateSparseData: true,
                overlayBars: false
            });
        }

        function drawPrerunCharts(chartArr) {
            $.each(templateUtil.prerunCharts(), function (objKey) {
                const id = templateUtil.plainId([objKey, 'id']);
                var name = '';
                var values = [];
                $.each(chartArr, function (arrIndex, arrValue) {
                    if (arrValue.name == objKey) {
                        name = arrValue.name;
                        values = arrValue.values;
                    }
                });
                if (!id.startsWith('bar')) {
                    drawPointChart(id, name, values);
                }
            });
        }

        return {
            drawPointChart: drawPointChart,
            drawLineChart: drawLineChart,
            drawBarChart: drawBarChart,
            drawPrerunCharts: drawPrerunCharts
        }
    });
