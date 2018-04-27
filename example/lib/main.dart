import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_mapbox/flutter_mapbox.dart';

Future<Null> main() async {
  runApp(new MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() {
    return new _MyAppState();
  }
}

class _MyAppState extends State<MyApp> {

  MapController _controller;

  @override
  void initState() {
    super.initState();
    _controller = new MapController();
  }

  @override
  Widget build(BuildContext context) {
    final MapWidget mapView = new MapWidget(
        controller: _controller,
        options: new MapboxMapOptions(
      style: Style.mapboxStreets,
      camera: new CameraPosition(
          target: new LatLng(lat: -37.8155984, lng: 144.9640312),
          zoom: 11.0,
          bearing: 0.0,
          tilt: 0.0),
    ));

    return new MaterialApp(
        home: new Scaffold(
            appBar: new AppBar(
              title: new Text('Mapbox Flutter Demo'),
            ),
            body: new Container(child: mapView),
            floatingActionButton: new FloatingActionButton(
                elevation: 0.0,
                child: new Icon(Icons.check),
                backgroundColor: new Color(0xFFE57373),
                onPressed: () {
                  _controller.zoom(12.0, 1000.0, 100.0, 5000);
                })));
  }
}
