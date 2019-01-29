(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TalentLocationDeleteController',TalentLocationDeleteController);

    TalentLocationDeleteController.$inject = ['$uibModalInstance', 'entity', 'TalentLocation'];

    function TalentLocationDeleteController($uibModalInstance, entity, TalentLocation) {
        var vm = this;

        vm.talentLocation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TalentLocation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
