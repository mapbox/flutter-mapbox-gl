import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_mapbox/controller.dart';
import 'package:flutter_mapbox/overlay.dart';

class MinMaxZoomLevelDemo extends StatefulWidget {
  @override
  _MinMaxZoomLevelDemoState createState() => new _MinMaxZoomLevelDemoState();
}

class _MinMaxZoomLevelDemoState extends State<MinMaxZoomLevelDemo> {
  final MapboxOverlayController controller = new MapboxOverlayController();

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
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
              title: new Text('setMinZoom(9)'),
              onTap: () {
                controller.setMinZoom(9.0);
                Navigator.of(context).pop();
              },
            ),
            new ListTile(
              title: new Text('setMaxZoom(11.5)'),
              onTap: () {
                controller.setMaxZoom(11.5);
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
            style: Style.trafficDay,
            camera: new CameraPosition(
                target: new LatLng(lat: 35.685670, lng:  139.752793),
                zoom: 8.0,
                bearing: 0.0,
                tilt: 0.0),
          ),
        ),
      ),
    );
  }
}
