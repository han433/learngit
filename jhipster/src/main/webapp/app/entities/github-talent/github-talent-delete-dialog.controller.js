(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Github_talentDeleteController',Github_talentDeleteController);

    Github_talentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Github_talent'];

    function Github_talentDeleteController($uibModalInstance, entity, Github_talent) {
        var vm = this;

        vm.github_talent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Github_talent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
