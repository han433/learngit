(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ActivityDetailsReportDeleteController',ActivityDetailsReportDeleteController);

    ActivityDetailsReportDeleteController.$inject = ['$uibModalInstance', 'entity', 'ActivityDetailsReport'];

    function ActivityDetailsReportDeleteController($uibModalInstance, entity, ActivityDetailsReport) {
        var vm = this;

        vm.activityDetailsReport = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ActivityDetailsReport.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
