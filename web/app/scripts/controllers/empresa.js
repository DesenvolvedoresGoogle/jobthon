'use strict';

angular.module('appApp')
    .controller('EmpresaCtrl', ['$scope', 'Empresa',
        function($scope, Empresa) {
            Empresa.getList().then(function(data) {
                $scope.Empresas = data;
            });

            $scope.editEmpresas = function(empresa) {
                $scope.editEmpresa = empresa;
            }
        }
    ]);