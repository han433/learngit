(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserAlertDialogController', UserAlertDialogController);

    UserAlertDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserAlert'];

    function UserAlertDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserAlert) {
        var vm = this;

        vm.userAlert = entity;
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
            if (vm.userAlert.id !== null) {
                UserAlert.update(vm.userAlert, onSaveSuccess, onSaveError);
            } else {
                UserAlert.save(vm.userAlert, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:userAlertUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
