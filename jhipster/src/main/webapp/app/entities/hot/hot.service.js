(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Hot', Hot);

    Hot.$inject = ['$resource'];

    function Hot ($resource) {
        var resourceUrl =  'api/hots/:id';

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
