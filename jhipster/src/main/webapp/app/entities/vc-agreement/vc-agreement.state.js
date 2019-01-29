(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vc-agreement', {
            parent: 'entity',
            url: '/vc-agreement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.vc_agreement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vc-agreement/vc-agreements.html',
                    controller: 'Vc_agreementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vc_agreement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('vc-agreement-detail', {
            parent: 'vc-agreement',
            url: '/vc-agreement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.vc_agreement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vc-agreement/vc-agreement-detail.html',
                    controller: 'Vc_agreementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vc_agreement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Vc_agreement', function($stateParams, Vc_agreement) {
                    return Vc_agreement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'vc-agreement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('vc-agreement-detail.edit', {
            parent: 'vc-agreement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vc-agreement/vc-agreement-dialog.html',
                    controller: 'Vc_agreementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vc_agreement', function(Vc_agreement) {
                            return Vc_agreement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vc-agreement.new', {
            parent: 'vc-agreement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vc-agreement/vc-agreement-dialog.html',
                    controller: 'Vc_agreementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sign_date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vc-agreement', null, { reload: 'vc-agreement' });
                }, function() {
                    $state.go('vc-agreement');
                });
            }]
        })
        .state('vc-agreement.edit', {
            parent: 'vc-agreement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vc-agreement/vc-agreement-dialog.html',
                    controller: 'Vc_agreementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vc_agreement', function(Vc_agreement) {
                            return Vc_agreement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vc-agreement', null, { reload: 'vc-agreement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vc-agreement.delete', {
            parent: 'vc-agreement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vc-agreement/vc-agreement-delete-dialog.html',
                    controller: 'Vc_agreementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Vc_agreement', function(Vc_agreement) {
                            return Vc_agreement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vc-agreement', null, { reload: 'vc-agreement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
