import 'dart:async';
import 'dart:ui';

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

  @override
  String toString() {
    return 'LatLng{lat: $lat, lng: $lng}';
  }

}

class ProjectedMeters {
  final double northing;
  final double easting;

  ProjectedMeters(this.northing, this.easting);

  Map<String, Object> toMap() {
    return {"northing": northing, "easting": easting};
  }

  @override
  String toString() {
    return 'ProjectedMeters{northing: $northing, easting: $easting}';
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

class MapboxOverlayController {
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

  Future<Null> setStyleUrl(String styleUrl) async {
    try {
      await _channel.invokeMethod(
        'setStyleUrl',
        <String, Object>{
          'textureId': _textureId,
          'styleUrl': styleUrl,
        },
      );
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<String> getStyleUrl() async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'getStyleUrl',
        <String, Object>{'textureId': _textureId},
      );
      return reply['styleUrl'];
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<Null> setStyleJson(String styleJson) async {
    try {
      await _channel.invokeMethod(
        'setStyleJson',
        <String, Object>{
          'textureId': _textureId,
          'styleJson': styleJson,
        },
      );
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<String> getStyleJson() async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'getStyleJson',
        <String, Object>{'textureId': _textureId},
      );
      return reply['styleJson'];
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  //
  // Camera API
  //

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
      CameraPosition camera, int duration) async {
    try {
      await _channel.invokeMethod(
        'easeTo',
        <String, Object>{
          'textureId': _textureId,
          'camera': camera.toMap(),
          'duration': duration,
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

  Future<double> getMinZoom() async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'getMinZoom',
        <String, Object>{'textureId': _textureId},
      );
      return reply['zoom'];
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<Null> setMinZoom(double zoom) async {
    try {
      await _channel.invokeMethod(
        'setMinZoom',
        <String, Object>{
          'textureId': _textureId,
          'zoom': zoom,
        },
      );
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<double> getMaxZoom() async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'getMaxZoom',
        <String, Object>{'textureId': _textureId},
      );
      return reply['zoom'];
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<Null> setMaxZoom(double zoom) async {
    try {
      await _channel.invokeMethod(
        'setMaxZoom',
        <String, Object>{
          'textureId': _textureId,
          'zoom': zoom,
        },
      );
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  //
  // Projection API
  //

  Future<LatLng> getLatLngForOffset(Offset offset) async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'getLatLngForPixel',
        <String, Object>{
          'textureId': _textureId,
          'x': offset.dx,
          'y': offset.dy,
        },
      );
      double lat = reply['lat'];
      double lng = reply['lng'];
      return new LatLng(lat:lat, lng: lng);
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<Offset> getOffsetForLatLng(LatLng latLng) async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'getPixelForLatLng',
        <String, Object>{
          'textureId': _textureId,
          'lat': latLng.lat,
          'lng': latLng.lng,
        },
      );
      double dx = reply['dx'];
      double dy = reply['dy'];
      return new Offset(dx, dy);
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<ProjectedMeters> getProjectedMetersForLatLng(LatLng latLng) async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'getProjectedMetersForLatLng',
        <String, Object>{
          'textureId': _textureId,
          'lat': latLng.lat,
          'lng': latLng.lng,
        },
      );
      double northing = reply['northing'];
      double easting = reply['easting'];
      return new ProjectedMeters(northing, easting);
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<LatLng> getLatLngForProjectedMeters(ProjectedMeters meters) async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'getLatLngForProjectedMeters',
        <String, Object>{
          'textureId': _textureId,
          'northing' : meters.northing,
          'easting' : meters.easting,
        },
      );
      double latitude = reply['lat'];
      double longitude =  reply['lng'];
      return new LatLng(lat: latitude,lng: longitude);
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<double> getMetersPerPixelAtLatitude(double latitude) async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'getMetersPerPixelAtLatitude',
        <String, Object>{
          'textureId': _textureId,
          'lat': latitude,
        },
      );
      return reply["meters"];
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

 Future<List> queryRenderedFeatures(
      Offset offset, List<String> layerIds, String filter) async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'queryRenderedFeatures',
        <String, Object>{
          'textureId': _textureId,
          'x': offset.dx,
          'y': offset.dy,
          'layerIds': layerIds,
          'filter': filter,
        },
      );
      return reply['features'];
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }

  Future<List> queryRenderedFeaturesInRect(Rect rect, List<String> layerIds, String filter) async {
    try {
      final Map<Object, Object> reply = await _channel.invokeMethod(
        'queryRenderedFeatures',
        <String, Object>{
          'textureId': _textureId,
          'left': rect.left,
          'top': rect.top,
          'right': rect.right,
          'bottom': rect.bottom,
          'layerIds': layerIds,
          'filter': filter,
        },
      );
      return reply['features'];
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }
  
  Future<Null> dispose(int _textureId) async {
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
