(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('apn-param', {
            parent: 'entity',
            url: '/apn-param',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.apnParam.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/apn-param/apn-params.html',
                    controller: 'ApnParamController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('apnParam');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('apn-param-detail', {
            parent: 'apn-param',
            url: '/apn-param/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.apnParam.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/apn-param/apn-param-detail.html',
                    controller: 'ApnParamDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('apnParam');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ApnParam', function($stateParams, ApnParam) {
                    return ApnParam.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'apn-param',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('apn-param-detail.edit', {
            parent: 'apn-param-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apn-param/apn-param-dialog.html',
                    controller: 'ApnParamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApnParam', function(ApnParam) {
                            return ApnParam.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('apn-param.new', {
            parent: 'apn-param',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apn-param/apn-param-dialog.html',
                    controller: 'ApnParamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tenantId: null,
                                paramName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('apn-param', null, { reload: 'apn-param' });
                }, function() {
                    $state.go('apn-param');
                });
            }]
        })
        .state('apn-param.edit', {
            parent: 'apn-param',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apn-param/apn-param-dialog.html',
                    controller: 'ApnParamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApnParam', function(ApnParam) {
                            return ApnParam.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('apn-param', null, { reload: 'apn-param' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('apn-param.delete', {
            parent: 'apn-param',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apn-param/apn-param-delete-dialog.html',
                    controller: 'ApnParamDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ApnParam', function(ApnParam) {
                            return ApnParam.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('apn-param', null, { reload: 'apn-param' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
