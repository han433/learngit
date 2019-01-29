(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Division', Division);

    Division.$inject = ['$resource', 'DateUtils'];

    function Division ($resource, DateUtils) {
        var resourceUrl =  'api/divisions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.tenantId = DateUtils.convertDateTimeFromServer(data.tenantId);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
