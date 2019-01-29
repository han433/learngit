(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotListUserController', HotListUserController);

    HotListUserController.$inject = ['HotListUser'];

    function HotListUserController(HotListUser) {

        var vm = this;

        vm.hotListUsers = [];

        loadAll();

        function loadAll() {
            HotListUser.query(function(result) {
                vm.hotListUsers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
