# Keystore Configuration

## 🔐 **Generate Release Keystore**

```bash
# Navigate to keystore directory
cd app/keystore

# Generate keystore (hanya sekali!)
keytool -genkey -v -keystore release.keystore -alias release -keyalg RSA -keysize 2048 -validity 10000

# Example answers untuk keystore generation:
# Enter keystore password: [buat password yang kuat]
# Re-enter new password: [ulangi password]
# What is your first and last name?: Khozinnatul Ulum
# What is the name of your organizational unit?: MA Miftahul Falah
# What is the name of your organization?: MA Miftahul Falah
# What is the name of your City or Locality?: Kuningan
# What is the name of your State or Province?: Jawa Barat
# What is the two-letter country code for this unit?: ID
# Is CN=Khozinnatul Ulum, OU=MA Miftahul Falah, O=MA Miftahul Falah, L=Kuningan, ST=Jawa Barat, C=ID correct?: yes

# Enter key password for <release>: [buat password untuk key]
# RETURN if same as keystore password: [tekan Enter]
```

## ⚠️ **IMPORTANT WARNING**

**KESTORE HILANG = APLIKASI TIDAK BISA UPDATE LAGI!**

1. **BACKUP KESTORE SECARA BERKALA** ✅
   - Google Drive
   - External hard drive
   - Cloud storage
   - Multiple locations

2. **JANGAN PERNAH COMMIT KEYSOTE KE GIT** ❌
   - Tambahkan ke `.gitignore`
   - Simpan di tempat yang aman

3. **CATAT PASSWORD DI TEMPAT AMAN** 📝
   - Password manager
   - Encrypted file
   - Physical note

## 📱 **Build Release APK**

```bash
# Build release APK
./gradlew assembleRelease

# Build release bundle (untuk Play Store)
./gradlew bundleRelease
```

## 🔧 **Environment Variables**

Untuk production build, setup environment variables:

```bash
# Terminal
export KEYSTORE_PASSWORD="your_keystore_password"
export KEY_ALIAS="release"
export KEY_PASSWORD="your_key_password"

# Atau di ~/.bashrc atau ~/.zshrc
echo 'export KEYSTORE_PASSWORD="your_keystore_password"' >> ~/.bashrc
echo 'export KEY_ALIAS="release"' >> ~/.bashrc
echo 'export KEY_PASSWORD="your_key_password"' >> ~/.bashrc
```

## 📂 **File Locations**

- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release.apk`
- **Release Bundle**: `app/build/outputs/bundle/release/app-release.aab`
- **Keystore**: `app/keystore/release.keystore` (JANGAN DI-HAPUS!)