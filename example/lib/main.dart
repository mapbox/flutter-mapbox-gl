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
  final MapboxMap mapboxMap = new MapboxMap();

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('Mapbox Flutter Demo'),
        ),
        drawer: new Drawer(
          // Add a ListView to the drawer. This ensures the user can scroll
          // through the options in the Drawer if there isn't enough vertical
          // space to fit everything.
          child: new ListView(
            // Important: Remove any padding from the ListView.
            padding: EdgeInsets.zero,
            children: <Widget>[
              new DrawerHeader(
                child: new Text('Drawer Header'),
                decoration: new BoxDecoration(
                  color: Colors.blue,
                ),
              ),
              new ListTile(
                title: new Text('flyTo New York'),
                onTap: () {
                  mapboxMap.flyTo(
                      0.0,
                      new LatLng(lat: 40.758896, lng: -73.985130),
                      1000,
                      0.0,
                      11.0);
                },
              ),
              new ListTile(
                title: new Text('flyTo Melbourne'),
                onTap: () {
                  mapboxMap.flyTo(
                      0.0,
                      new LatLng(lat: -37.8155984, lng: 144.9640312),
                      1000,
                      0.0,
                      11.0);
                },
              ),
            ],
          ),
        ),
        body: new Container(
          child: new MapView(
            map: mapboxMap,
            options: new MapboxMapOptions(
              style: Style.mapboxStreets,
              camera: new CameraPosition(
                  target: new LatLng(lat: -37.8155984, lng: 144.9640312),
                  zoom: 11.0,
                  bearing: 0.0,
                  tilt: 0.0),
            ),
          ),
        ),
      ),
    );
  }
}
