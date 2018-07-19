package com.mapbox.mapboxsdk.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.graphics.SurfaceTexture;

import com.mapbox.mapboxsdk.geometry.ProjectedMeters;
import com.mapbox.mapboxsdk.maps.renderer.surfacetexture.SurfaceTextureMapRenderer;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.renderer.MapRenderer;
import com.mapbox.mapboxsdk.net.ConnectivityReceiver;
import com.mapbox.mapboxsdk.storage.FileSource;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.expressions.Expression;

public class FlutterMap implements NativeMapView.ViewCallback, MapView.OnMapChangedListener {
  private final Context context;
  private final NativeMapView nativeMapView;
  private final MapRenderer mapRenderer;
  private int width;
  private int height;

  public FlutterMap(Context context, MapboxMapOptions options, SurfaceTexture surfaceTexture, int width, int height) {
    this.context = context;
    this.width = width;
    this.height = height;

    String localFontFamily = options.getLocalIdeographFontFamily();
    boolean translucentSurface = options.getTranslucentTextureSurface();
    mapRenderer = new SurfaceTextureMapRenderer(context, surfaceTexture, width, height, localFontFamily,
        translucentSurface);

    nativeMapView = new NativeMapView(context, this, mapRenderer);
    nativeMapView.addOnMapChangedListener(this);
    nativeMapView.setStyleUrl(options.getStyle());
    nativeMapView.resizeView(width, height);
    nativeMapView.setReachability(ConnectivityReceiver.instance(context).isConnected(context));
    nativeMapView.update();

    CameraPosition cameraPosition = options.getCamera();
    if (cameraPosition != null) {
      nativeMapView.jumpTo(cameraPosition.bearing, cameraPosition.target, cameraPosition.tilt, cameraPosition.zoom);
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

  public void setStyleUrl(String styleUrl){
    nativeMapView.setStyleUrl(styleUrl);
  }

  public String getStyleUrl(){
    return nativeMapView.getStyleUrl();
  }

  public void setStyleJson(String styleJson) {
    nativeMapView.setStyleJson(styleJson);
  }

  public String getStyleJson(){
    return nativeMapView.getStyleJson();
  }

  public void moveBy(double dx, double dy, long duration) {
    nativeMapView.moveBy(dx, dy, duration);
  }

  public void easeTo(CameraPosition cameraPosition, int duration, boolean easingInterpolator) {
    nativeMapView.easeTo(cameraPosition.bearing, cameraPosition.target, duration, cameraPosition.tilt,
        cameraPosition.zoom, easingInterpolator);
  }

  public void flyTo(CameraPosition cameraPosition, int duration) {
    nativeMapView.flyTo(cameraPosition.bearing, cameraPosition.target, duration, cameraPosition.tilt,
        cameraPosition.zoom);
  }

  public void jumpTo(CameraPosition cameraPosition) {
    nativeMapView.jumpTo(cameraPosition.bearing, cameraPosition.target, cameraPosition.tilt, cameraPosition.zoom);
  }

  public double getMetersPerPixelAtLatitude(double latitude){
    return nativeMapView.getMetersPerPixelAtLatitude(latitude);
  }

  public ProjectedMeters getProjecteMeters(LatLng latLng){
    return nativeMapView.projectedMetersForLatLng(latLng);
  }

  public LatLng getLatLng(ProjectedMeters projectedMeters){
    return nativeMapView.latLngForProjectedMeters(projectedMeters);
  }

  public LatLng getLatLng(PointF screenPoint){
    return nativeMapView.latLngForPixel(new PointF(screenPoint.x*getPixelRatio(), screenPoint.y*getPixelRatio()));
  }

  public PointF getScreenPoint(LatLng latLng){
    PointF screenPoint = nativeMapView.pixelForLatLng(latLng);
    return new PointF(screenPoint.x/getPixelRatio(),screenPoint.y/getPixelRatio());
  }

  public double getMinZoom() {
    return nativeMapView.getMinZoom();
  }

  public void setMinZoom(double zoom) {
    nativeMapView.setMinZoom(zoom);
  }

  public double getMaxZoom() {
    return nativeMapView.getMaxZoom();
  }

  public void setMaxZoom(double zoom) {
    nativeMapView.setMaxZoom(zoom);
  }

  public double getZoom() {
    return nativeMapView.getZoom();
  }

  public void zoom(double zoom, PointF focalPoint, long duration) {
    nativeMapView.setZoom(zoom, focalPoint, duration);
  }

  public float getPixelRatio() {
    return nativeMapView.getPixelRatio();
  }

  public java.util.List<com.mapbox.geojson.Feature> queryRenderedFeatures(@NonNull PointF coordinates, @Nullable String[] layerIds, @Nullable Expression filter){
    PointF scaledCoordinates = new PointF(coordinates.x*getPixelRatio(), coordinates.y*getPixelRatio());
    return nativeMapView.queryRenderedFeatures(scaledCoordinates,layerIds,filter);
  }

  public java.util.List<com.mapbox.geojson.Feature> queryRenderedFeatures(@NonNull RectF coordinates, @Nullable String[] layerIds, @Nullable Expression filter){
    RectF scaledCoordinates = new RectF(coordinates.left*getPixelRatio(),coordinates.top*getPixelRatio(),coordinates.right*getPixelRatio(),coordinates.bottom*getPixelRatio());
    return nativeMapView.queryRenderedFeatures(scaledCoordinates,layerIds,filter);
  }
}
