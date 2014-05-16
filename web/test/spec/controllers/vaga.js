'use strict';

describe('Controller: VagaCtrl', function () {

  // load the controller's module
  beforeEach(module('appApp'));

  var VagaCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    VagaCtrl = $controller('VagaCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
