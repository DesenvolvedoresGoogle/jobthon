'use strict';

describe('Service: Empregador', function () {

  // load the service's module
  beforeEach(module('appApp'));

  // instantiate service
  var Empregador;
  beforeEach(inject(function (_Empregador_) {
    Empregador = _Empregador_;
  }));

  it('should do something', function () {
    expect(!!Empregador).toBe(true);
  });

});
