(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('SummaryReportDialogController', SummaryReportDialogController);

    SummaryReportDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SummaryReport'];

    function SummaryReportDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SummaryReport) {
        var vm = this;

        vm.summaryReport = entity;
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
            if (vm.summaryReport.id !== null) {
                SummaryReport.update(vm.summaryReport, onSaveSuccess, onSaveError);
            } else {
                SummaryReport.save(vm.summaryReport, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:summaryReportUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
