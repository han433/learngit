(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('SequenceDialogController', SequenceDialogController);

    SequenceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sequence'];

    function SequenceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sequence) {
        var vm = this;

        vm.sequence = entity;
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
            if (vm.sequence.id !== null) {
                Sequence.update(vm.sequence, onSaveSuccess, onSaveError);
            } else {
                Sequence.save(vm.sequence, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:sequenceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
