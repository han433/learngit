(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserFavoriteJobDialogController', UserFavoriteJobDialogController);

    UserFavoriteJobDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserFavoriteJob'];

    function UserFavoriteJobDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserFavoriteJob) {
        var vm = this;

        vm.userFavoriteJob = entity;
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
            if (vm.userFavoriteJob.id !== null) {
                UserFavoriteJob.update(vm.userFavoriteJob, onSaveSuccess, onSaveError);
            } else {
                UserFavoriteJob.save(vm.userFavoriteJob, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:userFavoriteJobUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
