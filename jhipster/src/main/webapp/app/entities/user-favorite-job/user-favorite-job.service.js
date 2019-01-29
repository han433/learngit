(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('UserFavoriteJob', UserFavoriteJob);

    UserFavoriteJob.$inject = ['$resource'];

    function UserFavoriteJob ($resource) {
        var resourceUrl =  'api/user-favorite-jobs/:id';

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
