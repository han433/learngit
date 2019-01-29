(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('TalentLocation', TalentLocation);

    TalentLocation.$inject = ['$resource'];

    function TalentLocation ($resource) {
        var resourceUrl =  'api/talent-locations/:id';

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
