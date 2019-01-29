(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Vc_agreementDialogController', Vc_agreementDialogController);

    Vc_agreementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Vc_agreement'];

    function Vc_agreementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Vc_agreement) {
        var vm = this;

        vm.vc_agreement = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vc_agreement.id !== null) {
                Vc_agreement.update(vm.vc_agreement, onSaveSuccess, onSaveError);
            } else {
                Vc_agreement.save(vm.vc_agreement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:vc_agreementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.sign_date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
