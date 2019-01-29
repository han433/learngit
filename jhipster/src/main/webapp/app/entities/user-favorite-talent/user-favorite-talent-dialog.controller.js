(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserFavoriteTalentDialogController', UserFavoriteTalentDialogController);

    UserFavoriteTalentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserFavoriteTalent'];

    function UserFavoriteTalentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserFavoriteTalent) {
        var vm = this;

        vm.userFavoriteTalent = entity;
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
            if (vm.userFavoriteTalent.id !== null) {
                UserFavoriteTalent.update(vm.userFavoriteTalent, onSaveSuccess, onSaveError);
            } else {
                UserFavoriteTalent.save(vm.userFavoriteTalent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:userFavoriteTalentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
