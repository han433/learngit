(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-credit', {
            parent: 'entity',
            url: '/user-credit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.userCredit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-credit/user-credits.html',
                    controller: 'UserCreditController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userCredit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-credit-detail', {
            parent: 'user-credit',
            url: '/user-credit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.userCredit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-credit/user-credit-detail.html',
                    controller: 'UserCreditDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userCredit');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserCredit', function($stateParams, UserCredit) {
                    return UserCredit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-credit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-credit-detail.edit', {
            parent: 'user-credit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-credit/user-credit-dialog.html',
                    controller: 'UserCreditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserCredit', function(UserCredit) {
                            return UserCredit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-credit.new', {
            parent: 'user-credit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-credit/user-credit-dialog.html',
                    controller: 'UserCreditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                credit: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-credit', null, { reload: 'user-credit' });
                }, function() {
                    $state.go('user-credit');
                });
            }]
        })
        .state('user-credit.edit', {
            parent: 'user-credit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-credit/user-credit-dialog.html',
                    controller: 'UserCreditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserCredit', function(UserCredit) {
                            return UserCredit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-credit', null, { reload: 'user-credit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-credit.delete', {
            parent: 'user-credit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-credit/user-credit-delete-dialog.html',
                    controller: 'UserCreditDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserCredit', function(UserCredit) {
                            return UserCredit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-credit', null, { reload: 'user-credit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
