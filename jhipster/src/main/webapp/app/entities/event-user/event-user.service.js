(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Event_user', Event_user);

    Event_user.$inject = ['$resource'];

    function Event_user ($resource) {
        var resourceUrl =  'api/event-users/:id';

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
