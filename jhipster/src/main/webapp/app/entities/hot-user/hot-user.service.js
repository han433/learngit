(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('HotUser', HotUser);

    HotUser.$inject = ['$resource'];

    function HotUser ($resource) {
        var resourceUrl =  'api/hot-users/:id';

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
