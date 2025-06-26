# NoteFlow

A modern Android note-taking app with voice note transcription, built using Kotlin, Jetpack Compose, Room, and Firebase.

## Features
- User authentication (Firebase Auth)
- Create, edit, and delete notes
- Record and playback audio notes
- Transcribe audio notes to text using AI (Firebase Functions)
- Tagging and search
- Light/Dark theme support

## Getting Started

### Prerequisites
- Android Studio
- Firebase project (add your `google-services.json`)

### Setup
1. Clone the repository:
   ```sh
   git clone https://github.com/aditya0l/StickTO.git
   ```
2. Open in Android Studio.
3. Add your `google-services.json` to `app/`.
4. Sync Gradle and run the app on an emulator or device.

## Project Structure
- `app/src/main/java/com/example/noteflow/` — Main source code
- `data/` — Data models, Room database, remote services
- `repository/` — Data repository pattern
- `userinterface/` — UI screens (Jetpack Compose)
- `util/` — Utilities (audio, permissions, storage)
- `viewmodel/` — ViewModels (MVVM)

## License
MIT 