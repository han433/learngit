(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('UserAlert', UserAlert);

    UserAlert.$inject = ['$resource'];

    function UserAlert ($resource) {
        var resourceUrl =  'api/user-alerts/:id';

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
