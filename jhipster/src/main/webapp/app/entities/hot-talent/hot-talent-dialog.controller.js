(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotTalentDialogController', HotTalentDialogController);

    HotTalentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HotTalent', 'Hot'];

    function HotTalentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HotTalent, Hot) {
        var vm = this;

        vm.hotTalent = entity;
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
            if (vm.hotTalent.id !== null) {
                HotTalent.update(vm.hotTalent, onSaveSuccess, onSaveError);
            } else {
                HotTalent.save(vm.hotTalent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:hotTalentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
