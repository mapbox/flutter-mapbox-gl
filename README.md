# Flutter Mapbox GL Native

> **Please note that this project is experimental and is not officially supported.** We welcome [feedback](https://github.com/mapbox/flutter-mapbox-gl/issues) and contributions.

This Flutter plugin for [mapbox-gl-native](https://github.com/mapbox/mapbox-gl-native) enables
embedded interactive and customizable vector maps inside of a Flutter widget. This project plugin is in early development stage. Only Android is supported for now.

![screenshot.png](screenshot.png)

## Getting Started

Following examples use Mapbox vector tiles, which require a Mapbox account and a Mapbox access token. Obtain a free access token on [your Mapbox account page](https://www.mapbox.com/account/access-tokens/). After you get the key, initialize in your dart app:

```
import 'package:mapbox_gl/mapbox.dart';

new Mapbox().setAccessToken("YOUR TOKEN HERE");
```

### Android

 
#### Demo app

- Install [Flutter](https://flutter.io/get-started/) and validate its installation with `flutter doctor`
- Clone this repository with `git clone git@github.com:mapbox/flutter-mapbox-gl.git`
- Run the app with `cd flutter_mapbox/example && flutter run`

#### New project

- Create new Flutter project in your IDE or via terminal
- Add `mapbox_gl: ^0.0.1` dependency to `pubspec.yaml` file and [get the package](https://flutter.io/using-packages/#adding-a-package-dependency-to-an-app)

```
dependencies {
    // ...
    implementation "com.mapbox.mapboxsdk:mapbox-android-sdk:6.1.0-SNAPSHOT"
}
```

- Import Mapbox widgets and add them to your widget tree
```
import 'package:mapbox_gl/controller.dart';
import 'package:mapbox_gl/mapbox.dart';
import 'package:mapbox_gl/flutter_mapbox.dart';
import 'package:mapbox_gl/overlay.dart';
```

## Documentation

This README file currently houses all of the documentation for this Flutter project. Please visit [mapbox.com/android-docs](https://www.mapbox.com/android-docs/) if you'd like more information about the Mapbox Maps SDK for Android and [mapbox.com/ios-sdk](https://www.mapbox.com/ios-sdk/) for more information about the Mapbox Maps SDK for iOS.

## Getting Help

- **Need help with your code?**: Look for previous questions on the [#mapbox tag](https://stackoverflow.com/questions/tagged/mapbox+android) — or [ask a new question](https://stackoverflow.com/questions/tagged/mapbox+android).
- **Have a bug to report?** [Open an issue](https://github.com/mapbox/flutter-mapbox-gl/issues/new). If possible, include a full log and information which shows the issue.
- **Have a feature request?** [Open an issue](https://github.com/mapbox/flutter-mapbox-gl/issues/new). Tell us what the feature should do and why you want the feature.

## Sample code

[This repository's example library](https://github.com/mapbox/flutter-mapbox-gl/tree/master/example/lib) is currently the best place for you to find reference code for this project.

## Contributing

We welcome contributions to this repository!

If you're interested in helping build this Mapbox/Flutter integration, please read [the contribution guide](https://github.com/mapbox/flutter-mapbox-gl/blob/master/CONTRIBUTING.md) to learn how to get started.
