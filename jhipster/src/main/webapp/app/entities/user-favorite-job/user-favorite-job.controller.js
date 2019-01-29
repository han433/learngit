(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserFavoriteJobController', UserFavoriteJobController);

    UserFavoriteJobController.$inject = ['UserFavoriteJob'];

    function UserFavoriteJobController(UserFavoriteJob) {

        var vm = this;

        vm.userFavoriteJobs = [];

        loadAll();

        function loadAll() {
            UserFavoriteJob.query(function(result) {
                vm.userFavoriteJobs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
