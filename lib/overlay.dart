import 'dart:async';
import 'dart:math';
import 'dart:ui';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_mapbox/controller.dart';

class MapboxOverlay extends StatefulWidget {
  MapboxOverlay({Key key, this.controller, this.options}) : super(key: key);

  final MapboxOverlayController controller;
  final MapboxMapOptions options;

  @override
  State<StatefulWidget> createState() => new _MapboxOverlayState();
}


/// Manages the creation and state of the map.
/// This includes:
///  - maintaining the id of the used surface texture
///  - maintaining the map transformation
///  - handling gesture input
class _MapboxOverlayState extends State<MapboxOverlay> {
  bool _initialized = false;
  int _textureId = -1;
  Offset _scaleStartFocal;
  double _zoom;
  Size _size; // local coordinate system.

  Future<Null> _createMapView(
      Window window, Size size, MapboxMapOptions options) async {
    _size = size;

    try {
      int textureId = await widget.controller.create(
          width: _size.width * window.devicePixelRatio,
          height: _size.height * window.devicePixelRatio,
          options: options);

      if (!mounted) {
        return;
      }
      setState(() {
        _textureId = textureId;
      });
    } on PlatformException catch (e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return new LayoutBuilder(
        builder: (BuildContext context, BoxConstraints constraints) {
      if (constraints.biggest.isEmpty) {
        return new Container();
      }

      if (!_initialized) {
        _createMapView(window, constraints.biggest, widget.options);
        _initialized = true;
        return new Container();
      } else {
        widget.controller.setTextureId(_textureId);
      }

      GestureDetector detector = new GestureDetector(
        onDoubleTap: _onDoubleTap,
        onScaleStart: _onScaleStart,
        onScaleUpdate: _onScaleUpdate,
        onScaleEnd: _onScaleEnd,
        child: new Texture(textureId: _textureId),
      );
      return detector;
    });
  }

  /// Called when the user double taps the screen, results in zooming the map
  void _onDoubleTap() {
    // TODO we currently zoom on center, this needs to be the tapped offset instead
    widget.controller.getZoom().then((zoom) {
      zoom++;
      widget.controller.zoom(zoom, _size.width / 2 * window.devicePixelRatio,
          _size.height / 2 * window.devicePixelRatio, 350);
    });
  }

  /// Called when the user initiates a scale/pan gesture.
  void _onScaleStart(ScaleStartDetails details) {
    _scaleStartFocal = localToMapOffset(details.focalPoint);
    _zoom = 0.0;
  }

  /// Called when the user scales or pans the map.
  void _onScaleUpdate(ScaleUpdateDetails details) {
    Offset focalGesture = localToMapOffset(details.focalPoint);
    final Offset delta = focalGesture - _scaleStartFocal;
    widget.controller.moveBy(delta.dx, delta.dy, 0);

    if (details.scale != 1.0) {
      RenderBox renderBox = context.findRenderObject();
      Offset focalPoint =
          localToMapOffset(renderBox.globalToLocal(details.focalPoint));

      double newZoom = _zoomLevel(details.scale);
      double _zoomBy = newZoom - _zoom;
      widget.controller.zoomBy(_zoomBy, focalPoint.dx, focalPoint.dy, 0);

      _zoom = newZoom;
    }

    _scaleStartFocal = localToMapOffset(details.focalPoint);
  }

  /// Called when the users stops scaling the map.
  void _onScaleEnd(ScaleEndDetails details) {
    _scaleStartFocal = null;
    _zoom = null;
  }

  /// Calculates a zoom value from a scale gesture detector input.
  double _zoomLevel(double scale) {
    return log(scale) / log(pi / 2);
  }

  /// Flutter supports 2 coordinate systems (local and global).
  /// You can convert between the two using box.localToGlobal/globalToLocal.
  /// This does not match what we expect on gl-native so we need to convert
  /// to the offset to match the input on the Android SDK
  Offset localToMapOffset(Offset offset) {
    // TODO replace with MatrixUtils.transformPoint
    return new Offset(offset.dx * window.devicePixelRatio,
        offset.dy * window.devicePixelRatio);
  }
}
