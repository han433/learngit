(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('credit-transaction', {
            parent: 'entity',
            url: '/credit-transaction',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.creditTransaction.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/credit-transaction/credit-transactions.html',
                    controller: 'CreditTransactionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('creditTransaction');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('credit-transaction-detail', {
            parent: 'credit-transaction',
            url: '/credit-transaction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.creditTransaction.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/credit-transaction/credit-transaction-detail.html',
                    controller: 'CreditTransactionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('creditTransaction');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CreditTransaction', function($stateParams, CreditTransaction) {
                    return CreditTransaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'credit-transaction',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('credit-transaction-detail.edit', {
            parent: 'credit-transaction-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/credit-transaction/credit-transaction-dialog.html',
                    controller: 'CreditTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CreditTransaction', function(CreditTransaction) {
                            return CreditTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('credit-transaction.new', {
            parent: 'credit-transaction',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/credit-transaction/credit-transaction-dialog.html',
                    controller: 'CreditTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                tenantId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('credit-transaction', null, { reload: 'credit-transaction' });
                }, function() {
                    $state.go('credit-transaction');
                });
            }]
        })
        .state('credit-transaction.edit', {
            parent: 'credit-transaction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/credit-transaction/credit-transaction-dialog.html',
                    controller: 'CreditTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CreditTransaction', function(CreditTransaction) {
                            return CreditTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('credit-transaction', null, { reload: 'credit-transaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('credit-transaction.delete', {
            parent: 'credit-transaction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/credit-transaction/credit-transaction-delete-dialog.html',
                    controller: 'CreditTransactionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CreditTransaction', function(CreditTransaction) {
                            return CreditTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('credit-transaction', null, { reload: 'credit-transaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
