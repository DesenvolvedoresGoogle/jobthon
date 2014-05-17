'use strict';

describe('Service: Vaga', function () {

  // load the service's module
  beforeEach(module('appApp'));

  // instantiate service
  var Vaga;
  beforeEach(inject(function (_Vaga_) {
    Vaga = _Vaga_;
  }));

  it('should do something', function () {
    expect(!!Vaga).toBe(true);
  });

});
