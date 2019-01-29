(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Github_talentController', Github_talentController);

    Github_talentController.$inject = ['Github_talent'];

    function Github_talentController(Github_talent) {

        var vm = this;

        vm.github_talents = [];

        loadAll();

        function loadAll() {
            Github_talent.query(function(result) {
                vm.github_talents = result;
                vm.searchQuery = null;
            });
        }
    }
})();
