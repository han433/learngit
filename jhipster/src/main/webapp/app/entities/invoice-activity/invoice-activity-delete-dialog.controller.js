(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('InvoiceActivityDeleteController',InvoiceActivityDeleteController);

    InvoiceActivityDeleteController.$inject = ['$uibModalInstance', 'entity', 'InvoiceActivity'];

    function InvoiceActivityDeleteController($uibModalInstance, entity, InvoiceActivity) {
        var vm = this;

        vm.invoiceActivity = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InvoiceActivity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
