'use strict';

describe('Controller: AnalisevagaCtrl', function () {

  // load the controller's module
  beforeEach(module('appApp'));

  var AnalisevagaCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AnalisevagaCtrl = $controller('AnalisevagaCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
