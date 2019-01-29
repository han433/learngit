(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('application-commission', {
            parent: 'entity',
            url: '/application-commission',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.application_commission.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-commission/application-commissions.html',
                    controller: 'Application_commissionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('application_commission');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('application-commission-detail', {
            parent: 'application-commission',
            url: '/application-commission/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.application_commission.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-commission/application-commission-detail.html',
                    controller: 'Application_commissionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('application_commission');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Application_commission', function($stateParams, Application_commission) {
                    return Application_commission.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'application-commission',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('application-commission-detail.edit', {
            parent: 'application-commission-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-commission/application-commission-dialog.html',
                    controller: 'Application_commissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Application_commission', function(Application_commission) {
                            return Application_commission.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-commission.new', {
            parent: 'application-commission',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-commission/application-commission-dialog.html',
                    controller: 'Application_commissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                application_id: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('application-commission', null, { reload: 'application-commission' });
                }, function() {
                    $state.go('application-commission');
                });
            }]
        })
        .state('application-commission.edit', {
            parent: 'application-commission',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-commission/application-commission-dialog.html',
                    controller: 'Application_commissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Application_commission', function(Application_commission) {
                            return Application_commission.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-commission', null, { reload: 'application-commission' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-commission.delete', {
            parent: 'application-commission',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-commission/application-commission-delete-dialog.html',
                    controller: 'Application_commissionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Application_commission', function(Application_commission) {
                            return Application_commission.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-commission', null, { reload: 'application-commission' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
