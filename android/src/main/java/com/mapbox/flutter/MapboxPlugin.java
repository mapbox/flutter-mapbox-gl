package com.mapbox.flutter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.ProjectedMeters;
import com.mapbox.mapboxsdk.maps.FlutterMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.geojson.Feature;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.view.FlutterView;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * FlutterMapboxPlugin
 */
public class MapboxPlugin implements MethodCallHandler {

  private final FlutterView view;
  private Activity activity;
  private Registrar registrar;

  private static Map<Long, MapInstance> maps = new HashMap<>();

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  private MapboxPlugin(Registrar registrar, FlutterView view, Activity activity) {
    this.registrar = registrar;
    this.view = view;
    this.activity = activity;

    activity.getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
      @Override
      public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity == MapboxPlugin.this.activity) {
          for (MapInstance mapInstance : maps.values()) {
            //                    mapInstance.map.onCreate(savedInstanceState);
          }
        }
      }

      @Override
      public void onActivityStarted(Activity activity) {
        if (activity == MapboxPlugin.this.activity) {
          for (MapInstance mapInstance : maps.values()) {
            mapInstance.map.onStart();
          }
        }
      }

      @Override
      public void onActivityResumed(Activity activity) {
        if (activity == MapboxPlugin.this.activity) {
          for (MapInstance mapInstance : maps.values()) {
            mapInstance.map.onResume();
          }
        }
      }

      @Override
      public void onActivityPaused(Activity activity) {
        if (activity == MapboxPlugin.this.activity) {
          for (MapInstance mapInstance : maps.values()) {
            mapInstance.map.onPause();
          }
        }
      }

      @Override
      public void onActivityStopped(Activity activity) {
        if (activity == MapboxPlugin.this.activity) {
          for (MapInstance mapInstance : maps.values()) {
            mapInstance.map.onStop();
          }
        }
      }

      @Override
      public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (activity == MapboxPlugin.this.activity) {
          for (MapInstance mapInstance : maps.values()) {
            //                    mapInstance.map.onSaveInstanceState(outState);
          }
        }
      }

      @Override
      public void onActivityDestroyed(Activity activity) {
        if (activity == MapboxPlugin.this.activity) {
          //                  for (MapInstance mapInstance : maps.values()) {
          //                    mapInstance.release();
          //                  }
        }
      }
    });
  }

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "com.mapbox/flutter_mapbox");
    channel.setMethodCallHandler(new MapboxPlugin(registrar, registrar.view(), registrar.activity()));
  }

  @TargetApi(Build.VERSION_CODES.N)
  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("create")) {
      FlutterView.SurfaceTextureEntry surfaceTextureEntry = view.createSurfaceTexture();
      final int width = ((Number) call.argument("width")).intValue();
      final int height = ((Number) call.argument("height")).intValue();
      final MapboxMapOptions options = parseOptions((Map<String, Object>) call.argument("options"));
      SurfaceTexture surfaceTexture = surfaceTextureEntry.surfaceTexture();
      surfaceTexture.setDefaultBufferSize(width, height);
      FlutterMap mapView = new FlutterMap(activity, options, surfaceTexture, width, height);
      mapView.onStart();
      mapView.onResume();

      maps.put(surfaceTextureEntry.id(), new MapInstance(mapView, surfaceTextureEntry));
      Map<String, Object> reply = new HashMap<>();
      reply.put("textureId", surfaceTextureEntry.id());
      result.success(reply);
      return;
    }

    long textureId = textureIdOfCall(call);
    if (!maps.containsKey(textureId)) {
      result.success(null);
      return;
    }
    MapInstance mapInstance = maps.get(textureId);

    switch (call.method) {

      case "setStyleUrl": {
        String styleUrl = stringParamOfCall(call, "styleUrl");
        mapInstance.map.setStyleUrl(styleUrl);
        result.success(null);
        break;
      }

      case "getStyleUrl": {
        Map<String, Object> reply = new HashMap<>();
        reply.put("styleUrl", mapInstance.map.getStyleUrl());
        result.success(reply);
        break;
      }

      case "setStyleJson": {
        String styleUrl = stringParamOfCall(call, "styleJson");
        mapInstance.map.setStyleJson(styleUrl);
        result.success(null);
        break;
      }

      case "getStyleJson": {
        Map<String, Object> reply = new HashMap<>();
        reply.put("styleJson", mapInstance.map.getStyleJson());
        result.success(reply);
        break;
      }

      case "moveBy": {
        double dx = doubleParamOfCall(call, "dx");
        double dy = doubleParamOfCall(call, "dy");
        long duration = longParamOfCall(call, "duration");
        mapInstance.map.moveBy(dx, dy, duration);
        result.success(null);
        break;
      }

      case "easeTo": {
        CameraPosition cameraPosition = parseCamera(call.argument("camera"));
        int duration = intParamOfCall(call, "duration");
        mapInstance.map.easeTo(cameraPosition, duration, true);
        result.success(null);
        break;
      }

      case "flyTo": {
        CameraPosition cameraPosition = parseCamera(call.argument("camera"));
        int duration = intParamOfCall(call, "duration");
        mapInstance.map.flyTo(cameraPosition, duration);
        result.success(null);
        break;
      }

      case "jumpTo": {
        CameraPosition cameraPosition = parseCamera(call.argument("camera"));
        mapInstance.map.jumpTo(cameraPosition);
        result.success(null);
        break;
      }

      case "zoom": {
        double zoom = doubleParamOfCall(call, "zoom");
        float x = floatParamOfCall(call, "x");
        float y = floatParamOfCall(call, "y");
        long duration = longParamOfCall(call, "duration");
        mapInstance.map.zoom(zoom, new PointF(x, y), duration);
        result.success(null);
        break;
      }

      case "zoomBy": {
        double zoomBy = doubleParamOfCall(call, "zoomBy");
        float x = floatParamOfCall(call, "x");
        float y = floatParamOfCall(call, "y");
        long duration = longParamOfCall(call, "duration");
        mapInstance.map.zoom(mapInstance.map.getZoom() + zoomBy, new PointF(x, y), duration);
        result.success(null);
        break;
      }

      case "getMinZoom": {
        Map<String, Object> reply = new HashMap<>();
        reply.put("zoom", mapInstance.map.getMinZoom());
        result.success(reply);
        break;
      }

      case "setMinZoom": {
        double zoom = doubleParamOfCall(call, "zoom");
        mapInstance.map.setMinZoom(zoom);
        result.success(null);
        break;
      }

      case "getMaxZoom": {
        Map<String, Object> reply = new HashMap<>();
        reply.put("zoom", mapInstance.map.getMaxZoom());
        result.success(reply);
        break;
      }

      case "setMaxZoom": {
        double zoom = doubleParamOfCall(call, "zoom");
        mapInstance.map.setMaxZoom(zoom);
        result.success(null);
        break;
      }

      case "getZoom": {
        Map<String, Object> reply = new HashMap<>();
        reply.put("zoom", mapInstance.map.getZoom());
        result.success(reply);
        break;
      }

      case "getLatLngForPixel": {
        Map<String, Object> reply = new HashMap<>();
        LatLng latLng = mapInstance.map.getLatLng(screenPointParamOfCall(call));
        reply.put("lat", latLng.getLatitude());
        reply.put("lng", latLng.getLongitude());
        result.success(reply);
        break;
      }

      case "getPixelForLatLng": {
        Map<String, Object> reply = new HashMap<>();
        PointF pointF = mapInstance.map.getScreenPoint(latLngParamOfCall(call));
        reply.put("dx", pointF.x);
        reply.put("dy", pointF.y);
        result.success(reply);
        break;
      }

      case "getProjectedMetersForLatLng": {
        Map<String, Object> reply = new HashMap<>();
        ProjectedMeters projectedMeters = mapInstance.map.getProjecteMeters(latLngParamOfCall(call));
        reply.put("northing", projectedMeters.getNorthing());
        reply.put("easting", projectedMeters.getEasting());
        result.success(reply);
        break;
      }

      case "getLatLngForProjectedMeters": {
        Map<String, Object> reply = new HashMap<>();
        LatLng latLng = mapInstance.map.getLatLng(projectedMetersPointParamOfCall(call));
        reply.put("lat", latLng.getLatitude());
        reply.put("lng", latLng.getLongitude());
        result.success(reply);
        break;
      }

      case "getMetersPerPixelAtLatitude": {
        Map<String, Object> reply = new HashMap<>();
        double latitude = doubleParamOfCall(call, "lat");
        reply.put("meters", mapInstance.map.getMetersPerPixelAtLatitude(latitude));
        result.success(reply);
        break;
      }

      case "dispose": {
        mapInstance.release();
        maps.remove(textureId);
        break;
      }

      case "queryRenderedFeatures": {
        Map<String, Object> reply = new HashMap<>();
        List<Feature> features;
        String[] layerIds = stringListParamOfCall(call,"layerIds");
        String filter = stringParamOfCall(call,"filter");
        Expression filterExpression = filter == null ? null : new Expression(filter);
        if (call.hasArgument("x")) {
          PointF pixel = screenPointParamOfCall(call);
          features = mapInstance.map.queryRenderedFeatures(pixel, layerIds, filterExpression);
        } else {
          RectF rectF = screenRectParamOfCall(call);
          features = mapInstance.map.queryRenderedFeatures(rectF, layerIds, filterExpression);
        }
        List<String> featuresJson = new ArrayList<>();
        for (Feature feature : features) {
          featuresJson.add(feature.toJson());
        }
        reply.put("features", featuresJson);
        result.success(reply);
        break;
      }

      default:
        result.notImplemented();
    }
  }

  private boolean booleanParamOfCall(MethodCall call, String param) {
    return Boolean.parseBoolean(call.argument(param));
  }

  private double doubleParamOfCall(MethodCall call, String param) {
    return ((Number) call.argument(param)).doubleValue();
  }

  private float floatParamOfCall(MethodCall call, String param) {
    return ((Number) call.argument(param)).floatValue();
  }

  private int intParamOfCall(MethodCall call, String param) {
    return ((Number) call.argument(param)).intValue();
  }

  private long longParamOfCall(MethodCall call, String param) {
    return ((Number) call.argument(param)).longValue();
  }

  private String stringParamOfCall(MethodCall call, String param) {
    return (String) call.argument(param);
  }

  private long textureIdOfCall(MethodCall call) {
    return ((Number) call.argument("textureId")).longValue();
  }

  private LatLng latLngParamOfCall(MethodCall call) {
    Double lat = call.argument("lat");
    Double lng = call.argument("lng");
    return new LatLng(lat, lng);
  }

  private PointF screenPointParamOfCall(MethodCall call) {
    Double x = call.argument("x");
    Double y = call.argument("y");
    return new PointF(x.floatValue(), y.floatValue());
  }

  private ProjectedMeters projectedMetersPointParamOfCall(MethodCall call) {
    double easting = call.argument("easting");
    double northing = call.argument("northing");
    return new ProjectedMeters(northing, easting);
  }

  private String[] stringListParamOfCall(MethodCall call, String param) {
    ArrayList<String> arrayList = (ArrayList<String>) call.argument(param);
    return arrayList == null ? null : arrayList.toArray(new String[arrayList.size()]);
  }

  private RectF screenRectParamOfCall(MethodCall call) {
    Double left = call.argument("left");
    Double top = call.argument("top");
    Double right = call.argument("right");
    Double bottom = call.argument("bottom");
    return new RectF(left.floatValue(), top.floatValue(), right.floatValue(), bottom.floatValue());
  }

  private MapboxMapOptions parseOptions(Map<String, Object> options) {

    String style = (String) options.get("style");
    if (style == null) {
      style = Style.MAPBOX_STREETS;
    }
    MapboxMapOptions mapOptions = new MapboxMapOptions().styleUrl(style);

    Map<String, Object> camera = (Map<String, Object>) options.get("camera");
    if (camera != null) {
      mapOptions.camera(parseCamera(camera));
    }
    return mapOptions;
  }

  private CameraPosition parseCamera(Map<String, Object> camera) {
    CameraPosition.Builder cameraPosition = new CameraPosition.Builder();

    LatLng target = parseLatLng((Map<String, Object>) camera.get("target"));
    if (target != null) {
      cameraPosition.target(target);
    }

    Double zoom = (Double) camera.get("zoom");
    if (zoom != null) {
      cameraPosition.zoom(zoom);
    }

    Double bearing = (Double) camera.get("bearing");
    if (bearing != null) {
      cameraPosition.bearing(bearing);
    }

    Double tilt = (Double) camera.get("tilt");
    if (tilt != null) {
      cameraPosition.tilt(tilt);
    }

    return cameraPosition.build();
  }

  private LatLng parseLatLng(Map<String, Object> target) {
    if (target.containsKey("lat") && target.containsKey("lng")) {
      return new LatLng(((Number) target.get("lat")).doubleValue(), ((Number) target.get("lng")).doubleValue());
    }
    return null;
  }

  private static class MapInstance {
    FlutterMap map;
    FlutterView.SurfaceTextureEntry surfaceTextureEntry;

    MapInstance(FlutterMap map, FlutterView.SurfaceTextureEntry surfaceTextureEntry) {
      this.map = map;
      this.surfaceTextureEntry = surfaceTextureEntry;
    }

    void release() {
      if (map != null) {
        map.onPause();
        map.onDestroy();
        map = null;
      }

      if (surfaceTextureEntry != null) {
        surfaceTextureEntry.release();
        surfaceTextureEntry = null;
      }
    }
  }
}
