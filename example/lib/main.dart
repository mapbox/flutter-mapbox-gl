import 'dart:async';

import 'package:flutter/material.dart';
import 'flutter_animation.dart';
import 'flutter_list.dart';
import 'flutter_tabbar.dart';
import 'mapbox_camera.dart';
import 'mapbox_minmaxzoomlevel.dart';
import 'mapbox_style.dart';
import 'mapbox_projection.dart';

Future<Null> main() async {
  runApp(new MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Demo',
      home: new OverviewScreen(),
    );
  }
}

// shows an expandable list of examples
class OverviewScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: const Text('Flutter Mapbox GL'),
      ),
      body: new ListView.builder(
        itemBuilder: (BuildContext context, int index) =>
            new EntryItem(data[index], context),
        itemCount: data.length,
      ),
    );
  }
}

final List<Entry> data = <Entry>[
  new ExpansionEntry(
    'Mapbox API',
    <ScreenEntry>[
      new ScreenEntry('Camera Animations', new CameraDemo()),
      new ScreenEntry('Min/Max Zoom Levels', new MinMaxZoomLevelDemo()),
      new ScreenEntry('Style API', new StyleDemo()),
      new ScreenEntry('Projection', new ProjectionDemo())
    ],
  ),
  new ExpansionEntry(
    'Flutter integration',
    <ScreenEntry>[
      new ScreenEntry('Animation', new AnimationDemo()),
      new ScreenEntry('List integration', new ListDemo()),
      new ScreenEntry('Tabs integration', new TabBarDemo()),
    ],
  )
];

abstract class Entry {
  Entry(this.title);

  final String title;
}

class ScreenEntry extends Entry {
  ScreenEntry(String title, this.screen) : super(title);

  final Widget screen;
}

class ExpansionEntry extends Entry {
  ExpansionEntry(String title, this.children) : super(title);

  final List<Entry> children;
}

class EntryItem extends StatelessWidget {
  const EntryItem(this.entry, this.context);

  final Entry entry;
  final BuildContext context;

  Widget _buildTiles(Entry root) {
    if (root is ScreenEntry) {
      return new ListTile(
          title: new Text(root.title),
          onTap: () {
            Navigator.push(
              context,
              new MaterialPageRoute(builder: (context) => root.screen),
            );
          });
    } else {
      return new ExpansionTile(
        key: new PageStorageKey<Entry>(root),
        title: new Text(root.title),
        children: (root as ExpansionEntry).children.map(_buildTiles).toList(),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return _buildTiles(entry);
  }
}
