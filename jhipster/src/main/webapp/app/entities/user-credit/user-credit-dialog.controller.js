(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserCreditDialogController', UserCreditDialogController);

    UserCreditDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserCredit'];

    function UserCreditDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserCredit) {
        var vm = this;

        vm.userCredit = entity;
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
            if (vm.userCredit.id !== null) {
                UserCredit.update(vm.userCredit, onSaveSuccess, onSaveError);
            } else {
                UserCredit.save(vm.userCredit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:userCreditUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
