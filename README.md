# cosmobuzz-client-kotlin (Unofficial)

![Build](https://github.com/T-Fowl/cosmobuzz-client-kotlin/workflows/Build/badge.svg)
![Maven Central](https://img.shields.io/maven-central/v/com.tfowl.cosmobuzz/cosmobuzz-client-kotlin)
![GitHub](https://img.shields.io/github/license/T-Fowl/cosmobuzz-client-kotlin)

Simple kotlin client wrapper for creating and managing rooms on [cosmobuzz.net](https://www.cosmobuzz.net/). Created as
a sub-project of a trivia controller for trivia nights with my friends.

## Usage

Creating a room:

```kotlin
when (val result = CosmoBuzz.createRoom()) {
    is CreateRoomResult.Success -> run(result.room)
    is CreateRoomResult.Failure -> error(result.reason)
}
``` 

`CosmoBuzzRoom` is essentially a view-model for a room's state

Accessing state:

```kotlin
room.players.collect { players -> playersChanged(players) }
room.settings.collect { settings -> settingsChanged(settings) }
room.buzzer.collect { player -> playerBuzzed(player) }
```

Updating room:

```kotlin
room.resetBuzzers()
room.updateSettings(newRoomSettings)
```

## Example project

The examples project contains a (basic) cli interpreter around a `CosmoBuzzRoom`, here is an example run:

```shell
CosmoBuzzRoom(code=427126, url=https://www.cosmobuzz.net/#/play/<room-code>)

>list-players
Players (2):
	PlayerA
	PlayerB
	
BUZZ: PlayerA
BUZZ: PlayerB

>reset-buzzers
>toggle-first-buz

BUZZ: PlayerA
```

## Download

Add a gradle dependency to your project:

Groovy

```groovy
repositories {
    mavenCentral()
}
implementation "com.tfowl.cosmobuzz:cosmobuzz-client-kotlin:$version"
```

Kotlin DSL

```kotlin
repositories {
    mavenCentral()
}
implementation("com.tfowl.cosmobuzz:cosmobuzz-client-kotlin:$version")
```

Add a maven dependency to your project:

```xml

<dependency>
    <groupId>com.tfowl.cosmobuzz</groupId>
    <artifactId>cosmobuzz-client-kotlin</artifactId>
    <version>${version}</version>
</dependency>
```

## License

```
MIT License

Copyright (c) 2021 Thomas Fowler

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```