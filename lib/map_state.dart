import 'dart:async';
import 'dart:math';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_mapbox/map_widget.dart';

class MapViewState extends State<MapWidget> {

  bool _initialized = false;
  int _textureId = -1;
  Offset _scaleStartFocal;
  double _zoom;
  Size _size;

  @override
  void dispose() {
    widget.controller.dispose();
    super.dispose();
  }

  Future<Null> _createMapView(Size size, MapboxMapOptions options) async {
    _size = size;
    try {
      int textureId = await widget.controller.create(
          width: size.width, height: size.height, options: options);
      if (!mounted) {
        print("not mounted, returning");
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
        _createMapView(constraints.biggest, widget.options);

        _initialized = true;
        return new Container();
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

  void _onDoubleTap() {
    widget.controller.getZoom().then((zoom) {
      zoom++;
      widget.controller.zoom(zoom, _size.width / 2, _size.height / 2, 350);
    });
  }

  void _onScaleStart(ScaleStartDetails details) {
    _scaleStartFocal = details.focalPoint;
    _zoom = 0.0;
  }

  void _onScaleUpdate(ScaleUpdateDetails details) {
    final Offset delta = details.focalPoint - _scaleStartFocal;
    widget.controller.moveBy(delta.dx, delta.dy, 0);

    if (details.scale != 1.0) {
      RenderBox renderBox = context.findRenderObject();
      Offset focalPoint = renderBox.globalToLocal(details.focalPoint);

      double newZoom = _zoomLevel(details.scale);
      double _zoomBy = newZoom - _zoom;
      widget.controller.zoomBy(_zoomBy, focalPoint.dx, focalPoint.dy, 0);
      _zoom = newZoom;
    }

    _scaleStartFocal = details.focalPoint;
  }

  void _onScaleEnd(ScaleEndDetails details) {
    _scaleStartFocal = null;
    _zoom = null;
  }

  double _zoomLevel(double scale) {
    return log(scale) / log(pi / 2);
  }
}
