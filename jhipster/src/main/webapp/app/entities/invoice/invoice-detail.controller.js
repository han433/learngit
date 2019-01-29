(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('InvoiceDetailController', InvoiceDetailController);

    InvoiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Invoice'];

    function InvoiceDetailController($scope, $rootScope, $stateParams, previousState, entity, Invoice) {
        var vm = this;

        vm.invoice = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:invoiceUpdate', function(event, result) {
            vm.invoice = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
