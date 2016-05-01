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
            .state('project-detail', {
                parent: 'entity',
                url: '/project/{prjId}',
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
                        return Project.getPrj({prjId: $stateParams.prjId});
                    }]
                }
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
            .state('diagram-editor.edit', {
                parent: 'project',
                url: '/{prjId}/diagrams/{diagId}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/diagram/diagram-editor.html',
                        controller: 'DiagramEditorController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Diagram', function($stateParams, Diagram) {
                        console.log('yossssssr  '+$stateParams.diagId);
                        return Diagram.get({diagId : $stateParams.diagId}).$promise;
                    }]
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
                url: '/{prjId}/edit',
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
                                return Project.getPrj({prjId: $stateParams.prjId});
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
                url: '/{prjId}/delete',
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
                                return Project.getPrj({prjId: $stateParams.prjId});
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
