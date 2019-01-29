(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('SummaryReport', SummaryReport);

    SummaryReport.$inject = ['$resource'];

    function SummaryReport ($resource) {
        var resourceUrl =  'api/summary-reports/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
