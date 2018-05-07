import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:mapbox_gl/controller.dart';
import 'package:mapbox_gl/overlay.dart';

class TabBarDemo extends StatelessWidget {

  final MapboxOverlayController controller = new MapboxOverlayController();

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new DefaultTabController(
        length: 3,
        child: new Scaffold(
          appBar: new AppBar(
            bottom: new TabBar(
              tabs: [
                new Tab(icon: new Icon(Icons.directions_car)),
                new Tab(icon: new Icon(Icons.directions_transit)),
                new Tab(icon: new Icon(Icons.directions_bike)),
              ],
            ),
            title: new Text('Tabs Demo'),
          ),
          body: new TabBarView(
            children: [
              new MapboxOverlay(
                controller: controller,
                options: new MapboxMapOptions(
                  style: Style.light,
                  camera: new CameraPosition(
                      target: new LatLng(lat: 40.717183, lng: -74.000974),
                      zoom: 8.0,
                      bearing: 0.0,
                      tilt: 0.0),
                ),
              ),
              new Icon(Icons.directions_car),
              new Icon(Icons.directions_transit),
              new Icon(Icons.directions_bike),
            ],
          ),
        ),
      ),
    );
  }
}
