(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotListUserDialogController', HotListUserDialogController);

    HotListUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HotListUser'];

    function HotListUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HotListUser) {
        var vm = this;

        vm.hotListUser = entity;
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
            if (vm.hotListUser.id !== null) {
                HotListUser.update(vm.hotListUser, onSaveSuccess, onSaveError);
            } else {
                HotListUser.save(vm.hotListUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:hotListUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
