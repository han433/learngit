(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('TeamUser', TeamUser);

    TeamUser.$inject = ['$resource'];

    function TeamUser ($resource) {
        var resourceUrl =  'api/team-users/:id';

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
