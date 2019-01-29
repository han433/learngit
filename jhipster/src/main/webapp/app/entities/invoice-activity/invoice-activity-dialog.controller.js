(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('InvoiceActivityDialogController', InvoiceActivityDialogController);

    InvoiceActivityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InvoiceActivity'];

    function InvoiceActivityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InvoiceActivity) {
        var vm = this;

        vm.invoiceActivity = entity;
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
            if (vm.invoiceActivity.id !== null) {
                InvoiceActivity.update(vm.invoiceActivity, onSaveSuccess, onSaveError);
            } else {
                InvoiceActivity.save(vm.invoiceActivity, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:invoiceActivityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
