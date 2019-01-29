(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('JobLocation', JobLocation);

    JobLocation.$inject = ['$resource'];

    function JobLocation ($resource) {
        var resourceUrl =  'api/job-locations/:id';

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
