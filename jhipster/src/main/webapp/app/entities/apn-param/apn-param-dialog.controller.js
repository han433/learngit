(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ApnParamDialogController', ApnParamDialogController);

    ApnParamDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ApnParam'];

    function ApnParamDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ApnParam) {
        var vm = this;

        vm.apnParam = entity;
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
            if (vm.apnParam.id !== null) {
                ApnParam.update(vm.apnParam, onSaveSuccess, onSaveError);
            } else {
                ApnParam.save(vm.apnParam, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:apnParamUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
