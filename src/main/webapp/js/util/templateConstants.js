define(function() {

    const INPUT_FIELDS = {
        "jobs": "number of jobs",
        "workers": "number of workers",
        "storage": "storage capacity",
        "interval": "average job entry interval",
        "process": "average job processing time",
        "runs": "number of runs"
    };

    const SELECT_FIELDS = {
        "discipline": {
            "name": "service discipline",
            "values": ["LIFO", "FIFO"]
        }
    };

    const PRERUN_CHARTS = {
        "correlationChart": "correlation",
        "prevNextChart": "next value"
    };

    function inputFields() {
        return INPUT_FIELDS;
    }

    function selectFields() {
        return SELECT_FIELDS;
    }

    function prerunCharts() {
        return PRERUN_CHARTS;
    }

    return {
        inputFields: inputFields,
        selectFields: selectFields,
        prerunCharts: prerunCharts
    }
});