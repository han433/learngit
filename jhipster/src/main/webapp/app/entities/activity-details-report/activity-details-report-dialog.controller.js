(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ActivityDetailsReportDialogController', ActivityDetailsReportDialogController);

    ActivityDetailsReportDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ActivityDetailsReport'];

    function ActivityDetailsReportDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ActivityDetailsReport) {
        var vm = this;

        vm.activityDetailsReport = entity;
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
            if (vm.activityDetailsReport.id !== null) {
                ActivityDetailsReport.update(vm.activityDetailsReport, onSaveSuccess, onSaveError);
            } else {
                ActivityDetailsReport.save(vm.activityDetailsReport, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:activityDetailsReportUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
