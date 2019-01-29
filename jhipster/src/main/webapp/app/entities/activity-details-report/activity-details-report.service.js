(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('ActivityDetailsReport', ActivityDetailsReport);

    ActivityDetailsReport.$inject = ['$resource'];

    function ActivityDetailsReport ($resource) {
        var resourceUrl =  'api/activity-details-reports/:id';

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
