# PhotoAlbum

[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15) [![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

## Screenshot

![screenshot](https://raw.githubusercontent.com/liaoheng/PhotoAlbum/master/screenshot.gif "Screenshot")

## Target platforms

- API level 15 or later

## Latest version

- Version 1.0.0 (03/03/2016)

## Getting started

In your `build.gradle`:
```gradle
repositories {
  mavenCentral()
}
```
```gradle
compile 'com.github.liaoheng:album:1.0.0'
```

## Usage

### sample
```gradle
compile 'com.squareup.picasso:picasso:2.5.2'
```
```java
ImagePagerActivity.start(MainActivity.this, album);
```

### more
see [sample](https://github.com/liaoheng/PhotoAlbum/tree/master/sample/src/main/java/com/github/liaoheng/album/sample)

## Thanks
[Thanks to chrisbanes's PhotoView](https://github.com/chrisbanes/PhotoView)

## License

Copyright (C) 2016 liaoheng

Licensed under the Apache License, Version 2.0