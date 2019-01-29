(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TeamUserController', TeamUserController);

    TeamUserController.$inject = ['TeamUser'];

    function TeamUserController(TeamUser) {

        var vm = this;

        vm.teamUsers = [];

        loadAll();

        function loadAll() {
            TeamUser.query(function(result) {
                vm.teamUsers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
