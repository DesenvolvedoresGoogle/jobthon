'use strict';

angular.module('appApp')
    .service('Empresa', ['Restangular',
        function Empresa(Restangular) {
            // AngularJS will instantiate a singleton by calling "new" on this function
            return {
                get: function(id) {
                    return Restangular.one('empresa', id).get();
                },
                getList: function() {
                    return Restangular.all('empresas').getList();
                },
                save: function(empresa) {
                    if (empresa.id >= 0) {
                        Restangular.one('empresa', empresa.id).customPUT(empresa)
                    } else {
                        Restangular.all('empresas').post(empresa);
                    }
                }
            }
        }
    ]);