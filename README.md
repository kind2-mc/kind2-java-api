[![License](https://img.shields.io/github/license/kind2-mc/kind2-java-api)](https://github.com/kind2-mc/kind2-java-api/blob/master/LICENSE)
[![CI](https://github.com/kind2-mc/kind2-java-api/workflows/Java%20CI%20with%20Gradle/badge.svg?branch=master)](https://github.com/kind2-mc/kind2-java-api/actions?query=workflow%3A%22Java+CI+with+Gradle%22)
[![Maven Central](https://img.shields.io/maven-central/v/edu.uiowa.cs.clc/kind2-java-api.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22edu.uiowa.cs.clc%22%20AND%20a:%22kind2-java-api%22)

# Java API for Kind 2
An API for constructing Lustre programs, running Kind 2, and showing results/suggestions.

## Install Instructions
There are three ways to install the Java API artifact for Kind 2 for use in your development environment. The instructions for each are listed below:

### Install artifact from Maven Central
#### Maven
Add the following dependency tag to your `pom.xml` file:
```xml
<dependency>
  <groupId>edu.uiowa.cs.clc</groupId>
  <artifactId>kind2-java-api</artifactId>
  <version>0.2.2</version>
</dependency>
```

#### Gradle
Add the following dependency DSL to your `build.gradle` file:
```groovy
implementation 'edu.uiowa.cs.clc:kind2-java-api:0.2.2'
```

#### Other build management tools
Go to [Maven Central](https://search.maven.org/artifact/edu.uiowa.cs.clc/kind2-java-api/0.2.2/jar) and copy the appropriate code snippet for your build management tool.

### Install artifact from Github Packages
#### Maven
1. Authenticate to GitHub Packages. For more information, see "[Authenticating to GitHub Packages](https://docs.github.com/articles/configuring-apache-maven-for-use-with-github-package-registry/#authenticating-to-github-packages)."
2. Add the following repository tag to your `pom.xml` file:
  ```xml
  <repository>
    <id>github</id>
    <name>GitHub kind2-mc Apache Maven Packages</name>
    <url>https://maven.pkg.github.com/kind2-mc/kind2-java-api</url>
  </repository>
  ```
3. Add the following dependency tag to your `pom.xml` file:
  ```xml
  <dependency>
    <groupId>edu.uiowa.cs.clc</groupId>
    <artifactId>kind2-java-api</artifactId>
    <version>0.2.2</version>
  </dependency>
  ```

#### Gradle
1. Authenticate to GitHub Packages. For more information, see "[Authenticating to GitHub Packages](https://docs.github.com/articles/configuring-gradle-for-use-with-github-package-registry/#authenticating-to-github-packages)."
2. Add the following repository DSL to your `build.gradle` file:
  ```groovy
  maven {
    name = "GitHubPackages"
    url = uri("https://maven.pkg.github.com/kind2-mc/kind2-java-api")
  }
  ```
3. Add the following dependency DSL to your `build.gradle` file:
  ```groovy
  implementation 'edu.uiowa.cs.clc:kind2-java-api:0.2.2'
  ```

### Build from sources
```shell
git clone https://github.com/kind2-mc/kind2-java-api.git
cd kind2-java-api
./gradlew build
```
To import the API, use the jar file `build/libs/kind2-java-api.jar`. 
Alternatively you can just copy the package `edu.uiowa.cs.clc.kind2` to your source code.

## API Usage
An example of how to use the API is provided in [`StopWatch.java`](https://github.com/kind2-mc/kind2-java-api/blob/master/src/main/java/StopWatch.java). Follow these steps when using the API:
1. Install `kind2-java-api` artifact following the steps in the above section.
2. Import `edu.uiowa.cs.clc.kind2` package in your source code.
3. Construct a lustre `Program` object using the following utilities:
    - Builder objects: `ProgramBuilder`, `ComponentBuilder`, etc.
    - Utility classes: `TypeUtil` and `ExprUtil`.
4. Construct a `Kind2Api` object and call `Kind2Api.execute`, passing the constructed program object.
5. Analyze the `Result` object returned by `Kind2Api.execute`.

## Results
`Result` contains the following features:
- `Result` features:
  - `getValidProperties`, `getFalsifiedProperties`, and `getUnknownProperties` return properties for all components.
  - `getNodeResult` returns an object of `NodeResult` that summarizes all analyses done by kind2 for a given component.

- `NodeResult` features:
  - `getSuggestions` returns an object of `Suggestion` that provides explanations and suggestions based on kind2 analyses for the current component.
  - `getValidProperties`, `getFalsifiedProperties`, and `getUnknownProperties` return properties for the current component, and all its subcomponents.

- `Suggestion` contains explanations and a suggestion for the associated component. If `N` is the current component, and `M` is possibly a subcomponent of `N`, then the suggestion is one of the following:
  - `noActionRequired`: no action required because all components of the system satisfy their contracts, and no component of the system was refined.
  - `strengthenSubComponentContract`: fix `M`s contract because `N` is correct after refinement, but `M`'s contract is too weak to prove `N`'s contract, but `M`'s definition is strong enough.
  - `completeSpecificationOrRemoveComponent`: Either complete specification of `N`'s contract, or remove component `M`, because component `N` satisfies its current contract and one or more assumptions of `M` are not satisfied by `N`.
  - `makeWeakerOrFixDefinition`: either make assumption `A` weaker, or fix `N`'s definition to satisfy `A`, because component `N` doesn't satisfy its contract after refinement, and assumption `A` of `M` is not satisfied by `N`.
  - `makeAssumptionStrongerOrFixDefinition`: Either make `N`'s assumptions stronger, or fix `N`'s definition to satisfy `N`'s guarantees, because component `N` doesn't satisfy its contract after refinement, and either `N` has no subcomponents, or all its subcomponents satisfy their contract.
  - `fixSubComponentIssues`: fix reported issues for `N`'s subcomponents, because component `N` doesn't satisfy its contract after refinement, and One or more subcomponents of N don't satisfy their contract.
  - `fixOneModeActive`: define all modes of component `N`, because kind2 found a state that is not covered by any of the modes in `N`'s contract.
  - `increaseTimeout`: increase the timeout for kind2, because it fails to prove or disprove one of the properties with the previous timeout.

## Credits
This project barrows heavily from [loonworks/jkind](https://github.com/loonwerks/jkind) by Rockwell Collins.
