(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('JobLocationDialogController', JobLocationDialogController);

    JobLocationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'JobLocation'];

    function JobLocationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, JobLocation) {
        var vm = this;

        vm.jobLocation = entity;
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
            if (vm.jobLocation.id !== null) {
                JobLocation.update(vm.jobLocation, onSaveSuccess, onSaveError);
            } else {
                JobLocation.save(vm.jobLocation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:jobLocationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
