'use strict';

angular.module('appApp')
    .factory('VagaService', ['Restangular',
        function Vaga(Restangular) {
            // AngularJS will instantiate a singleton by calling "new" on this function
            return {
                get: function(id) {
                    return Restangular.one('vagas', id).get();
                },
                getList: function() {
                    return Restangular.all('vagas').getList();
                },
                add: function(vaga) {
                    return Restangular.all('vaga').post(vaga);

                }
            }

        }
    ]);