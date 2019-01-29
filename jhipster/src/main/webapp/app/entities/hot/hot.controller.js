(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotController', HotController);

    HotController.$inject = ['Hot'];

    function HotController(Hot) {

        var vm = this;

        vm.hots = [];

        loadAll();

        function loadAll() {
            Hot.query(function(result) {
                vm.hots = result;
                vm.searchQuery = null;
            });
        }
    }
})();
