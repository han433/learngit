(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('summary-report', {
            parent: 'entity',
            url: '/summary-report',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.summaryReport.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/summary-report/summary-reports.html',
                    controller: 'SummaryReportController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('summaryReport');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('summary-report-detail', {
            parent: 'summary-report',
            url: '/summary-report/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.summaryReport.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/summary-report/summary-report-detail.html',
                    controller: 'SummaryReportDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('summaryReport');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SummaryReport', function($stateParams, SummaryReport) {
                    return SummaryReport.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'summary-report',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('summary-report-detail.edit', {
            parent: 'summary-report-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/summary-report/summary-report-dialog.html',
                    controller: 'SummaryReportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SummaryReport', function(SummaryReport) {
                            return SummaryReport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('summary-report.new', {
            parent: 'summary-report',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/summary-report/summary-report-dialog.html',
                    controller: 'SummaryReportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('summary-report', null, { reload: 'summary-report' });
                }, function() {
                    $state.go('summary-report');
                });
            }]
        })
        .state('summary-report.edit', {
            parent: 'summary-report',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/summary-report/summary-report-dialog.html',
                    controller: 'SummaryReportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SummaryReport', function(SummaryReport) {
                            return SummaryReport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('summary-report', null, { reload: 'summary-report' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('summary-report.delete', {
            parent: 'summary-report',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/summary-report/summary-report-delete-dialog.html',
                    controller: 'SummaryReportDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SummaryReport', function(SummaryReport) {
                            return SummaryReport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('summary-report', null, { reload: 'summary-report' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
