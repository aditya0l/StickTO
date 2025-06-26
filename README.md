# NoteFlow 📝🎤

A modern, feature-rich Android note-taking application built with cutting-edge technologies. NoteFlow combines the power of voice recording, AI transcription, and intuitive design to create the ultimate note-taking experience.

![NoteFlow Logo](https://img.shields.io/badge/NoteFlow-Android%20App-blue?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple?style=flat&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-blue?style=flat&logo=jetpack-compose)
![Firebase](https://img.shields.io/badge/Firebase-33.14.0-orange?style=flat&logo=firebase)

## 🌟 Features

### 📱 Core Functionality
- **Smart Note Management**: Create, edit, delete, and organize notes with ease
- **Voice Recording**: Record high-quality audio notes with real-time playback
- **AI-Powered Transcription**: Convert voice notes to text using advanced AI
- **Tagging System**: Organize notes with custom tags for better categorization
- **Search & Filter**: Find notes quickly with powerful search capabilities
- **Rich Text Support**: Format your notes with various text styling options

### 🔐 Authentication & Security
- **Firebase Authentication**: Secure user registration and login
- **Google Sign-In**: One-tap authentication with Google accounts
- **User Profiles**: Personalized experience with user-specific data
- **Data Privacy**: Your notes are stored securely and privately

### 🎨 User Experience
- **Material Design 3**: Modern, intuitive interface following Google's design guidelines
- **Dark/Light Theme**: Switch between themes based on your preference
- **Responsive Design**: Optimized for all screen sizes and orientations
- **Smooth Animations**: Delightful micro-interactions and transitions
- **Accessibility**: Built with accessibility in mind for all users

### 🔧 Advanced Features
- **Offline Support**: Work with your notes even without internet connection
- **Auto-Save**: Never lose your work with automatic saving
- **Voice-to-Text**: Real-time speech recognition for hands-free note-taking
- **Audio Playback**: High-quality audio playback with speed controls
- **Export Options**: Share and export your notes in various formats

## 🏗️ Architecture

NoteFlow follows modern Android development best practices with a clean architecture approach:

### 📁 Project Structure
```
app/src/main/java/com/example/noteflow/
├── data/                    # Data layer
│   ├── Note.kt             # Note entity
│   ├── NoteDao.kt          # Data Access Object
│   ├── NoteDatabase.kt     # Room database
│   ├── Converters.kt       # Type converters
│   ├── remote/             # Remote services
│   │   └── AiService.kt    # AI transcription service
│   └── ThemePreferenceManager.kt
├── repository/             # Repository layer
│   ├── NoteRepository.kt   # Repository interface
│   └── NoteRepositoryImpl.kt # Repository implementation
├── userinterface/          # UI layer (Jetpack Compose)
│   ├── NoteApp.kt         # Main navigation
│   ├── LoginScreen.kt     # Authentication screens
│   ├── RegisterScreen.kt
│   ├── NotesListScreen.kt # Note management screens
│   ├── AddEditNoteScreen.kt
│   ├── NoteDetailScreen.kt
│   ├── SettingsScreen.kt  # App settings
│   └── UserProfileSection.kt
├── viewmodel/             # ViewModel layer
│   ├── NoteViewModel.kt   # Note management logic
│   ├── AuthViewModel.kt   # Authentication logic
│   └── NoteViewModelFactory.kt
├── util/                  # Utilities
│   ├── AudioPlayer.kt     # Audio playback utilities
│   ├── VoiceRecorder.kt   # Voice recording utilities
│   ├── PermissionUtils.kt # Permission handling
│   └── StorageHelper.kt   # File storage utilities
└── ui/theme/              # UI theming
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

### 🏛️ Architecture Patterns
- **MVVM (Model-View-ViewModel)**: Clean separation of concerns
- **Repository Pattern**: Centralized data management
- **Room Database**: Local data persistence
- **Jetpack Compose**: Modern declarative UI
- **Navigation Component**: Type-safe navigation
- **Dependency Injection**: Loose coupling between components

## 🛠️ Technology Stack

### Core Technologies
- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern UI toolkit
- **Room Database**: Local data persistence
- **Firebase**: Backend services and authentication
- **Material Design 3**: Design system

### Key Libraries
- **AndroidX Core KTX**: Kotlin extensions
- **Lifecycle Components**: Lifecycle-aware components
- **Navigation Compose**: Type-safe navigation
- **Retrofit**: HTTP client for API calls
- **Coil**: Image loading
- **Lottie**: Animation support
- **DataStore**: Preferences storage
- **Gson**: JSON serialization

### Firebase Services
- **Firebase Authentication**: User management
- **Firebase Storage**: File storage
- **Firebase Functions**: Serverless functions for AI transcription
- **Firebase Analytics**: Usage analytics

## 🚀 Getting Started

### Prerequisites
- **Android Studio Hedgehog** (2023.1.1) or later
- **Android SDK** (API level 24+)
- **Kotlin** 1.9.0 or later
- **Firebase Project** (for backend services)
- **Google Services** configuration file

### Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/adityajaif/StickTO.git
   cd NoteFlow
   ```

2. **Set Up Firebase**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Enable Authentication (Email/Password and Google Sign-In)
   - Enable Cloud Storage
   - Enable Cloud Functions
   - Download `google-services.json` and place it in the `app/` directory

3. **Configure AI Services**
   - Set up Firebase Functions for AI transcription
   - Configure the necessary API keys in Firebase Functions

4. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned repository and open it

5. **Sync and Build**
   - Wait for Gradle sync to complete
   - Build the project (Build → Make Project)
   - Run on an emulator or physical device

### Configuration

#### Firebase Setup
1. Add your `google-services.json` to the `app/` directory
2. Configure Firebase Authentication methods
3. Set up Firebase Storage rules
4. Deploy Firebase Functions for AI transcription

#### Permissions
The app requires the following permissions:
- `RECORD_AUDIO`: For voice recording
- `WRITE_EXTERNAL_STORAGE`: For saving audio files
- `READ_EXTERNAL_STORAGE`: For accessing saved files

## 📱 Usage

### Creating Notes
1. Tap the "+" button to create a new note
2. Enter a title and content
3. Use the voice recording button to add audio
4. Add tags for organization
5. Save your note

### Voice Recording
1. Tap the microphone icon to start recording
2. Speak clearly into your device
3. Tap again to stop recording
4. Use AI transcription to convert to text
5. Edit the transcribed text as needed

### Managing Notes
- **View**: Tap on any note to view its details
- **Edit**: Tap the edit button to modify notes
- **Delete**: Swipe left on a note to delete
- **Search**: Use the search bar to find specific notes
- **Filter**: Use tags to filter your notes

### Settings
- **Theme**: Switch between light and dark themes
- **Account**: Manage your user profile
- **Logout**: Sign out of your account

## 🔧 Development

### Building from Source
```bash
# Clone the repository
git clone https://github.com/adityajaif/StickTO.git

# Open in Android Studio
# Sync Gradle dependencies
# Build the project
```

### Running Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

### Code Style
The project follows Kotlin coding conventions and uses:
- **ktlint**: Code formatting
- **Detekt**: Static code analysis
- **Android Lint**: Android-specific linting

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### Contribution Guidelines
- Follow Kotlin coding conventions
- Write meaningful commit messages
- Add tests for new features
- Update documentation as needed
- Ensure all tests pass

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Google** for Android and Jetpack Compose
- **Firebase** for backend services
- **Material Design** for design guidelines
- **Open Source Community** for various libraries

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/adityajaif/StickTO/issues)
- **Discussions**: [GitHub Discussions](https://github.com/adityajaif/StickTO/discussions)
- **Email**: [adityajaif2004@gmail.com]

## 🔮 Roadmap

### Upcoming Features
- [ ] **Cloud Sync**: Sync notes across devices
- [ ] **Collaboration**: Share notes with others
- [ ] **Advanced AI**: Smart note summarization
- [ ] **Export Options**: PDF, Word, Markdown export
- [ ] **Web Version**: Browser-based note editor
- [ ] **Mobile Widgets**: Quick note creation
- [ ] **Offline Mode**: Enhanced offline capabilities
- [ ] **Voice Commands**: Hands-free operation

### Version History
- **v1.0.0**: Initial release with core features
- **v1.1.0**: Added AI transcription
- **v1.2.0**: Enhanced UI and performance
- **v1.3.0**: Voice recording improvements

---

<div align="center">
  <p>Made with ❤️ by <a href="https://github.com/adityajaif">Aditya</a></p>
  <p>⭐ Star this repository if you find it helpful!</p>
</div> 
