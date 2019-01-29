(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserFavoriteJobDeleteController',UserFavoriteJobDeleteController);

    UserFavoriteJobDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserFavoriteJob'];

    function UserFavoriteJobDeleteController($uibModalInstance, entity, UserFavoriteJob) {
        var vm = this;

        vm.userFavoriteJob = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserFavoriteJob.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
