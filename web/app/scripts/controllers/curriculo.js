'use strict';

angular.module('appApp')
    .controller('CurriculoCtrl', ['$scope', 'Curriculo',
        function($scope, Curriculo) {
            Curriculo.getList().then(function(data) {
                $scope.Curriculos = data;
            });
        }
    ]);