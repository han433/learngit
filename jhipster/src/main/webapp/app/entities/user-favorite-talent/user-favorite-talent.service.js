(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('UserFavoriteTalent', UserFavoriteTalent);

    UserFavoriteTalent.$inject = ['$resource'];

    function UserFavoriteTalent ($resource) {
        var resourceUrl =  'api/user-favorite-talents/:id';

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
