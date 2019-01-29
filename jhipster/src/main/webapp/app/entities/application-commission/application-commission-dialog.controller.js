(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Application_commissionDialogController', Application_commissionDialogController);

    Application_commissionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Application_commission'];

    function Application_commissionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Application_commission) {
        var vm = this;

        vm.application_commission = entity;
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
            if (vm.application_commission.id !== null) {
                Application_commission.update(vm.application_commission, onSaveSuccess, onSaveError);
            } else {
                Application_commission.save(vm.application_commission, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:application_commissionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
