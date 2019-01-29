(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotUserController', HotUserController);

    HotUserController.$inject = ['HotUser'];

    function HotUserController(HotUser) {

        var vm = this;

        vm.hotUsers = [];

        loadAll();

        function loadAll() {
            HotUser.query(function(result) {
                vm.hotUsers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
