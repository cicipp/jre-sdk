# BuyBuddy JRE Software Development Kit

Lead maintainers: [Emir Çiftçioğlu (eciftcioglu)](https://github.com/eciftcioglu/), [Buğra Ekuklu (Chatatata)](https://github.com/Chatatata/).

![Build status](https://img.shields.io/travis/heybuybuddy/jre-sdk.svg)

**If you're developing applications for Android devices, go to [android-sdk](https://github.com/heybuybuddy/android-sdk).**

## Abstract

The BuyBuddy JRE Software Development Kit makes your application integrate easily with BuyBuddy platform to create astonishing shopping experience, using Java and other JVM languages (i.e. Scala, Kotlin). 
This library provides a generic abstraction layer to access BuyBuddy platform in a modular way.

To get started, navigate to [JRE Integration Guide](https://github.com/heybuybuddy/BuyBuddyKit/).

### Features
- **Platform management**: An object-oriented abstraction of platform management to not deal with underlying HATEOAS API. Every single entity found in our platform can be managed with this library.
- **Simplified payments**: You might use your own payment system, or you can use existing ones found in APIs.

## Installation

### Maven

Add following dependency entry to your `.pom` file.

```xml
<dependency>
    <groupId>co.buybuddy</groupId>
    <artifactId>buybuddy-java-sdk</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>
```

### Gradle

```groovy
compile group: 'co.buybuddy', name: 'buybuddy-java-sdk', version: '1.0.1-SNAPSHOT' 
```

### SBT

```
libraryDependencies += "co.buybuddy" % "buybuddy-java-sdk" % "1.0.1-SNAPSHOT"
```

### Ivy

```xml
<dependency org="co.buybuddy" name="buybuddy-java-sdk" rev="1.0.1-SNAPSHOT" />>
```

### Grape

```
@Grapes(
  @Grab(group='co.buybuddy', module='buybuddy-java-sdk', version='1.0.1-SNAPSHOT')
)
```

### Leiningen

```
[co.buybuddy.buybuddy-java-sdk "1.0.1-SNAPSHOT"]
```

### Buildr

```
'co.buybuddy.buybuddy-java-sdk:buybuddy-java-sdk:1.0.1-SNAPSHOT'
```

## Support
BuyBuddy engineering team is always ready to support you.

### Contributing
All contributions are welcomed, you may open issues or pull requests regarding bug (or bug fixes), new features, or improvements and clarifications in documentations.
We really try hard to make everything found (including HTTP web services) in our platform open source, hence we expect patience from you while everything is going to be eligible.

Finally, please read our [Code of Conduct](https://github.com/heybuybuddy/jre-sdk/blob/refactor/CODE_OF_CONDUCT.md).

### Running Unit Tests
1. Clone the repository to your local: `git clone https://github.com/heybuybuddy/jre-sdk/`.
2. Run `$ mvn test`.

### Releases

| Version                    | Stability   | Development                         | Purpose                                         |
| -------------------------: | :---------: | :---------------------------------: | :---------------------------------------------- |
| active 1.0.1-SNAPSHOT      | Unstable    | Actively developed.                 | Unstable, new library under development.        |

## License
MIT
