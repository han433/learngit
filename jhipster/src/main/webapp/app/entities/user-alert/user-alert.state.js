(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-alert', {
            parent: 'entity',
            url: '/user-alert',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.userAlert.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-alert/user-alerts.html',
                    controller: 'UserAlertController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userAlert');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-alert-detail', {
            parent: 'user-alert',
            url: '/user-alert/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.userAlert.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-alert/user-alert-detail.html',
                    controller: 'UserAlertDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userAlert');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserAlert', function($stateParams, UserAlert) {
                    return UserAlert.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-alert',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-alert-detail.edit', {
            parent: 'user-alert-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-alert/user-alert-dialog.html',
                    controller: 'UserAlertDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserAlert', function(UserAlert) {
                            return UserAlert.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-alert.new', {
            parent: 'user-alert',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-alert/user-alert-dialog.html',
                    controller: 'UserAlertDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-alert', null, { reload: 'user-alert' });
                }, function() {
                    $state.go('user-alert');
                });
            }]
        })
        .state('user-alert.edit', {
            parent: 'user-alert',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-alert/user-alert-dialog.html',
                    controller: 'UserAlertDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserAlert', function(UserAlert) {
                            return UserAlert.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-alert', null, { reload: 'user-alert' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-alert.delete', {
            parent: 'user-alert',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-alert/user-alert-delete-dialog.html',
                    controller: 'UserAlertDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserAlert', function(UserAlert) {
                            return UserAlert.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-alert', null, { reload: 'user-alert' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
