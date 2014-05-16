'use strict';

describe('Service: Estados', function () {

  // load the service's module
  beforeEach(module('appApp'));

  // instantiate service
  var Estados;
  beforeEach(inject(function (_Estados_) {
    Estados = _Estados_;
  }));

  it('should do something', function () {
    expect(!!Estados).toBe(true);
  });

});
