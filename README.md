# ☕ Coffee Bliss - Membership Card App

Aplikasi Android Digital Membership Card untuk Coffee Bliss, dibangun menggunakan teknologi modern Android.

---

## 🛠️ Teknologi yang Digunakan

| Teknologi | Versi | Fungsi |
|---|---|---|
| Kotlin | 2.0.21 | Bahasa pemrograman utama |
| Jetpack Compose | BOM 2024.12.01 | UI Framework deklaratif |
| Room Database | 2.6.1 | Penyimpanan data lokal |
| MVVM Architecture | - | Pola arsitektur aplikasi |
| Navigation Compose | 2.8.5 | Navigasi antar screen |
| Material 3 Design | - | Desain UI modern |
| ZXing | 3.5.3 | Generate QR Code |
| Kotlin Coroutines & Flow | - | Pemrograman asinkron |

---

## 📁 Struktur Project

```
CoffeeBliss/
├── app/
│   └── src/main/
│       ├── java/com/coffeebliss/
│       │   ├── data/
│       │   │   ├── dao/
│       │   │   │   ├── MemberDao.kt          ← Query Room untuk Member
│       │   │   │   └── TransactionDao.kt     ← Query Room untuk Transaksi
│       │   │   ├── database/
│       │   │   │   └── AppDatabase.kt        ← Database Room (Singleton)
│       │   │   ├── entity/
│       │   │   │   ├── Member.kt             ← Entity/tabel Member
│       │   │   │   └── Transaction.kt        ← Entity/tabel Transaksi
│       │   │   └── repository/
│       │   │       └── CoffeeBlissRepository.kt ← Business logic & data access
│       │   ├── ui/
│       │   │   ├── components/
│       │   │   │   └── Components.kt         ← Reusable Composable UI
│       │   │   ├── screens/
│       │   │   │   ├── SplashScreen.kt       ← Layar pembuka
│       │   │   │   ├── HomeScreen.kt         ← Daftar semua member
│       │   │   │   ├── AddMemberScreen.kt    ← Form registrasi member
│       │   │   │   ├── MemberCardScreen.kt   ← Kartu member digital + QR
│       │   │   │   ├── AddTransactionScreen.kt ← Tambah transaksi
│       │   │   │   ├── RedeemRewardScreen.kt ← Tukar poin dengan hadiah
│       │   │   │   └── TransactionHistoryScreen.kt ← Riwayat transaksi
│       │   │   ├── theme/
│       │   │   │   └── Theme.kt              ← Warna & tema Material 3
│       │   │   ├── NavGraph.kt               ← Definisi navigasi
│       │   │   └── Screen.kt                 ← Route constants
│       │   ├── viewmodel/
│       │   │   └── CoffeeBlissViewModel.kt  ← ViewModel (MVVM)
│       │   └── MainActivity.kt
│       ├── res/
│       │   └── values/
│       │       ├── strings.xml
│       │       └── themes.xml
│       └── AndroidManifest.xml
├── gradle/
│   └── libs.versions.toml                   ← Version catalog (dependencies)
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🚀 Cara Menjalankan Project

### Prasyarat
- Android Studio Ladybug (2024.2) atau lebih baru
- JDK 11+
- Android SDK 26+
- Gradle 8.7+

### Langkah-langkah
1. **Clone / Extract project** ke folder lokal
2. Buka **Android Studio**
3. Pilih **File → Open** → arahkan ke folder `CoffeeBliss`
4. Tunggu hingga Gradle sync selesai (~2-5 menit pertama kali)
5. Jalankan di emulator atau device fisik (Android 8.0 / API 26+)
6. Klik tombol **▶ Run**

---

## 📱 Fitur Aplikasi

### FR-01 Registrasi Member
- Input: Nama, Email, No HP
- Validasi: Email format, field kosong, duplikat email
- Output: ID Member otomatis (MBR00001, MBR00002, ...)

### FR-02 Digital Membership Card
- Tampilan kartu member digital bergaya premium
- QR Code unik berisi informasi member
- Menampilkan: Nama, ID Member, Total Poin

### FR-03 Tambah Transaksi
- Input nominal pembelian
- Hitung poin otomatis: Rp10.000 = 1 Poin
- Preview estimasi poin sebelum simpan

### FR-04 Riwayat Transaksi
- Daftar semua transaksi member
- Menampilkan: tanggal, nominal, poin didapat/digunakan
- Summary: total transaksi, total belanja, poin saat ini

### FR-05 Redeem Reward
- Tampil daftar reward yang tersedia
- Indikator reward yang bisa/tidak bisa di-redeem
- Konfirmasi sebelum redeem
- Poin berkurang otomatis setelah redeem

### Reward List
| Poin | Reward |
|------|--------|
| 50   | Espresso Gratis |
| 100  | Cappuccino Gratis |
| 150  | Latte Gratis |

---

## 🏗️ Arsitektur MVVM

```
UI Layer (Jetpack Compose)
    ↕ StateFlow / Composable State
ViewModel Layer (CoffeeBlissViewModel)
    ↕ Suspend Functions / Flow
Repository Layer (CoffeeBlissRepository)
    ↕ DAO Functions
Data Layer (Room Database)
```

### Alur Data
1. **UI** memiliki state yang di-observe dari **ViewModel** via `StateFlow`
2. **ViewModel** memanggil fungsi di **Repository**
3. **Repository** mengakses **DAO** untuk operasi database
4. **Room** menyimpan data ke SQLite secara lokal
5. Perubahan data langsung muncul di UI via **Flow** (reactive)

---

## 🎨 Desain UI

### Color Palette (Material 3)
| Nama | Hex | Penggunaan |
|------|-----|------------|
| CoffeeBrown | #3E2723 | Primary, tombol utama |
| CoffeeMedium | #6D4C41 | Secondary |
| CoffeeAccent | #D4A853 | Highlight, poin |
| CoffeeAccentLight | #F5E6C8 | Background card |
| CoffeeSurface | #FDF6EE | Background halaman |
| CoffeeGreen | #2E7D32 | Sukses, redeem |

---

## ⚙️ Non-Functional Requirements

- **Performance**: Startup < 3 detik, query database < 500ms
- **Reliability**: Data tetap tersedia setelah aplikasi ditutup (Room DB)
- **Usability**: UI sederhana dan intuitif dengan Material Design 3
- **Maintainability**: MVVM + Repository Pattern

---

## 📦 Dependencies

```toml
# Room Database
androidx-room-runtime = "2.6.1"
androidx-room-ktx = "2.6.1"

# Jetpack Compose
compose-bom = "2024.12.01"
navigation-compose = "2.8.5"
lifecycle-viewmodel-compose = "2.8.7"

# QR Code
zxing-core = "3.5.3"

# Material Icons Extended
material-icons-extended (via compose BOM)
```

---

## 👥 User Persona

**Andi (22 tahun, Mahasiswa)**
- Ingin mengumpulkan poin setiap kali beli kopi
- Melihat reward yang bisa ditukar
- Tidak ingin membawa kartu fisik

**Rina (30 tahun, Barista)**
- Menambahkan transaksi pelanggan
- Melihat status member pelanggan

---

## 📋 Definition of Done

- ✅ Fitur berjalan sesuai requirement
- ✅ Tidak ada error kritis
- ✅ UI/UX baik
- ✅ Data tersimpan dengan aman di Room Database
- ✅ Crash rate < 2%
- ✅ Waktu loading < 2 detik

---

*"Good Coffee, Great Rewards, Better Experience." — Coffee Bliss*
