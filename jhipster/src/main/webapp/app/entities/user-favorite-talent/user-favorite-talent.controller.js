(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserFavoriteTalentController', UserFavoriteTalentController);

    UserFavoriteTalentController.$inject = ['UserFavoriteTalent'];

    function UserFavoriteTalentController(UserFavoriteTalent) {

        var vm = this;

        vm.userFavoriteTalents = [];

        loadAll();

        function loadAll() {
            UserFavoriteTalent.query(function(result) {
                vm.userFavoriteTalents = result;
                vm.searchQuery = null;
            });
        }
    }
})();
