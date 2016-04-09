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
              templateUtil) {

        function drawPointChart(id, label, values) {
            var ctx = document.getElementById(id).getContext("2d");
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
                drawPointChart(id, name, values);
            });
        }

        return {
            drawPointChart: drawPointChart,
            drawPrerunCharts: drawPrerunCharts
        }
    });
