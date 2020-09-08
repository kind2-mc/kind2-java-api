# Java API for Kind 2 ![Java CI with Gradle](https://github.com/kind2-mc/kind2-java-api/workflows/Java%20CI%20with%20Gradle/badge.svg?branch=master)
An API that constructs lustre programs, runs Kind 2, and shows results/suggestions.

### Build instructions

```shell
git clone https://github.com/kind2-mc/kind2-java-api.git
cd kind2-java-api
./gradlew build
```
To import the API, use the jar file `build/libs/kind2-java-api.jar`. 
Alternatively you can just copy the package `edu.uiowa.kind2` to your source code.

### API usage
An example of how to use the API is provided in `src/main/java/Main.java`. Follow these steps when using the API:
1. Add `build/libs/kind2-java-api.jar` to your java class path.
2. Import package `edu.uiowa.kind2`.
3. Construct a lustre `program` object.
4. Implement the `IProgressMonitor` interface.
5. Construct a `Kind2Api` object and call `Kind2Api.execute`.
6. Analyze the results.

### Results
`Kind2Result` contains the following features:
- `Kind2Result` features:
  - `getValidProperties`, `getFalsifiedProperties`, and `getUnknownProperties` return properties for all components.
  - `getNodeResult` returns an object of `Kind2NodeResult` that summarizes all analyses done by kind2 for a given component.

- `Kind2NodeResult` features:
  - `getSuggestions` returns an object of `Kind2Suggestion` that provides explanations and suggestions based on kind2 analyses for the current component.
  - `getValidProperties`, `getFalsifiedProperties`, and `getUnknownProperties` return properties for the current component, and all its subcomponents.

- `Kind2Suggestion` contains explanations and a suggestion for the associated component. If `N` is the current component, and `M` is possibly a subcomponent of `N`, then the suggestion is one of the following:
  - `noActionRequired`: no action required because all components of the system satisfy their contracts, and no component of the system was refined.
  - `strengthenSubComponentContract`: fix `M`s contract because `N` is correct after refinement, but `M`'s contract is too weak to prove `N`'s contract, but `M`'s definition is strong enough.
  - `completeSpecificationOrRemoveComponent`: Either complete specification of `N`'s contract, or remove component `M`, because component `N` satisfies its current contract and one or more assumptions of `M` are not satisfied by `N`.
  - `makeWeakerOrFixDefinition`: either make assumption `A` weaker, or fix `N`'s definition to satisfy `A`, because component `N` doesn't satisfy its contract after refinement, and assumption `A` of `M` is not satisfied by `N`.
  - `makeAssumptionStrongerOrFixDefinition`: Either make `N`'s assumptions stronger, or fix `N`'s definition to satisfy `N`'s guarantees, because component `N` doesn't satisfy its contract after refinement, and either `N` has no subcomponents, or all its subcomponents satisfy their contract.
  - `fixSubComponentIssues`: fix reported issues for `N`'s subcomponents, because component `N` doesn't satisfy its contract after refinement, and One or more subcomponents of N don't satisfy their contract.
  - `fixOneModeActive`: define all modes of component `N`, because kind2 found a state that is not covered by any of the modes in `N`'s contract.
  - `increaseTimeout`: increase the timeout for kind2, because it fails to prove or disprove one of the properties with the previous timeout.

### Credits
This project barrows heavily from [loonworks/jkind](https://github.com/loonwerks/jkind) by Rockwell Collins.
