(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TalentLocationDialogController', TalentLocationDialogController);

    TalentLocationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TalentLocation'];

    function TalentLocationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TalentLocation) {
        var vm = this;

        vm.talentLocation = entity;
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
            if (vm.talentLocation.id !== null) {
                TalentLocation.update(vm.talentLocation, onSaveSuccess, onSaveError);
            } else {
                TalentLocation.save(vm.talentLocation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:talentLocationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
