# Coffee Bliss

---

## Teknologi yang Digunakan

| Teknologi | Versi | Fungsi |
|---|---|---|
| Kotlin | 2.0.21 | Language |
| Jetpack Compose | BOM 2024.12.01 | UI framework |
| Room Database | 2.6.1 | Local database |
| MVVM Architecture | - | App architecture |
| Navigation Compose | 2.8.5 | Between ncreen nav |
| Material 3 Design | - | UI |
| ZXing | 3.5.3 | Generate QR code |
| Kotlin Coroutines & Flow | - | Async programs |

---

## Struktur Project

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
│   └── libs.versions.toml
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Requirements
- Android Studio Ladybug (2024.2) atau lebih baru
- JDK 11+
- Android SDK 26+
- Gradle 8.7+

---

## App Feature

### Registrasi Member
- Input: Nama, Email, No HP
- Validasi: Email format, field kosong, duplikat email
- Output: ID Member otomatis (MBR00001, MBR00002, ...)

### Digital Membership Card
- Tampilan kartu member digital bergaya premium
- QR Code unik berisi informasi member
- Menampilkan: Nama, ID Member, Total Poin

### Tambah Transaksi
- Input nominal pembelian
- Hitung poin otomatis: Rp10.000 = 1 Poin
- Preview estimasi poin sebelum simpan

### Riwayat Transaksi
- Daftar semua transaksi member
- Menampilkan: tanggal, nominal, poin didapat/digunakan
- Summary: total transaksi, total belanja, poin saat ini

### Redeem Reward
- Tampil daftar reward yang tersedia
- Indikator reward yang bisa/tidak bisa di-redeem
- Konfirmasi sebelum redeem
- Poin berkurang otomatis setelah redeem

---

## MVVM Architecture

```
UI Layer (Jetpack Compose)
    ↕ StateFlow / Composable State
ViewModel Layer (CoffeeBlissViewModel)
    ↕ Suspend Functions / Flow
Repository Layer (CoffeeBlissRepository)
    ↕ DAO Functions
Data Layer (Room Database)
```

---

## Dependencies

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
