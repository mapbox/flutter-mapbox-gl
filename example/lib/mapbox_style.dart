import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_mapbox/controller.dart';
import 'package:flutter_mapbox/overlay.dart';

class StyleDemo extends StatefulWidget {
  @override
  _StyleDemoState createState() => new _StyleDemoState();
}

class _StyleDemoState extends State<StyleDemo> {
  final MapboxOverlayController controller = new MapboxOverlayController();

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('Style Demo'),
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
              child: new Text('Select a style to load'),
              decoration: new BoxDecoration(
                color: Colors.blue,
              ),
            ),
            new ListTile(
              title: new Text('Mapbox streets'),
              onTap: () {
                controller.setStyleUrl(Style.mapboxStreets);
                Navigator.of(context).pop();
              },
            ),
            new ListTile(
              title: new Text('Outdoors'),
              onTap: () {
                controller.setStyleUrl(Style.outdoors);
                Navigator.of(context).pop();
              },
            ),
            new ListTile(
              title: new Text('Light'),
              onTap: () {
                controller.setStyleUrl(Style.light);
                Navigator.of(context).pop();
              },
            ),
            new ListTile(
              title: new Text('Dark'),
              onTap: () {
                controller.setStyleUrl(Style.dark);
                Navigator.of(context).pop();
              },
            ),
            new ListTile(
              title: new Text('Satellite'),
              onTap: () {
                controller.setStyleUrl(Style.satellite);
                Navigator.of(context).pop();
              },
            ),
            new ListTile(
              title: new Text('Satellite streets'),
              onTap: () {
                controller.setStyleUrl(Style.satelliteStreets);
                Navigator.of(context).pop();
              },
            ),
            new ListTile(
              title: new Text('Traffic day'),
              onTap: () {
                controller.setStyleUrl(Style.trafficDay);
                Navigator.of(context).pop();
              },
            ),
            new ListTile(
              title: new Text('Traffic night'),
              onTap: () {
                controller.setStyleUrl(Style.trafficNight);
                Navigator.of(context).pop();
              },
            ),
          ],
        ),
      ),
      body: new Container(
        child: new MapboxOverlay(
          controller: controller,
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
    );
  }
}
