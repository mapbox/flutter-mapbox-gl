package com.example.fluttermapboxexample;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.*;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

import com.mapbox.mapboxsdk.*;
import com.mapbox.mapboxsdk.maps.*;
import com.mapbox.mapboxsdk.maps.*;
import com.mapbox.mapboxsdk.camera.*;
import com.mapbox.mapboxsdk.geometry.*;
import com.mapbox.mapboxsdk.constants.*;
import com.mapbox.mapboxsdk.annotations.*;


public class MainActivity extends FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
    Mapbox.getInstance(this, BuildConfig.MAPBOX_ACCESS_TOKEN);
  }
}
