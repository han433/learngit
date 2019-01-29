(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotDialogController', HotDialogController);

    HotDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Hot', 'HotUser'];

    function HotDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Hot, HotUser) {
        var vm = this;

        vm.hot = entity;
        vm.clear = clear;
        vm.save = save;
        vm.hotusers = HotUser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.hot.id !== null) {
                Hot.update(vm.hot, onSaveSuccess, onSaveError);
            } else {
                Hot.save(vm.hot, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:hotUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
