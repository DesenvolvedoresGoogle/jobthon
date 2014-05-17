'use strict';

angular
    .module('appApp', [
        'ngCookies',
        'ngResource',
        'ngSanitize',
        'ngRoute',
        'restangular',
        'ui.router'
    ])
    .config(['$stateProvider', '$urlRouterProvider', 'RestangularProvider',
        function($stateProvider, $urlRouterProvider, RestangularProvider) {
            RestangularProvider.setBaseUrl('http://gdgjobthom.appspot.com/');
            RestangularProvider.addResponseInterceptor(function(element, operation) {
                var elementArray = [];
                if (operation === 'getList') {
                    if (element === "null") {
                        element = [];
                    }
                }
                return element;
            })
            //
            // For any unmatched url, redirect to /state1
            $urlRouterProvider.otherwise("/");

            // Now set up the states
            $stateProvider
                .state('main', {
                    url: "/",
                    templateUrl: "views/main.html"
                })
                .state('main.home', {
                    url: "/",
                    templateUrl: "views/home.html"
                })
                .state('main.vagas', {
                    url: "vagas",
                    templateUrl: "views/vagas/vagas.html",
                    controller: 'VagaCtrl'
                })
                .state('main.vagasanalise', {
                    url: "vagas/:id/analises",
                    templateUrl: "views/vagas/vagas.analise.html",
                    controller: 'AnalisevagaCtrl'
                })
                .state('main.curriculoanalise', {
                    url: "curriculo/:email/analises",
                    templateUrl: "views/curriculo/curriculo.analise.html",
                    controller: 'AnalisecurriculoCtrl'
                })
                .state('main.curriculo', {
                    url: "curriculo",
                    templateUrl: "views/curriculo/curriculo.html",
                    controller: 'CurriculoCtrl'
                })
                .state('main.empresas', {
                    url: "empresas",
                    templateUrl: "views/empresa/empresas.html",
                    controller: 'EmpresaCtrl'
                })
        }
    ]).run(['$rootScope', 'Estados',
        function($rootScope, Estados) {
            $rootScope.Estados = Estados;
        }
    ]);