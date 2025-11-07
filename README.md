# Pembelajaran Kaidah - Android Mobile App

## 📱 Project Overview

Aplikasi mobile Android untuk pembelajaran kaidah bahasa Arab (Ilmu Nahwu) dengan fokus pada implementasi algoritma **Linear Congruent Method (LCM)** untuk pengacakan soal.

## 🎯 Study Information

- **Judul Skripsi**: Rancang Bangun Aplikasi Pembelajaran Pengenalan Kaidah Dalam Bahasa Arab Menggunakan Algoritma Linear Congruent Method (LCM) Berbasis Android
- **Studi Kasus**: MA MODEL MIFTAHUL FALAH
- **Mahasiswa**: Khozinnatul Ulum (20210810076)
- **Program Studi**: Teknik Informatika S1
- **Fakultas**: Ilmu Komputer
- **Universitas**: Universitas Kuningan
- **Tahun**: 2025

## 🏗️ Project Structure

```
PembelajaranKaidah/
├── app/
│   ├── src/main/java/com/khozin/pembelajarankaidah/
│   │   ├── data/
│   │   │   ├── model/           # Data models
│   │   │   ├── repository/      # Repository classes
│   │   │   ├── local/          # Room Database
│   │   │   └── remote/         # API Service
│   │   ├── ui/
│   │   │   ├── login/          # Login screen
│   │   │   ├── home/           # Home dashboard
│   │   │   ├── kaidah/         # Material list
│   │   │   ├── quiz/           # Quiz session
│   │   │   ├── progress/       # Progress tracking
│   │   │   └── profile/        # User profile
│   │   ├── utils/              # Utility classes
│   │   │   ├── LinearCongruentMethod.java  # LCM Algorithm
│   │   │   └── SessionManager.java
│   │   └── MainActivity.java
│   ├── build.gradle             # App dependencies
│   └── src/main/res/             # Resources
├── docs/                       # Documentation
├── build.gradle                # Project dependencies
└── README.md                   # This file
```

## 🔧 Technology Stack

### Core Technologies
- **Language**: Java
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 36 (Android 14)
- **Architecture**: MVVM (Model-View-ViewModel)

### Key Libraries
- **Network**:
  - Retrofit 2.9.0 (HTTP client)
  - Gson 2.9.0 (JSON parsing)
  - OkHttp 4.10.0 (HTTP client)
- **Database**:
  - Room 2.6.1 (Local SQLite database)
  - Repository pattern for data management
- **UI**:
  - Material Design Components
  - Glide 4.15.1 (Image loading)
  - ConstraintLayout

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Java 11+
- Android 8.0+ (API 26+)
- Web backend server running (CodeIgniter 4)

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd PembelajaranKaidah
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - File → Open → Select project directory
   - Wait for Gradle sync to complete

3. **Configure API Base URL**

   Edit `app/src/main/java/com/khozin/pembelajarankaidah/network/ApiConstants.java`:
   ```java
   // For Android Emulator
   public static final String BASE_URL = "http://10.0.2.2:8080/api/";

   // For Physical Device (replace with your computer's IP)
   // public static final String BASE_URL = "http://192.168.1.100:8080/api/";

   // For Production
   // public static final String BASE_URL = "https://yourapp.com/api/";
   ```

4. **Build and Run**
   - Select device/emulator
   - Click Run (▶) or use shortcut `Shift + F10`

## 🔌 API Integration

### Available Endpoints

#### Authentication
- `POST /api/siswa/login` - Student login
- `GET /api/siswa/profile` - Get student profile

#### Learning Materials
- `GET /api/kaidah` - Get all kaidah materials
- `GET /api/kaidah/{id}` - Get specific kaidah details

#### Learning Sessions
- `POST /api/sesi/start` - Start new session
- `GET /api/sesi/active` - Check active session
- `POST /api/sesi/{id}/jawab` - Submit answer
- `POST /api/sesi/{id}/finish` - Complete session
- `GET /api/sesi/{id}/hasil` - Get results

### Authentication Flow

1. **Login**: Students use NIS and password (default: 123456)
2. **Token**: Server returns Base64 encoded token (user_id:timestamp)
3. **API Calls**: Include token in `Authorization: Bearer <token>` header
4. **Token Validity**: 24 hours from generation

### Sample Login Credentials
```
NIS: 2025001-2025010
Password: 123456
```

## 🧮 Linear Congruent Method (LCM)

### Algorithm Implementation
The app implements LCM algorithm for question randomization:

**Formula**: `Xn+1 = (a × Xn + c) mod m`

**Parameters** (from skripsi research):
- **a (multiplier)**: 10
- **c (increment)**: 23
- **m (modulus)**: 29
- **X0 (seed)**: timestamp + user_id + material_id

### Usage in App
1. **Question Randomization**: Shuffle question order
2. **Answer Randomization**: Shuffle answer choices
3. **Session Reproducibility**: Same seed = same question order
4. **Research Validation**: Consistent with web backend implementation

### Code Location
`app/src/main/java/com/khozin/pembelajarankaidah/utils/LinearCongruentMethod.java`

## 📱 App Features

### Core Functionality
- ✅ **Student Authentication** - Login with NIS/password
- ✅ **Material Access** - Browse kaidah materials (Bab 1 Kalam, Bab 2 I'rab)
- ✅ **Interactive Quiz** - Randomized questions using LCM
- ✅ **Progress Tracking** - Monitor learning progress
- ✅ **Results Analysis** - Detailed quiz results and statistics
- ✅ **Offline Support** - Local database for offline learning

### UI/UX Features
- 📱 **Modern Material Design** - Clean, intuitive interface
- 🎨 **Consistent Theme** - Follows project design system
- 📊 **Progress Visualization** - Visual learning progress
- 🔔 **Real-time Feedback** - Immediate answer validation
- 📱 **Responsive Design** - Works on various screen sizes

### Technical Features
- 🔄 **Session Management** - Complete session lifecycle
- 📡 **API Integration** - RESTful API with proper error handling
- 💾 **Local Caching** - Room database for offline access
- 🔐 **Secure Authentication** - Token-based security
- 🧪 **LCM Algorithm** - Research-focused randomization

## 📊 Database Schema

### Local Tables
- `siswa` - Student information
- `materi_kaidah` - Learning materials (cached from API)
- `sesi_latihan` - Local session data
- `riwayat_belajar` - Learning progress history
- `detail_jawaban` - Answer tracking

### Sync Strategy
- **Online Mode**: Real-time API communication
- **Offline Mode**: Local database with sync on reconnect
- **Cache Strategy**: Material caching for offline access

## 🔧 Development Guide

### Adding New Features

1. **Create Model**: Add data model in `data/model/`
2. **Update API**: Add endpoint in `data/remote/ApiService.java`
3. **Implement Repository**: Add repository in `data/repository/`
4. **Create ViewModel**: Add ViewModel in `ui/[feature]/`
5. **Design UI**: Create Fragment/Activity in `ui/[feature]/`

### Code Style

Follow Android Java conventions:
- **Packages**: `com.khozin.pembelajarankaidah.feature`
- **Classes**: `PascalCase` (e.g., `LoginActivity`)
- **Methods**: `camelCase` (e.g., `startSession()`)
- **Constants**: `UPPER_SNAKE_CASE` (e.g., `BASE_URL`)

### Error Handling

- **Network Errors**: Show user-friendly messages
- **Validation**: Input validation with proper feedback
- **Crash Reporting**: Implement crash reporting
- **Logging**: Comprehensive logging for debugging

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

### API Testing
- Use Postman or curl for API testing
- Check `../docs/API_Documentation.md` for API details
- Test with provided credentials

## 📱 Build Variants

### Debug
- Debuggable builds with logging
- Development server integration
- Test credentials available

### Release
- Optimized builds
- Production server integration
- ProGuard obfuscation

## 📄 API Documentation

Complete API documentation available in:
`../docs/API_Documentation.md`

## 🚨 Important Notes

### Development Environment
- Web backend must be running for mobile app functionality
- Configure proper IP address for physical device testing
- Ensure network permissions in AndroidManifest.xml

### Network Security
- HTTP for development, HTTPS for production
- Certificate pinning for production builds
- Proper SSL/TLS configuration

### Testing Credentials
Default student credentials for testing:
```
NIS: 2025001-2025010
Password: 123456
```

### LCM Algorithm
- Consistent implementation with web backend
- Parameters hardcoded per skripsi requirements
- Seed-based randomization for reproducible results

## 📞 Support

### For Development Issues
- Check API backend status first
- Verify network connectivity
- Review API documentation
- Check Android Studio logcat

### For API Issues
- Refer to `../docs/API_Documentation.md`
- Check web backend logs
- Test API endpoints with curl/Postman

### Project Related
- Contact development team
- Review skripsi documentation
- Check issue tracker

## 📝 License

This project is part of the skripsi research for:
- **Universitas Kuningan**
- **Program Studi Teknik Informatika S1**
- **Mahasiswa**: Khozinnatul Ulum (20210810076)

---

## 🎯 Quick Start Guide

1. **Setup Development Environment**
   - Install Android Studio
   - Start web backend server
   - Configure API base URL

2. **Run the App**
   - Open project in Android Studio
   - Sync Gradle
   - Run on emulator/device

3. **Test Login**
   - Use credentials: NIS=2025001, Password=123456
   - Navigate through materials
   - Test quiz functionality

4. **Verify LCM Implementation**
   - Start multiple sessions
   - Check question randomization
   - Verify reproducible results

Happy coding! 🚀