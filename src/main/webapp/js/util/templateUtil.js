define(function() {

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
        'runs': 'number of runs'
    };

    const SELECT_FIELD = {
        'discipline': {
            'name': 'service discipline',
            'values': ['LIFO', 'FIFO']
        }
    };

    const PRERUN_CHART = {
        'correlationChart': 'correlation',
        'prevNextChart': 'next value'
    };

    function fieldTypes() {
        return FIELD_TYPE;
    }
    
    function inputFields() {
        return INPUT_FIELD;
    }

    function selectFields() {
        return SELECT_FIELD;
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
        prerunCharts: prerunCharts,
        plainId: composeId,
        jqId: composeJqueryId,
        jqElem: getComposedJqElement
    }
});