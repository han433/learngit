(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('HotListUser', HotListUser);

    HotListUser.$inject = ['$resource'];

    function HotListUser ($resource) {
        var resourceUrl =  'api/hot-list-users/:id';

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
