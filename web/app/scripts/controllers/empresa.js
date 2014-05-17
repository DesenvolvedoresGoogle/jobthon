'use strict';

angular.module('appApp')
    .controller('EmpresaCtrl', ['$scope', '$state', 'Empresa',
        function($scope, $state, Empresa) {
            function init() {
                Empresa.getList().then(function(data) {
                    $scope.Empresas = data;
                });
            }
            init();

            $scope.editEmpresas = function(empresa) {
                $scope.editEmpresa = angular.copy(empresa);
            }

            $scope.addEmpresa = function() {
                $scope.Empresas.push($scope.newEmpresa);
                Empresa.add($scope.newEmpresa);
            };

            $scope.saveEditEmpresa = function() {
                Empresa.add($scope.editEmpresa);
                init();
            };

        }
    ]);