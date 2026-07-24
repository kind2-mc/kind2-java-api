package edu.uiowa.cs.clc.kind2.api;

import edu.uiowa.cs.clc.kind2.results.Result;

/**
 * Receives {@link edu.uiowa.cs.clc.kind2.results.Result} updates while Kind 2 is still running.
 */
@FunctionalInterface
public interface ResultListener {

    /**
     * Called each time Kind 2 produces more output.
     *
     * @param result the result, updated with everything read so far
     */
    void onUpdate(Result result);
    
}
