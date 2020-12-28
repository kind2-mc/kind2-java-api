/*
 * Copyright (c) 2020, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.kind2.results;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Kind2 output log.
 */
public class AstInfo {
    /**
     * Associated kind2Result object
     */
    private final Kind2Result kind2Result;
    /**
     * The original kind2 output for this object in pretty json format.
     */
    private final String prettyJson;
    /**
     * The original kind2 output for this object in json format
     */
    private final String json;
    /**
     * The name of the Kind 2 module which wrote the log.
     */
    private final String source;
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
    private final String line;
    /**
     * Associated column in the input file, if any.
     */
    private final String column;
    /**
     * isHidden determines whether the current log is printed.
     */
    private boolean isHidden;

    public AstInfo(Kind2Result kind2Result, JsonElement jsonElement) {
        this.kind2Result = kind2Result;
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        prettyJson = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        json = new GsonBuilder().create().toJson(jsonElement);
        this.source = jsonObject.get(Kind2Labels.source).getAsString();
        this.name = jsonObject.get(Kind2Labels.name).getAsString();
        this.file = jsonObject.get(Kind2Labels.file).getAsString();
        this.line = jsonObject.get(Kind2Labels.line) == null ? null
                : jsonObject.get(Kind2Labels.line).getAsString();
        this.column = jsonObject.get(Kind2Labels.column) == null ? null
                : jsonObject.get(Kind2Labels.column).getAsString();
    }

    /**
     * @return the associated kind2 result for this log.
     */
    public Kind2Result getKind2Result() {
        return kind2Result;
    }

    /**
     * @return The original kind2 output for this object in pretty json format.
     */
    public String getJson() {
        return prettyJson;
    }

    /**
     * @return the associated line in the input file, if any.
     */
    public String getLine() {
        return line;
    }

    /**
     * @return The name of the Kind 2 module which wrote the log.
     */
    public String getSource() {
        return source;
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
     * @return the associated column in the input file, if any.
     */
    public String getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return json;
    }

    /**
     * @return a boolean that determines whether the current log is printed.
     */
    public boolean isHidden() {
        return isHidden;
    }
}
