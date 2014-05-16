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

            //
            // For any unmatched url, redirect to /state1
            $urlRouterProvider.otherwise("/");

            // Now set up the states
            $stateProvider
                .state('main', {
                    url: "/",
                    templateUrl: "views/main.html"
                })
                .state('main.vagas', {
                    url: "vagas",
                    templateUrl: "views/vagas/vagas.html",
                    controller: 'VagaCtrl'
                })
                .state('main.vagascurriculo', {
                    url: "vagas/:id/curriculo",
                    templateUrl: "views/vagas/vagas.curriculo.html",
                    controller: 'VagaCtrl'
                })
                .state('main.curriculo', {
                    url: "curriculo",
                    templateUrl: "views/curriculo/curriculo.html",
                    controller: function($scope) {
                        $scope.items = ["A", "List", "Of", "Items"];
                    }
                })
        }
    ]);