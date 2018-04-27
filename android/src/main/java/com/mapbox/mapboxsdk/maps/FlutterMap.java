package com.mapbox.mapboxsdk.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;

import com.mapbox.mapboxsdk.maps.renderer.surfacetexture.SurfaceTextureMapRenderer;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.renderer.MapRenderer;
import com.mapbox.mapboxsdk.net.ConnectivityReceiver;
import com.mapbox.mapboxsdk.storage.FileSource;

public class FlutterMap implements NativeMapView.ViewCallback, MapView.OnMapChangedListener {

  private final Context context;
  private final NativeMapView nativeMapView;
  private final MapRenderer mapRenderer;

  public FlutterMap(Context context, MapboxMapOptions options,
                    SurfaceTexture surfaceTexture, int width, int height) {
    this.context = context;

    String localFontFamily = options.getLocalIdeographFontFamily();
    boolean translucentSurface = options.getTranslucentTextureSurface();
    mapRenderer = new SurfaceTextureMapRenderer(context, surfaceTexture, width, height, localFontFamily, translucentSurface);

    nativeMapView = new NativeMapView(context, 1, this, mapRenderer);
    nativeMapView.addOnMapChangedListener(this);
    nativeMapView.setStyleUrl(options.getStyle());
    nativeMapView.resizeView(width, height);
    nativeMapView.setReachability(ConnectivityReceiver.instance(context).isConnected(context));
    nativeMapView.update();

    CameraPosition cameraPosition = options.getCamera();
    if (cameraPosition != null) {
        nativeMapView.jumpTo(cameraPosition.bearing, cameraPosition.target,
            cameraPosition.tilt, cameraPosition.zoom);
    }
  }

  @Override
  public int getWidth() {
    // only needed if other component requires width from NativeMapView
    // correct integration requires notifying size changes to this class
    throw new RuntimeException("not implemented");
  }

  @Override
  public int getHeight() {
    // only needed if other component requires height from NativeMapView
    // correct integration requires notifying size changes to this class
    throw new RuntimeException("not implemented");
  }

  @Override
  public Bitmap getViewContent() {
    // returns bitmap of overlain android views for snapshot integration
    return null;
  }

  @Override
  public void onMapChanged(int change) {

  }

  public void onStart() {
    ConnectivityReceiver.instance(context).activate();
    FileSource.getInstance(context).activate();
    mapRenderer.onStart();
  }

  public void onResume() {
    mapRenderer.onResume();
  }

  public void onPause() {
    mapRenderer.onPause();
  }

  public void onStop() {
    mapRenderer.onStop();
    ConnectivityReceiver.instance(context).deactivate();
    FileSource.getInstance(context).deactivate();
  }

  public void onDestroy() {
    nativeMapView.removeOnMapChangedListener(this);
    mapRenderer.onDestroy();
    nativeMapView.destroy();
  }

  public void moveBy(double dx, double dy, long duration) {
    nativeMapView.moveBy(dx, dy, duration);
  }

  public double getZoom() {
    return nativeMapView.getZoom();
  }

  public void zoom(double zoom, PointF focalPoint, long duration) {
    nativeMapView.setZoom(zoom, focalPoint, duration);
  }
}
