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
                save: function(vaga) {
                    if (vaga.id >= 0) {
                        Restangular.one('vagas', vaga.id).customPUT(vaga)
                    } else {
                        Restangular.all('vagas').post(vaga);
                    }
                }
            }

        }
    ]);