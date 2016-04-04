/**
 * Created on 01.04.16.
 */
define(function() {

    const INPUT_FIELDS = {
        "jobs": "number of jobs",
        "workers": "number of workers",
        "storage": "storage capacity",
        "interval": "average job entry interval",
        "process": "average job processing time"
    };

    const SELECT_FIELDS = {
        "discipline": {
            "name": "service discipline",
            "values": ["LIFO", "FIFO"]
        }
    };

    function inputFields() {
        return INPUT_FIELDS;
    }

    function selectFields() {
        return SELECT_FIELDS;
    }

    return {
        inputFields: inputFields,
        selectFields: selectFields
    }
});