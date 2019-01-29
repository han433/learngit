(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('activity-report', {
            parent: 'entity',
            url: '/activity-report',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.activityReport.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activity-report/activity-reports.html',
                    controller: 'ActivityReportController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('activityReport');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('activity-report-detail', {
            parent: 'activity-report',
            url: '/activity-report/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.activityReport.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activity-report/activity-report-detail.html',
                    controller: 'ActivityReportDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('activityReport');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ActivityReport', function($stateParams, ActivityReport) {
                    return ActivityReport.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'activity-report',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('activity-report-detail.edit', {
            parent: 'activity-report-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-report/activity-report-dialog.html',
                    controller: 'ActivityReportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActivityReport', function(ActivityReport) {
                            return ActivityReport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('activity-report.new', {
            parent: 'activity-report',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-report/activity-report-dialog.html',
                    controller: 'ActivityReportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('activity-report', null, { reload: 'activity-report' });
                }, function() {
                    $state.go('activity-report');
                });
            }]
        })
        .state('activity-report.edit', {
            parent: 'activity-report',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-report/activity-report-dialog.html',
                    controller: 'ActivityReportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActivityReport', function(ActivityReport) {
                            return ActivityReport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('activity-report', null, { reload: 'activity-report' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('activity-report.delete', {
            parent: 'activity-report',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-report/activity-report-delete-dialog.html',
                    controller: 'ActivityReportDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ActivityReport', function(ActivityReport) {
                            return ActivityReport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('activity-report', null, { reload: 'activity-report' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
