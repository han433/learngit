(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Application_commissionDetailController', Application_commissionDetailController);

    Application_commissionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Application_commission'];

    function Application_commissionDetailController($scope, $rootScope, $stateParams, previousState, entity, Application_commission) {
        var vm = this;

        vm.application_commission = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:application_commissionUpdate', function(event, result) {
            vm.application_commission = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
