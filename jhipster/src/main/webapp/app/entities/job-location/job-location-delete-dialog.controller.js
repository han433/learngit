(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('JobLocationDeleteController',JobLocationDeleteController);

    JobLocationDeleteController.$inject = ['$uibModalInstance', 'entity', 'JobLocation'];

    function JobLocationDeleteController($uibModalInstance, entity, JobLocation) {
        var vm = this;

        vm.jobLocation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            JobLocation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
