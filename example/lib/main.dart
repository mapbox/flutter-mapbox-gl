import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_mapbox/flutter_mapbox.dart';

Future<Null> main() async {
  runApp(new MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('Mapbox Flutter Demo'),
        ),
        body: new Container(
          child:new MapView(
            map: new MapboxMap(),
            options:new MapboxMapOptions(
              style: Style.mapboxStreets,
              camera: new CameraPosition(
                target: new LatLng(lat: -37.8155984, lng: 144.9640312),
                zoom: 11.0,
                bearing: 0.0,
                tilt: 0.0
              ),
            ),
          ),
        ),
      ),
    );
  }
}
