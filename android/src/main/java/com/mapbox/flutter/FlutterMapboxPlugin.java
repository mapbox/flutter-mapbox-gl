package com.mapbox.flutter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;

import java.util.Map;
import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.view.FlutterView;

import com.mapbox.mapboxsdk.maps.*;
import com.mapbox.mapboxsdk.camera.*;
import com.mapbox.mapboxsdk.geometry.*;
import com.mapbox.mapboxsdk.constants.*;

/**
 * FlutterMapboxPlugin
 */
public class FlutterMapboxPlugin implements MethodCallHandler {

  private final FlutterView view;
  private Activity activity;
  private Registrar registrar;

  private static Map<Long, MapInstance> maps = new HashMap<>();

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  private FlutterMapboxPlugin(Registrar registrar, FlutterView view, Activity activity) {
    this.registrar = registrar;
    this.view = view;
    this.activity = activity;

    activity.getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
      @Override
      public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity == FlutterMapboxPlugin.this.activity) {
          for (MapInstance mapInstance : maps.values()) {
            //                    mapInstance.map.onCreate(savedInstanceState);
          }
        }
      }

      @Override
      public void onActivityStarted(Activity activity) {
        if (activity == FlutterMapboxPlugin.this.activity) {
          for (MapInstance mapInstance : maps.values()) {
            mapInstance.map.onStart();
          }
        }
      }

      @Override
      public void onActivityResumed(Activity activity) {
        if (activity == FlutterMapboxPlugin.this.activity) {
          for (MapInstance mapInstance : maps.values()) {
            mapInstance.map.onResume();
          }
        }
      }

      @Override
      public void onActivityPaused(Activity activity) {
        if (activity == FlutterMapboxPlugin.this.activity) {
          for (MapInstance mapInstance : maps.values()) {
            mapInstance.map.onPause();
          }
        }
      }

      @Override
      public void onActivityStopped(Activity activity) {
        if (activity == FlutterMapboxPlugin.this.activity) {
          for (MapInstance mapInstance : maps.values()) {
            mapInstance.map.onStop();
          }
        }
      }

      @Override
      public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (activity == FlutterMapboxPlugin.this.activity) {
          for (MapInstance mapInstance : maps.values()) {
            //                    mapInstance.map.onSaveInstanceState(outState);
          }
        }
      }

      @Override
      public void onActivityDestroyed(Activity activity) {
        if (activity == FlutterMapboxPlugin.this.activity) {
          //                  for (MapInstance mapInstance : maps.values()) {
          //                    mapInstance.release();
          //                  }
        }
      }
    });
  }

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "com.mapbox/flutter_mapbox");
    channel.setMethodCallHandler(new FlutterMapboxPlugin(registrar, registrar.view(), registrar.activity()));
  }

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
  @Override
  public void onMethodCall(MethodCall call, Result result) {
    switch (call.method) {
    case "create": {
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
      break;
    }

    case "setStyleUrl": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        String styleUrl = stringParamOfCall(call, "styleUrl");
        MapInstance mapHolder = maps.get(textureId);
        mapHolder.map.setStyleUrl(styleUrl);
      }
      result.success(null);
      break;
    }

    case "getStyleUrl": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        MapInstance mapInstance = maps.get(textureId);
        Map<String, Object> reply = new HashMap<>();
        reply.put("styleUrl", mapInstance.map.getStyleUrl());
        result.success(reply);
      }
      result.success(null);
      break;
    }

    case "setStyleJson": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        String styleUrl = stringParamOfCall(call, "styleJson");
        MapInstance mapHolder = maps.get(textureId);
        mapHolder.map.setStyleJson(styleUrl);
      }
      result.success(null);
      break;
    }

    case "getStyleJson": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        MapInstance mapInstance = maps.get(textureId);
        Map<String, Object> reply = new HashMap<>();
        reply.put("styleJson", mapInstance.map.getStyleJson());
        result.success(reply);
      }
      result.success(null);
      break;
    }

    case "moveBy": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        double dx = doubleParamOfCall(call, "dx");
        double dy = doubleParamOfCall(call, "dy");
        long duration = longParamOfCall(call, "duration");

        MapInstance mapHolder = maps.get(textureId);
        mapHolder.map.moveBy(dx, dy, duration);
      }
      result.success(null);
      break;
    }

    case "easeTo": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {

        CameraPosition cameraPosition = parseCamera(call.argument("camera"));
        int duration = intParamOfCall(call, "duration");
        boolean easingInterpolator = booleanParamOfCall(call, "easingInterpolator");

        MapInstance mapHolder = maps.get(textureId);
        mapHolder.map.easeTo(cameraPosition, duration, easingInterpolator);
      }
      result.success(null);
      break;
    }

    case "flyTo": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {

        CameraPosition cameraPosition = parseCamera(call.argument("camera"));
        int duration = intParamOfCall(call, "duration");

        MapInstance mapHolder = maps.get(textureId);
        mapHolder.map.flyTo(cameraPosition, duration);
      }
      result.success(null);
      break;
    }

    case "jumpTo": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {

        CameraPosition cameraPosition = parseCamera(call.argument("camera"));

        MapInstance mapHolder = maps.get(textureId);
        mapHolder.map.jumpTo(cameraPosition);
      }
      result.success(null);
      break;
    }

    case "zoom": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        MapInstance mapInstance = maps.get(textureId);

        double zoom = doubleParamOfCall(call, "zoom");
        float x = floatParamOfCall(call, "x");
        float y = floatParamOfCall(call, "y");
        long duration = longParamOfCall(call, "duration");

        mapInstance.map.zoom(zoom, new PointF(x, y), duration);
      }
      result.success(null);
      break;
    }

    case "zoomBy": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        MapInstance mapInstance = maps.get(textureId);

        double zoomBy = doubleParamOfCall(call, "zoomBy");
        float x = floatParamOfCall(call, "x");
        float y = floatParamOfCall(call, "y");
        long duration = longParamOfCall(call, "duration");

        mapInstance.map.zoom(mapInstance.map.getZoom() + zoomBy, new PointF(x, y), duration);
      }
      result.success(null);
      break;
    }

    case "getMinZoom": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        MapInstance mapInstance = maps.get(textureId);

        Map<String, Object> reply = new HashMap<>();
        reply.put("zoom", mapInstance.map.getMinZoom());
        result.success(reply);
      }
      result.success(null);
      break;
    }

    case "setMinZoom": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        MapInstance mapInstance = maps.get(textureId);

        double zoom = doubleParamOfCall(call, "zoom");
        mapInstance.map.setMinZoom(zoom);
      }
      result.success(null);
      break;
    }

    case "getMaxZoom": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        MapInstance mapInstance = maps.get(textureId);

        Map<String, Object> reply = new HashMap<>();
        reply.put("zoom", mapInstance.map.getMaxZoom());
        result.success(reply);
      }
      result.success(null);
      break;
    }

    case "setMaxZoom": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        MapInstance mapInstance = maps.get(textureId);

        double zoom = doubleParamOfCall(call, "zoom");
        mapInstance.map.setMaxZoom(zoom);
      }
      result.success(null);
      break;
    }

    case "getZoom": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        MapInstance mapInstance = maps.get(textureId);
        Map<String, Object> reply = new HashMap<>();
        reply.put("zoom", mapInstance.map.getZoom());
        result.success(reply);
      }
      result.success(null);
      break;
    }

    case "dispose": {
      long textureId = textureIdOfCall(call);
      if (maps.containsKey(textureId)) {
        MapInstance mapHolder = maps.get(textureId);
        mapHolder.release();
        maps.remove(textureId);
      }
      result.success(null);
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

  private String stringParamOfCall(MethodCall call, String param){
    return (String) call.argument(param);
  }

  private long textureIdOfCall(MethodCall call) {
    return ((Number) call.argument("textureId")).longValue();
  }

  private MapboxMapOptions parseOptions(Map<String, Object> options) {

    String style = (String) options.get("style");
    if (style == null)
      style = Style.MAPBOX_STREETS;
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
    if (target != null)
      cameraPosition.target(target);

    Double zoom = (Double) camera.get("zoom");
    if (zoom != null)
      cameraPosition.zoom(zoom);

    Double bearing = (Double) camera.get("bearing");
    if (bearing != null)
      cameraPosition.bearing(bearing);

    Double tilt = (Double) camera.get("tilt");
    if (tilt != null)
      cameraPosition.tilt(tilt);

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
