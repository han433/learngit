(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('CreditTransactionDialogController', CreditTransactionDialogController);

    CreditTransactionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CreditTransaction'];

    function CreditTransactionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CreditTransaction) {
        var vm = this;

        vm.creditTransaction = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.creditTransaction.id !== null) {
                CreditTransaction.update(vm.creditTransaction, onSaveSuccess, onSaveError);
            } else {
                CreditTransaction.save(vm.creditTransaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:creditTransactionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
