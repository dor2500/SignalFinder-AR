# SignalFinder AR & Speedtest

This is a complete Android project scaffolding for the SignalFinder AR & Speedtest application, written in Kotlin using modern architecture (Compose, Material 3, Coroutines).

## Overview
This application is designed to help users pinpoint the optimal RF/network conditions for running a speed test using real-time sensor metrics and an AR camera overlay.

## Project Structure
- `app/src/main/java/com/example/signalfinder/`: Contains the core application logic.
  - `ar/`: AR overlay and camera rendering.
  - `scanner/`: RF and Wi-Fi signal scanning services, and HUD UI.
  - `speedtest/`: Built-in speed test module and gauge UI.
  - `ui/`: Main screens and theming.

## How to Build and Export the APK

You can build this project using Android Studio or directly via the command line using Gradle.

### Option 1: Using Android Studio (Recommended)
1. Open Android Studio.
2. Select **File > Open** and choose the `signalfinder-ar` directory.
3. Wait for the Gradle sync to complete. It will automatically download the required Android SDKs, Gradle wrappers, and dependencies.
4. Click the **Run** button (green play icon) or navigate to **Build > Build Bundle(s) / APK(s) > Build APK(s)** to generate the `.apk`.

### Option 2: Using the Command Line
If you have Gradle installed on your system, you can build the APK directly from the terminal.

1. Open a terminal and navigate to the project directory:
   ```bash
   cd C:\Users\Dorp\.gemini\antigravity-ide\scratch\signalfinder-ar
   ```

2. Run the Gradle wrapper task to set up the Gradle wrapper (if not already present):
   ```bash
   gradle wrapper
   ```

3. Build the debug APK:
   ```bash
   # On Windows
   gradlew.bat assembleDebug

   # On macOS/Linux
   ./gradlew assembleDebug
   ```

4. The compiled `.apk` will be generated at the following path:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

## Installation Instructions

To install the generated APK on your Android device:

1. Ensure **Developer Options** and **USB Debugging** are enabled on your Android device.
2. Connect your device to your computer via USB.
3. Use `adb` (Android Debug Bridge) to sideload the APK:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
4. Alternatively, you can copy the `app-debug.apk` file to your device's storage and open it using a file manager to install it directly.

## Permissions Required
Upon first launch, the app will request the following runtime permissions:
- Camera (for the AR viewfinder)
- Location (required for scanning Wi-Fi and Cell metrics)
- Phone State (required for detailed telephony parameters)

Make sure to accept these permissions to allow the signal scanning engine to function properly.
