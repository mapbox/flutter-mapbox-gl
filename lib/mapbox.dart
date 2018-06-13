import 'dart:async';
import 'dart:ui';

import 'package:flutter/services.dart';

const MethodChannel _channel = const MethodChannel('com.mapbox/flutter_mapbox');

class Mapbox {


  Future<Null> setAccessToken(String accessToken) async {
    try {
      await _channel.invokeMethod(
        'setAccessToken',
        <String, Object>{
          'accessToken': accessToken,
        },
      );
    } on PlatformException catch (e) {
      return new Future.error(e);
    }
  }
}
