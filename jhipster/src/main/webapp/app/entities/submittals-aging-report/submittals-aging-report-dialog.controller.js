(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('SubmittalsAgingReportDialogController', SubmittalsAgingReportDialogController);

    SubmittalsAgingReportDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SubmittalsAgingReport'];

    function SubmittalsAgingReportDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SubmittalsAgingReport) {
        var vm = this;

        vm.submittalsAgingReport = entity;
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
            if (vm.submittalsAgingReport.id !== null) {
                SubmittalsAgingReport.update(vm.submittalsAgingReport, onSaveSuccess, onSaveError);
            } else {
                SubmittalsAgingReport.save(vm.submittalsAgingReport, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:submittalsAgingReportUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
