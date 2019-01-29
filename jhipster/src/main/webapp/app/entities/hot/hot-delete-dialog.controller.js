(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotDeleteController',HotDeleteController);

    HotDeleteController.$inject = ['$uibModalInstance', 'entity', 'Hot'];

    function HotDeleteController($uibModalInstance, entity, Hot) {
        var vm = this;

        vm.hot = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Hot.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
