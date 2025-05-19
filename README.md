# GalleryX 📸

GalleryX is a modern Android gallery app built using Jetpack Compose, Hilt, and a clean multi-module architecture.  
It allows users to browse albums, view media inside folders, and switch between grid and list layouts.

---

## ✨ Features

- View albums with media grouped by folders
- Special "All Images" and "All Videos" virtual albums
- Album detail screen with media thumbnails
- Grid/List toggle support on both album and detail screens
- Modern image loading with Coil (no flicker on scroll)
- Full permission handling (Android 24–34)
- Unit and UI tests for key components

---

## 🧱 Architecture

This project follows Clean Architecture with the following modules:

- `app`: Entry point
- `feature:gallery`: UI and ViewModel logic
- `domain`: Use cases and models
- `data`: MediaStore interaction
- `core`: Shared utilities (e.g., permissions)

Technologies used:
- Jetpack Compose
- Hilt for DI
- Kotlin Coroutines + Flow
- MediaStore API
- Coil for image loading
- Compose Navigation
- Compose UI testing

---

## 🚀 How to Run

1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/GalleryX.git
   ```

2. Open in Android Studio

3. Set min SDK to **24** (already configured)

4. Run on a real device or emulator with media files

---

## 🔐 Permissions

The app requests the correct permissions based on API level:

- Android 24–32: `READ_EXTERNAL_STORAGE`
- Android 33+: `READ_MEDIA_IMAGES`, `READ_MEDIA_VIDEO`

---

## 🧪 Running Tests

To run unit + UI tests:

```bash
./gradlew test
./gradlew connectedAndroidTest
```

Includes:
- ViewModel unit tests (`AlbumViewModel`, `AlbumDetailViewModel`)
- Use case tests
- Compose UI tests (album rendering, grid/list toggle)

---

## 🛠️ Optional Extras (Completed ✅)

- Grid/List toggle on both screens
- Scroll performance optimizations using AsyncImage
- Placeholder, crossfade, and thumbnail resizing
- "All Images" and "All Videos" virtual albums
- Compose UI tests

---

## 🙋 Author

Created by Varun Gulati  
Senior Android Developer