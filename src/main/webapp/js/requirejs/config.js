requirejs.config({
    paths: {
        'bootstrap': ['libs/bootstrap', 'bootstrap'],
        'jquery': ['libs/jquery', 'jquery'],
        'handlebars': ['libs/handlebars', 'handlebars'],
        'text': ['libs/text', 'text'],
        'ScatterChart': ['libs/ScatterChart', 'ScatterChart'],
        'ChartFork': ['libs/ChartFork', 'ChartFork'],
        'Scatter': ['libs/Scatter', 'Scatter']
    },
    map: {
        'Scatter': {
            'Chart': 'ScatterChart'
        }
    }

});