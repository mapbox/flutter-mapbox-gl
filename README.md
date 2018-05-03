# Flutter Mapbox GL Native

> **Please note that this project is experimental and is not officially supported.** We welcome [feedback](https://github.com/mapbox/flutter-mapbox-gl/issues) and contributions.

This Flutter plugin for [mapbox-gl-native](https://github.com/mapbox/mapbox-gl-native) enables
embedded interactive and customizable vector maps inside of a Flutter widget. This project plugin is in early development stage. Only Android is supported for now.

![screenshot.png](screenshot.png)

## Getting Started

### Android

This demo app uses Mapbox vector tiles, which require a Mapbox account and a Mapbox access token. Obtain a free access token on [your Mapbox account page](https://www.mapbox.com/account/access-tokens/).

- Install [Flutter](https://flutter.io/get-started/) and validate its installation with `flutter doctor`
- Clone this repository with `git clone git@github.com:mapbox/flutter-mapbox-gl.git`
- Create a `local.properties` file with the following path: `flutter_mapbox/example/android/local.properties`
- Add `mapbox.accessToken="YOUR MAPBOX ACCESS TOKEN"`
 token to the **local.properties** file.
- Run the app with `cd flutter_mapbox/example && flutter run`


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