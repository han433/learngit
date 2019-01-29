(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Application_commission', Application_commission);

    Application_commission.$inject = ['$resource'];

    function Application_commission ($resource) {
        var resourceUrl =  'api/application-commissions/:id';

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
