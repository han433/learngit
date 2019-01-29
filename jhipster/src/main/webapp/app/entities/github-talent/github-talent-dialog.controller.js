(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Github_talentDialogController', Github_talentDialogController);

    Github_talentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Github_talent'];

    function Github_talentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Github_talent) {
        var vm = this;

        vm.github_talent = entity;
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
            if (vm.github_talent.id !== null) {
                Github_talent.update(vm.github_talent, onSaveSuccess, onSaveError);
            } else {
                Github_talent.save(vm.github_talent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:github_talentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
