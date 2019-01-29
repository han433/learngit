(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotTalentDeleteController',HotTalentDeleteController);

    HotTalentDeleteController.$inject = ['$uibModalInstance', 'entity', 'HotTalent'];

    function HotTalentDeleteController($uibModalInstance, entity, HotTalent) {
        var vm = this;

        vm.hotTalent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HotTalent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
