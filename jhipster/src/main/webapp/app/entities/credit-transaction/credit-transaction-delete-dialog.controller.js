(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('CreditTransactionDeleteController',CreditTransactionDeleteController);

    CreditTransactionDeleteController.$inject = ['$uibModalInstance', 'entity', 'CreditTransaction'];

    function CreditTransactionDeleteController($uibModalInstance, entity, CreditTransaction) {
        var vm = this;

        vm.creditTransaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CreditTransaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
