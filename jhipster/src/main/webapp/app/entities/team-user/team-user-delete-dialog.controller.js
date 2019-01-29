(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TeamUserDeleteController',TeamUserDeleteController);

    TeamUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'TeamUser'];

    function TeamUserDeleteController($uibModalInstance, entity, TeamUser) {
        var vm = this;

        vm.teamUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TeamUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
