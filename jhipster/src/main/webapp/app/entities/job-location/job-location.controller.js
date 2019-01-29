(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('JobLocationController', JobLocationController);

    JobLocationController.$inject = ['JobLocation'];

    function JobLocationController(JobLocation) {

        var vm = this;

        vm.jobLocations = [];

        loadAll();

        function loadAll() {
            JobLocation.query(function(result) {
                vm.jobLocations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
