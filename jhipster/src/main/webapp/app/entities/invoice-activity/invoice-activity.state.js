(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('invoice-activity', {
            parent: 'entity',
            url: '/invoice-activity',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.invoiceActivity.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invoice-activity/invoice-activities.html',
                    controller: 'InvoiceActivityController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('invoiceActivity');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('invoice-activity-detail', {
            parent: 'invoice-activity',
            url: '/invoice-activity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.invoiceActivity.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invoice-activity/invoice-activity-detail.html',
                    controller: 'InvoiceActivityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('invoiceActivity');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InvoiceActivity', function($stateParams, InvoiceActivity) {
                    return InvoiceActivity.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'invoice-activity',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('invoice-activity-detail.edit', {
            parent: 'invoice-activity-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-activity/invoice-activity-dialog.html',
                    controller: 'InvoiceActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InvoiceActivity', function(InvoiceActivity) {
                            return InvoiceActivity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('invoice-activity.new', {
            parent: 'invoice-activity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-activity/invoice-activity-dialog.html',
                    controller: 'InvoiceActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                subInvoiceNo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('invoice-activity', null, { reload: 'invoice-activity' });
                }, function() {
                    $state.go('invoice-activity');
                });
            }]
        })
        .state('invoice-activity.edit', {
            parent: 'invoice-activity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-activity/invoice-activity-dialog.html',
                    controller: 'InvoiceActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InvoiceActivity', function(InvoiceActivity) {
                            return InvoiceActivity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('invoice-activity', null, { reload: 'invoice-activity' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('invoice-activity.delete', {
            parent: 'invoice-activity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-activity/invoice-activity-delete-dialog.html',
                    controller: 'InvoiceActivityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InvoiceActivity', function(InvoiceActivity) {
                            return InvoiceActivity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('invoice-activity', null, { reload: 'invoice-activity' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
