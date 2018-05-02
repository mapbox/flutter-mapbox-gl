import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_mapbox/controller.dart';
import 'package:flutter_mapbox/overlay.dart';

class AnimationDemo extends StatefulWidget {
  @override
  _AnimationDemoState createState() => new _AnimationDemoState();
}

class _AnimationDemoState extends State<AnimationDemo> {

  final MapboxOverlayController controller = new MapboxOverlayController();

  bool _visible = true;

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('Flutter Animation Demo'),
      ),
      body: new Container(
        child: new AnimatedOpacity(
          // If the Widget should be visible, animate to 1.0 (fully visible). If
          // the Widget should be hidden, animate to 0.0 (invisible).
          opacity: _visible ? 1.0 : 0.0,
          duration: new Duration(milliseconds: 500),
          // The green box needs to be the child of the AnimatedOpacity
          child: new MapboxOverlay(
            controller: controller,
            options: new MapboxMapOptions(
              style: Style.outdoors,
              camera: new CameraPosition(
                  target: new LatLng(lat: 12.976321, lng: 77.591332),
                  zoom: 10.0,
                  bearing: 0.0,
                  tilt: 0.0),
            ),
          ),
        )
      ),
      floatingActionButton: new FloatingActionButton(
        onPressed: () {
          // Make sure we call setState! This will tell Flutter to rebuild the
          // UI with our changes!
          setState(() {
            _visible = !_visible;
          });
        },
        tooltip: 'Toggle Opacity',
        child: new Icon(Icons.flip),
      ), // This
    );
  }
}
