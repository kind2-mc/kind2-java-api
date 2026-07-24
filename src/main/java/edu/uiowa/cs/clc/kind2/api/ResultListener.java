package edu.uiowa.cs.clc.kind2.api;

import edu.uiowa.cs.clc.kind2.results.Result;

@FunctionalInterface
public interface ResultListener {

    void onUpdate(Result result);
    
}
