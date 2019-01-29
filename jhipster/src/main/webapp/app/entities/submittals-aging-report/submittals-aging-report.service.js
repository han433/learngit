(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('SubmittalsAgingReport', SubmittalsAgingReport);

    SubmittalsAgingReport.$inject = ['$resource'];

    function SubmittalsAgingReport ($resource) {
        var resourceUrl =  'api/submittals-aging-reports/:id';

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
