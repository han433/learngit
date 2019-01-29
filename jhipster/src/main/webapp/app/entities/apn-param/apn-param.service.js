(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('ApnParam', ApnParam);

    ApnParam.$inject = ['$resource'];

    function ApnParam ($resource) {
        var resourceUrl =  'api/apn-params/:id';

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
