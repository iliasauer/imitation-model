requirejs.config({
    paths: {
        'bootstrap': ['libs/bootstrap', 'bootstrap'],
        'jquery': ['libs/jquery', 'jquery'],
        'handlebars': ['libs/handlebars', 'handlebars'],
        'text': ['libs/text', 'text'],
        'ChartCore': ['libs/ChartCore', 'ChartCore'],
        'ChartFork': ['libs/ChartFork', 'ChartFork'],
        'ChartScatter': ['libs/ChartScatter', 'ChartScatter']
    },
    map: {
        'ChartScatter': {
            'Chart': 'ChartCore'
        }
    }
});