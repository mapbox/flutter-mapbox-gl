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

public class FlutterMap implements NativeMapView.ViewCallback,
    MapView.OnMapChangedListener {
  private Context context;
  private MapboxMapOptions mapboxMapOptions;
  private NativeMapView nativeMapView;
  private MapRenderer mapRenderer;
  private int width;
  private int height;

  public FlutterMap(Context context, MapboxMapOptions options,
                    SurfaceTexture surfaceTexture, int width, int height) {
    this.context = context;
    this.mapboxMapOptions = options;
    this.width = width;
    this.height = height;

    String localFontFamily = mapboxMapOptions.getLocalIdeographFontFamily();
    boolean translucentSurface = mapboxMapOptions.getTranslucentTextureSurface();
    mapRenderer = new SurfaceTextureMapRenderer(context, surfaceTexture, width, height, localFontFamily, translucentSurface);

    nativeMapView = new NativeMapView(context, 1, this, mapRenderer);
    nativeMapView.addOnMapChangedListener(this);
    nativeMapView.setStyleUrl(mapboxMapOptions.getStyle());
    nativeMapView.resizeView(width, height);
    nativeMapView.setReachability(ConnectivityReceiver.instance(context).isConnected(context));
    nativeMapView.update();

    CameraPosition cameraPosition = mapboxMapOptions.getCamera();
    if (cameraPosition != null) {
        nativeMapView.jumpTo(cameraPosition.bearing, cameraPosition.target,
            cameraPosition.tilt, cameraPosition.zoom);
    }
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public Bitmap getViewContent() {
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
    // null when destroying an activity programmatically mapbox-navigation-android/issues/503
    nativeMapView.destroy();
    mapRenderer.onDestroy();
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
