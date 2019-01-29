(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('InvoiceActivityDetailController', InvoiceActivityDetailController);

    InvoiceActivityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'InvoiceActivity'];

    function InvoiceActivityDetailController($scope, $rootScope, $stateParams, previousState, entity, InvoiceActivity) {
        var vm = this;

        vm.invoiceActivity = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:invoiceActivityUpdate', function(event, result) {
            vm.invoiceActivity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
