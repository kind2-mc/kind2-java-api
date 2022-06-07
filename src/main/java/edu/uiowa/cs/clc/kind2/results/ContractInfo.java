/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.results;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Kind2 Contract Info.
 */
public class ContractInfo implements AstInfo {
    /**
     * The original kind2 output for this object in pretty json format.
     */
    private final String prettyJson;
    /**
     * The original kind2 output for this object in json format
     */
    private final String json;
    /**
     * The component name.
     */
    private final String name;
    /**
     * Associated file, if any.
     */
    private final String file;
    /**
     * Associated line in the input file, if any.
     */
    private final String startLine;
    /**
     * Associated column in the input file, if any.
     */
    private final String startColumn;
    /**
     * Associated line in the input file, if any.
     */
    private final String endLine;
    /**
     * Associated column in the input file, if any.
     */
    private final String endColumn;

    public ContractInfo(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        prettyJson = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        json = new GsonBuilder().create().toJson(jsonElement);
        this.name = jsonObject.get(Labels.name).getAsString();
        this.file = jsonObject.get(Labels.file) == null ? null
                : jsonObject.get(Labels.file).getAsString();
        this.startLine = jsonObject.get(Labels.startLine) == null ? null
                : jsonObject.get(Labels.startLine).getAsString();
        this.startColumn = jsonObject.get(Labels.startColumn) == null ? null
                : jsonObject.get(Labels.startColumn).getAsString();
        this.endLine = jsonObject.get(Labels.endLine) == null ? null
                : jsonObject.get(Labels.endLine).getAsString();
        this.endColumn = jsonObject.get(Labels.endColumn) == null ? null
                : jsonObject.get(Labels.endColumn).getAsString();
    }

    /**
     * @return The original kind2 output for this object in pretty json format.
     */
    public String getJson() {
        return prettyJson;
    }

    /**
     * @return The component name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the uri of the file, if any.
     */
    public String getFile() {
        return file;
    }

    /**
     * @return the associated start line in the input file, if any.
     */
    public String getStartLine() {
        return startLine;
    }

    /**
     * @return the associated start column in the input file, if any.
     */
    public String getStartColumn() {
        return startColumn;
    }

    /**
     * @return the associated end line in the input file, if any.
     */
    public String getEndLine() {
        return endLine;
    }

    /**
     * @return the associated end column in the input file, if any.
     */
    public String getEndColumn() {
        return endColumn;
    }

    @Override
    public String toString() {
        return json;
    }
}
