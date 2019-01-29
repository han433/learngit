(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('DivisionController', DivisionController);

    DivisionController.$inject = ['Division'];

    function DivisionController(Division) {

        var vm = this;

        vm.divisions = [];

        loadAll();

        function loadAll() {
            Division.query(function(result) {
                vm.divisions = result;
                vm.searchQuery = null;
            });
        }
    }
})();
