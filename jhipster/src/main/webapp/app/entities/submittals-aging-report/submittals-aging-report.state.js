(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('submittals-aging-report', {
            parent: 'entity',
            url: '/submittals-aging-report',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.submittalsAgingReport.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/submittals-aging-report/submittals-aging-reports.html',
                    controller: 'SubmittalsAgingReportController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('submittalsAgingReport');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('submittals-aging-report-detail', {
            parent: 'submittals-aging-report',
            url: '/submittals-aging-report/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.submittalsAgingReport.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/submittals-aging-report/submittals-aging-report-detail.html',
                    controller: 'SubmittalsAgingReportDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('submittalsAgingReport');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SubmittalsAgingReport', function($stateParams, SubmittalsAgingReport) {
                    return SubmittalsAgingReport.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'submittals-aging-report',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('submittals-aging-report-detail.edit', {
            parent: 'submittals-aging-report-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/submittals-aging-report/submittals-aging-report-dialog.html',
                    controller: 'SubmittalsAgingReportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SubmittalsAgingReport', function(SubmittalsAgingReport) {
                            return SubmittalsAgingReport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('submittals-aging-report.new', {
            parent: 'submittals-aging-report',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/submittals-aging-report/submittals-aging-report-dialog.html',
                    controller: 'SubmittalsAgingReportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                company: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('submittals-aging-report', null, { reload: 'submittals-aging-report' });
                }, function() {
                    $state.go('submittals-aging-report');
                });
            }]
        })
        .state('submittals-aging-report.edit', {
            parent: 'submittals-aging-report',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/submittals-aging-report/submittals-aging-report-dialog.html',
                    controller: 'SubmittalsAgingReportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SubmittalsAgingReport', function(SubmittalsAgingReport) {
                            return SubmittalsAgingReport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('submittals-aging-report', null, { reload: 'submittals-aging-report' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('submittals-aging-report.delete', {
            parent: 'submittals-aging-report',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/submittals-aging-report/submittals-aging-report-delete-dialog.html',
                    controller: 'SubmittalsAgingReportDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SubmittalsAgingReport', function(SubmittalsAgingReport) {
                            return SubmittalsAgingReport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('submittals-aging-report', null, { reload: 'submittals-aging-report' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
