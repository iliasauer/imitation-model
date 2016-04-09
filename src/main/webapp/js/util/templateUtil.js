define(['jquery'],
    function ($) {

        const FIELD_TYPE = {
            INPUT: 'input',
            SELECT: 'select'
        };

        const INPUT_FIELD = {
            'jobs': 'number of jobs',
            'workers': 'number of workers',
            'storage': 'storage capacity',
            'interval': 'average job entry interval',
            'process': 'average job processing time',
            'runs': 'number of runs',
            'seed': 'generator seed (optional)'
        };

        const SELECT_FIELD = {
            'discipline': {
                'name': 'service discipline',
                'values': ['LIFO', 'FIFO']
            }
        };

        const PRERUN_POINT_CHART = {
            'correlationChart': 'correlation',
            'prevNextChart': 'next value'
        };

        function initPrerunChart() {
            var prerunChartTemp = {};
            $.each(PRERUN_POINT_CHART, function (key, value) {
                prerunChartTemp[key] = value;
            });
            prerunChartTemp['barChart'] = 'histogram of the distribution';
            return prerunChartTemp;
        }
        
        const PRERUN_CHART = initPrerunChart();

        function fieldTypes() {
            return FIELD_TYPE;
        }

        function inputFields() {
            return INPUT_FIELD;
        }

        function selectFields() {
            return SELECT_FIELD;
        }

        function prerunPointCharts() {
            return PRERUN_POINT_CHART;
        }
        
        function prerunCharts() {
            return PRERUN_CHART;
        }

        const DELIMITER = '-';

        function composeId(partsArr) {
            return partsArr.join(DELIMITER);
        }

        function composeJqueryId(partsArr) {
            return '#' + composeId(partsArr);
        }

        function getComposedJqElement(partsArr) {
            return $(composeJqueryId(partsArr));
        }

        return {
            fieldTypes: fieldTypes,
            inputFields: inputFields,
            selectFields: selectFields,
            prerunPointCharts: prerunPointCharts,
            prerunCharts: prerunCharts,
            plainId: composeId,
            jqId: composeJqueryId,
            jqElem: getComposedJqElement
        }
    });