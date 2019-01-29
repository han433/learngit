(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('ActivityReport', ActivityReport);

    ActivityReport.$inject = ['$resource'];

    function ActivityReport ($resource) {
        var resourceUrl =  'api/activity-reports/:id';

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
