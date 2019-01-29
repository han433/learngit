(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserFavoriteTalentDeleteController',UserFavoriteTalentDeleteController);

    UserFavoriteTalentDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserFavoriteTalent'];

    function UserFavoriteTalentDeleteController($uibModalInstance, entity, UserFavoriteTalent) {
        var vm = this;

        vm.userFavoriteTalent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserFavoriteTalent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
