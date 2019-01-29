(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotUserDialogController', HotUserDialogController);

    HotUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HotUser', 'Hot'];

    function HotUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HotUser, Hot) {
        var vm = this;

        vm.hotUser = entity;
        vm.clear = clear;
        vm.save = save;
        vm.hots = Hot.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.hotUser.id !== null) {
                HotUser.update(vm.hotUser, onSaveSuccess, onSaveError);
            } else {
                HotUser.save(vm.hotUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:hotUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
