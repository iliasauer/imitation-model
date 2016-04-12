define([
        'jquery',
        'handlebars',
        'Chart',
        'Scatter',
        'Chart2',
        'Chart3',
        './templateUtil'],
    function ($,
              Handlebars,
              Chart,
              Scatter,
              Chart2,
              Chart3,
              templateUtil) {

        function drawPointChart(id, label, values) {
            const ctx = document.getElementById(id).getContext("2d");
            const chartData = [{
                label: label,
                data: values
            }];
            new Chart(ctx).Scatter(chartData, {
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

        // function drawIndexLineChart(id, values) {
        //     const ctx = document.getElementById(id).getContext("2d");
        //     var labels = [];
        //     for (var i = 0; i < values.length; i++) {
        //         labels.push(i);
        //     }
        //     const chartData = [{
        //         labels: labels,
        //         datasets: [
        //             {
        //                 label: id,
        //                 fillColor: "rgba(220,220,220,0.2)",
        //                 strokeColor: "rgba(220,220,220,1)",
        //                 pointColor: "rgba(220,220,220,1)",
        //                 pointStrokeColor: "#fff",
        //                 pointHighlightFill: "#fff",
        //                 pointHighlightStroke: "rgba(220,220,220,1)",
        //                 data: values
        //             }
        //         ]
        //     }];
        //     new Chart2(ctx).Line(chartData, {});
        // }

        function drawPrerunPointCharts(chartArr) {
            $.each(templateUtil.prerunPointCharts(), function (objKey) {
                const id = templateUtil.plainId([objKey, 'id']);
                var name = '';
                var values = [];
                $.each(chartArr, function (arrIndex, arrValue) {
                    if (arrValue.name == objKey) {
                        name = arrValue.name;
                        values = arrValue.values;
                    }
                });
                drawPointChart(id, name, values);
            });
        }

        return {
            drawPointChart: drawPointChart,
            // drawIndexLineChart: drawIndexLineChart,
            drawBarChart: drawBarChart,
            drawPrerunPointCharts: drawPrerunPointCharts
        }
    });
