import 'package:flutter/material.dart';


void main() {
  runApp(MyApp());
}


class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('Game Booster')),
        body: Center(child: Text('Welcome to Android Game Booster!')),
      ),
    );
  }
}