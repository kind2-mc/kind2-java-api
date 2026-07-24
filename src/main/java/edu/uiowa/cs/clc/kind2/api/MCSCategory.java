/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

/**
 * The categories of model elements that may appear in a minimal cut set.
 */
public enum MCSCategory {
    /**
     * Calls to other components.
     */
    NODE_CALLS,
    /**
     * Contract items such as assumptions and guarantees.
     */
    CONTRACTS,
    /**
     * Equations defining variables.
     */
    EQUATIONS,
    /**
     * Assertions stated in the component.
     */
    ASSERTIONS,
    /**
     * Annotations such as the main annotation.
     */
    ANNOTATIONS;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
