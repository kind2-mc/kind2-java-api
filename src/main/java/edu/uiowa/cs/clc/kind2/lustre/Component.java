/*
 * Copyright (c) 2012-2013, Rockwell Collins
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.lustre;

import java.util.List;
import edu.uiowa.cs.clc.kind2.util.Util;

class Component extends ImportedComponent {
  final List<Constant> localConsts;
  final List<VarDecl> localVars;
  final List<Equation> equations;
  final List<Property> properties;
  final List<Expr> assertions;

  Component(String id, List<Parameter> inputs, List<Parameter> outputs, ContractBody contractBody,
      List<Constant> localConsts, List<VarDecl> localVars, List<Equation> equations,
      List<Expr> assertions, List<Property> properties) {
    super(id, inputs, outputs, contractBody);
    this.localConsts = Util.safeList(localConsts);
    this.localVars = Util.safeList(localVars);
    this.equations = Util.safeList(equations);
    this.assertions = Util.safeList(assertions);
    this.properties = Util.safeList(properties);
  }
}
