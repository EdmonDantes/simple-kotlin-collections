# Simple collections for kotlin

[![Maven Central](http://img.shields.io/maven-central/v/io.github.edmondantes/simple-kotlin-collections?color=green&style=flat-square)](https://search.maven.org/search?q=g:io.github.edmondantes%20a:simple-kotlin-collections)
[![GitHub](http://img.shields.io/github/license/edmondantes/simple-kotlin-collections?style=flat-square)](https://github.com/EdmonDantes/simple-kotlin-collections)
[![Kotlin](https://img.shields.io/badge/kotlin-1.8.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/EdmonDantes/simple-kotlin-collections/check.yml?style=flat-square)](https://github.com/EdmonDantes/simple-kotlin-collections/actions/workflows/check.yml)

Small library which provide some collections for [koltin](https://kotlinlang.org/)

## Table of Contents

1. [How to add library to you project](#how-to-add-library-to-you-project)
2. [Features](#features)

### How to add library to you project

#### Maven

```xml

<dependency>
    <groupId>io.github.edmondantes</groupId>
    <artifactId>simple-kotlin-collections</artifactId>
    <version>0.1.0</version>
</dependency>
```

#### Gradle (kotlin)

```kotlin
implementation("io.github.edmondantes:simple-kotlin-collections:0.1.0")
```

#### Gradle (groovy)

```groovy
implementation "io.github.edmondantes:simple-kotlin-collections:0.1.0"
```

### Features

#### LinkedList

Class `LinkedList` is a simple implementation of linked list. Use can create it by different ways:

```kotlin
linkedListOf(a, b, c)
LinkedList(5) { index -> supplier(index) }
LinkedList(collection as Collection)

byteArrayOf(1, 2, 3).asLinkedList()
```