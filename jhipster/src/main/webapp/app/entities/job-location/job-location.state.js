(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('job-location', {
            parent: 'entity',
            url: '/job-location',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.jobLocation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-location/job-locations.html',
                    controller: 'JobLocationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('jobLocation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('job-location-detail', {
            parent: 'job-location',
            url: '/job-location/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.jobLocation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-location/job-location-detail.html',
                    controller: 'JobLocationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('jobLocation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'JobLocation', function($stateParams, JobLocation) {
                    return JobLocation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'job-location',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('job-location-detail.edit', {
            parent: 'job-location-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-location/job-location-dialog.html',
                    controller: 'JobLocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobLocation', function(JobLocation) {
                            return JobLocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-location.new', {
            parent: 'job-location',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-location/job-location-dialog.html',
                    controller: 'JobLocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                city: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('job-location', null, { reload: 'job-location' });
                }, function() {
                    $state.go('job-location');
                });
            }]
        })
        .state('job-location.edit', {
            parent: 'job-location',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-location/job-location-dialog.html',
                    controller: 'JobLocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobLocation', function(JobLocation) {
                            return JobLocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-location', null, { reload: 'job-location' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-location.delete', {
            parent: 'job-location',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-location/job-location-delete-dialog.html',
                    controller: 'JobLocationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['JobLocation', function(JobLocation) {
                            return JobLocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-location', null, { reload: 'job-location' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
