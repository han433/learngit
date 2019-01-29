(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('UserCredit', UserCredit);

    UserCredit.$inject = ['$resource'];

    function UserCredit ($resource) {
        var resourceUrl =  'api/user-credits/:id';

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
