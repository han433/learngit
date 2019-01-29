(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('DivisionDialogController', DivisionDialogController);

    DivisionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Division'];

    function DivisionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Division) {
        var vm = this;

        vm.division = entity;
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
            if (vm.division.id !== null) {
                Division.update(vm.division, onSaveSuccess, onSaveError);
            } else {
                Division.save(vm.division, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:divisionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.tenantId = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
