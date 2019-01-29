(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ActivityReportDialogController', ActivityReportDialogController);

    ActivityReportDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ActivityReport'];

    function ActivityReportDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ActivityReport) {
        var vm = this;

        vm.activityReport = entity;
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
            if (vm.activityReport.id !== null) {
                ActivityReport.update(vm.activityReport, onSaveSuccess, onSaveError);
            } else {
                ActivityReport.save(vm.activityReport, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:activityReportUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
