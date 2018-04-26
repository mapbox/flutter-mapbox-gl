import 'dart:async';

import 'package:flutter/services.dart';

const MethodChannel _channel = const MethodChannel('com.mapbox/flutter_mapbox');

class Style {
  static final String mapboxStreets = "mapbox://styles/mapbox/streets-v10";
  static final String outdoors = "mapbox://styles/mapbox/outdoors-v10";
  static final String light = "mapbox://styles/mapbox/light-v9";
  static final String dark = "mapbox://styles/mapbox/dark-v9";
  static final String satellite = "mapbox://styles/mapbox/satellite-v9";
  static final String satelliteStreets =
      "mapbox://styles/mapbox/satellite-streets-v10";
  static final String trafficDay = "mapbox://styles/mapbox/traffic-day-v2";
  static final String trafficNight = "mapbox://styles/mapbox/traffic-night-v2";
}

class LatLng {
  final double lat;
  final double lng;

  LatLng({this.lat, this.lng});

  Map<String, Object> toMap() {
    return {"lat": lat, "lng": lng};
  }
}

class CameraPosition {
  final LatLng target;
  final double zoom;
  final double bearing;
  final double tilt;

  CameraPosition({this.target, this.zoom, this.bearing, this.tilt});

  CameraPosition copyWith(
      {LatLng target, double zoom, double bearing, double tilt}) {
    LatLng newTarget = target ?? this.target;
    double newZoom = zoom ?? this.zoom;
    double newBearing = bearing ?? this.bearing;
    double newTilt = tilt ?? this.tilt;

    return new CameraPosition(
        target: newTarget, zoom: newZoom, bearing: newBearing, tilt: newTilt);
  }

  Map<String, Object> toMap() {
    return {
      "target": target.toMap(),
      "zoom": zoom,
      "bearing": bearing,
      "tilt": tilt
    };
  }
}

class MapboxMapOptions {
  final String style;
  final CameraPosition camera;

  MapboxMapOptions({this.style, this.camera});

  Map<String, Object> toMap() {
    return {"style": style, "camera": camera.toMap()};
  }
}

class MapboxMap {
  int _textureId;

  void setTextureId(int textureId) {
    _textureId = textureId;
  }

  Future<int> create(
      {double width, double height, MapboxMapOptions options}) async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'create',
        <String, Object>{
          'width': width,
          'height': height,
          'options': options.toMap()
        },
      );
      _textureId = reply['textureId'];

      return new Future.value(_textureId);
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<Null> moveBy(double dx, double dy, int duration) async {
    try {
      await _channel.invokeMethod(
        'moveBy',
        <String, Object>{
          'textureId': _textureId,
          'dx': dx,
          'dy': dy,
          'duration': duration
        },
      );
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<Null> easeTo(
      CameraPosition camera, int duration, bool easingInterpolator) async {
    try {
      await _channel.invokeMethod(
        'flyTo',
        <String, Object>{
          'textureId': _textureId,
          'camera': camera.toMap(),
          'duration': duration,
          'easingInterpolator': easingInterpolator
        },
      );
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<Null> flyTo(CameraPosition camera, int duration) async {
    try {
      await _channel.invokeMethod(
        'flyTo',
        <String, Object>{
          'textureId': _textureId,
          'camera': camera.toMap(),
          'duration': duration
        },
      );
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<Null> jumpTo(CameraPosition camera) async {
    try {
      await _channel.invokeMethod(
        'jumpTo',
        <String, Object>{'textureId': _textureId, 'camera': camera.toMap()},
      );
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<Null> zoom(double zoom, double x, double y, int duration) async {
    try {
      await _channel.invokeMethod(
        'zoom',
        <String, Object>{
          'textureId': _textureId,
          'zoom': zoom,
          'x': x,
          'y': y,
          'duration': duration
        },
      );
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<Null> zoomBy(double zoomBy, double x, double y, int duration) async {
    try {
      await _channel.invokeMethod(
        'zoomBy',
        <String, Object>{
          'textureId': _textureId,
          'zoomBy': zoomBy,
          'x': x,
          'y': y,
          'duration': duration
        },
      );
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<double> getZoom() async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'getZoom',
        <String, Object>{'textureId': _textureId},
      );
      return reply['zoom'];
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<Null> dispose() async {
    try {
      await _channel.invokeMethod(
        'dispose',
        <String, Object>{'textureId': _textureId},
      );
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }
}
