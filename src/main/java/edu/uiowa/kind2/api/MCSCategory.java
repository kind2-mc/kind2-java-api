/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.api;

public enum MCSCategory {
    NODE_CALLS, CONTRACTS, EQUATIONS, ASSERTIONS, ANNOTATIONS;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
