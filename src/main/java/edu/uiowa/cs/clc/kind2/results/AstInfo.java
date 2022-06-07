/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

/**
 * Kind2 AST Info interface.
 */
public interface AstInfo {
    /**
     * @return The original kind2 output for this object in pretty json format.
     */
    public String getJson();

    /**
     * @return The component name.
     */
    public String getName();

    /**
     * @return the uri of the file, if any.
     */
    public String getFile();

    /**
     * @return the associated start line in the input file, if any.
     */
    public String getStartLine();

    /**
     * @return the associated start column in the input file, if any.
     */
    public String getStartColumn();

    /**
     * @return the associated end line in the input file, if any.
     */
    public String getEndLine();

    /**
     * @return the associated end column in the input file, if any.
     */
    public String getEndColumn();

    @Override
    public String toString();
}
