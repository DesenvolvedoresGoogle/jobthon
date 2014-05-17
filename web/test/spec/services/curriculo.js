'use strict';

describe('Service: Curriculo', function () {

  // load the service's module
  beforeEach(module('appApp'));

  // instantiate service
  var Curriculo;
  beforeEach(inject(function (_Curriculo_) {
    Curriculo = _Curriculo_;
  }));

  it('should do something', function () {
    expect(!!Curriculo).toBe(true);
  });

});
