(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('DivisionDetailController', DivisionDetailController);

    DivisionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Division'];

    function DivisionDetailController($scope, $rootScope, $stateParams, previousState, entity, Division) {
        var vm = this;

        vm.division = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:divisionUpdate', function(event, result) {
            vm.division = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
