import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:mapbox_gl/controller.dart';
import 'package:mapbox_gl/overlay.dart';

class ProjectionDemo extends StatefulWidget {
  @override
  _ProjectionDemoState createState() => new _ProjectionDemoState();
}

class _ProjectionDemoState extends State<ProjectionDemo> {
  final MapboxOverlayController controller = new MapboxOverlayController();

  Future _onTapUp(TapUpDetails details) async {
    // convert clicked point to projection data
    Offset clickedPoint = details.globalPosition;
    LatLng clickedLatLng = await controller.getLatLngForOffset(clickedPoint);
    ProjectedMeters projectedMeters =
    await controller.getProjectedMetersForLatLng(clickedLatLng);
    double meters = await controller.getMetersPerPixelAtLatitude(clickedLatLng.lat);
    print("Map clicked at $clickedPoint which translates to $clickedLatLng");
    print("This point has $projectedMeters.");
    print("At this offset and latitude a pixel translates to $meters");

    // use API to convert LatLng and ProjectedMeters back
    Offset convertedPoint = await controller.getOffsetForLatLng(clickedLatLng);
    LatLng convertedLatLng =
    await controller.getLatLngForProjectedMeters(projectedMeters);
    print("Converting back this data results in: $convertedPoint and $convertedLatLng");
  }

  @override
  Widget build(BuildContext context) {
    GestureDetector gestureDetector = new GestureDetector(
      onTapUp: (TapUpDetails details) => _onTapUp(details),
      child: new Container(
        child: new MapboxOverlay(
          controller: controller,
          options: new MapboxMapOptions(
            style: Style.satelliteStreets,
            camera: new CameraPosition(
                target: new LatLng(lat: 50.848209, lng: 4.350864),
                zoom: 13.5,
                bearing: 0.0,
                tilt: 0.0),
          ),
        ),
      ),
    );

    return new Scaffold(
        appBar: new AppBar(
          title: new Text('Mapbox Flutter Demo'),
        ),
        body: gestureDetector);
  }
}
