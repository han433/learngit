(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('division', {
            parent: 'entity',
            url: '/division',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.division.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/division/divisions.html',
                    controller: 'DivisionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('division');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('division-detail', {
            parent: 'division',
            url: '/division/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.division.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/division/division-detail.html',
                    controller: 'DivisionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('division');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Division', function($stateParams, Division) {
                    return Division.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'division',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('division-detail.edit', {
            parent: 'division-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/division/division-dialog.html',
                    controller: 'DivisionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Division', function(Division) {
                            return Division.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('division.new', {
            parent: 'division',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/division/division-dialog.html',
                    controller: 'DivisionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                address: null,
                                city: null,
                                province: null,
                                country: null,
                                zipcode: null,
                                tenantId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('division', null, { reload: 'division' });
                }, function() {
                    $state.go('division');
                });
            }]
        })
        .state('division.edit', {
            parent: 'division',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/division/division-dialog.html',
                    controller: 'DivisionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Division', function(Division) {
                            return Division.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('division', null, { reload: 'division' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('division.delete', {
            parent: 'division',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/division/division-delete-dialog.html',
                    controller: 'DivisionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Division', function(Division) {
                            return Division.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('division', null, { reload: 'division' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
