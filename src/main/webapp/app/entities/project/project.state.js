(function () {
    'use strict';

    angular
        .module('demoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('project', {
                parent: 'entity',
                url: '/project',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Projects'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/project/projects.html',
                        controller: 'ProjectController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {}
            })
            .state('project-diagram', {
                parent: 'entity',
                url: '/project/{prjId}/diagrams',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Diagram'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/diagram/diagrams.html',
                        controller: 'ProjectDiagramsController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    diagrams: ['$stateParams', 'Project', function ($stateParams, Project) {
                        return Project.diagrams({prjId: $stateParams.prjId});
                    }]
                }
            })
            .state('project-detail', {
                parent: 'entity',
                url: '/project/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Project'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/project/project-detail.html',
                        controller: 'ProjectDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Project', function ($stateParams, Project) {
                        return Project.get({id: $stateParams.id});
                    }]
                }
            })
            .state('diagram-editor', {
                parent: 'entity',
                url: '/project/{prjId}/diagrams/diagram-editor',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Diagram'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/diagram/diagram-editor.html',
                        controller: 'DiagramEditorController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            name: null,
                            visibility: null,
                            content: null,
                            validation: null,
                            id: null
                        };
                    }
                }
            })
            .state('project.new', {
                parent: 'project',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/project/project-dialog.html',
                        controller: 'ProjectDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    creation_date: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('project', null, {reload: true});
                    }, function () {
                        $state.go('project');
                    });
                }]
            })
            .state('project.edit', {
                parent: 'project',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/project/project-dialog.html',
                        controller: 'ProjectDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Project', function (Project) {
                                return Project.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function () {
                        $state.go('project', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('project.delete', {
                parent: 'project',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/project/project-delete-dialog.html',
                        controller: 'ProjectDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Project', function (Project) {
                                return Project.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function () {
                        $state.go('project', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
