import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_mapbox/controller.dart';
import 'package:flutter_mapbox/overlay.dart';

class CameraDemo extends StatefulWidget {
  @override
  _CameraDemoState createState() => new _CameraDemoState();
}

class _CameraDemoState extends State<CameraDemo> {
  final MapboxOverlayController controller = new MapboxOverlayController();

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('Mapbox Camera Demo'),
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
              child: new Text('Animate camera'),
              decoration: new BoxDecoration(
                color: Colors.blue,
              ),
            ),
            new ListTile(
              title: new Text('jumpTo Kings Cross'),
              onTap: () {
                controller.jumpTo(new CameraPosition(
                    target: new LatLng(lat: 51.530643, lng: -0.122998),
                    zoom: 14.0,
                    bearing: 60.0,
                    tilt: 60.0));
                Navigator.of(context).pop();
              },
            ),
            new ListTile(
              title: new Text('flyTo Tower Bridge'),
              onTap: () {
                controller.flyTo(
                    new CameraPosition(
                        target: new LatLng(lat: 51.505808, lng: -0.075147),
                        zoom: 12.0,
                        bearing: 0.0,
                        tilt: 35.0),
                    3000);
                Navigator.of(context).pop();
              },
            ),
            new ListTile(
              title: new Text('easeTo London Eye'),
              onTap: () {
                controller.easeTo(
                    new CameraPosition(
                        target: new LatLng(lat: 51.503428, lng: -0.119524),
                        zoom: 11.0,
                        bearing: 180.0,
                        tilt: 0.0),
                    3000);
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
                target: new LatLng(lat: 51.503428, lng: -0.119524),
                zoom: 11.0,
                bearing: 180.0,
                tilt: 0.0),
          ),
        ),
      ),
    );
  }
}
