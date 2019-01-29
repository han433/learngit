(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('EventController', EventController);

    EventController.$inject = ['Event'];

    function EventController(Event) {

        var vm = this;

        vm.events = [];

        loadAll();

        function loadAll() {
            Event.query(function(result) {
                vm.events = result;
                vm.searchQuery = null;
            });
        }
    }
})();
