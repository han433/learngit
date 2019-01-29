(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TeamUserDialogController', TeamUserDialogController);

    TeamUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TeamUser'];

    function TeamUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TeamUser) {
        var vm = this;

        vm.teamUser = entity;
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
            if (vm.teamUser.id !== null) {
                TeamUser.update(vm.teamUser, onSaveSuccess, onSaveError);
            } else {
                TeamUser.save(vm.teamUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:teamUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
