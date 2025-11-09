# Rancang Bangun Aplikasi Pembelajaran Pengenalan Kaidah Dalam Bahasa Arab Menggunakan Algoritma Linear Congruent Method (LCM) Berbasis Android

## Informasi Skripsi
- **Judul**: Rancang Bangun Aplikasi Pembelajaran Pengenalan Kaidah Dalam Bahasa Arab Menggunakan Algoritma Linear Congruent Method (LCM) Berbasis Android
- **Studi Kasus**: MA MODEL MIFTAHUL FALAH
- **Mahasiswa**: Khozinnatul Ulum (20210810076)
- **Program Studi**: Teknik Informatika S1
- **Fakultas**: Ilmu Komputer
- **Universitas**: Universitas Kuningan
- **Pembimbing 1**: Yati Nurhayati, M.Kom
- **Pembimbing 2**: Dede Husen, M.Kom
- **Tahun**: 2025

## Overview Project
Aplikasi pembelajaran kaidah bahasa Arab (Ilmu Nahwu) yang menggunakan algoritma Linear Congruent Method (LCM) untuk mengacak soal-soal pembelajaran. Terdiri dari 2 komponen:
- **Mobile App (Java/Android)**: Untuk akses siswa
- **Web App (CodeIgniter 4)**: Untuk admin (guru)

## Lokasi Penelitian
- **Nama Sekolah**: MA MODEL MIFTAHUL FALAH
- **Alamat**: Desa Cilowa, Kecamatan Kramatmulya, Kabupaten Kuningan
- **Jenis**: Madrasah Aliyah (setara SMA)
- **Pondok Pesantren**: Miftahul Falah
- **Pimpinan Umum**: KH. Aman Syamsul Falah, M.Pd.
- **Kiai Muda**: Muhammad Faiz, S.Ag.

## Struktur Folder
```
/Users/fikrikhairulshaleh/Valet/khozin/
├── CLAUDE.md                     # Dokumentasi project ini
├── naskah-sup-ozin.pdf          # Proposal skripsi
├── docs/                        # Dokumentasi UML & Diagram
│   ├── use-case-diagram.md
│   ├── erd-diagram.md
│   ├── flowchart-diagram.md
│   └── class-diagram.md
│
├── PembelajaranKaidah/          # Mobile App (Java/Android)
│   ├── app/
│   │   └── src/
│   │       └── main/
│   │           ├── java/        # Source code Java
│   │           │   ├── com/pembelajarankaidah/
│   │           │   │   ├── data/
│   │           │   │   │   ├── model/
│   │           │   │   │   ├── repository/
│   │           │   │   │   ├── lokal/     # Room Database
│   │           │   │   │   └── remote/    # API Service
│   │           │   │   ├── ui/
│   │           │   │   │   ├── login/
│   │           │   │   │   ├── home/
│   │           │   │   │   ├── kaidah/
│   │           │   │   │   ├── kuis/
│   │           │   │   │   └── profil/
│   │           │   │   └── utils/
│   │           │   │       └── AlgoritmaLCM.java
│   │           ├── res/         # Resources (layout, drawable, etc)
│   │           └── AndroidManifest.xml
│   ├── build.gradle
│   └── settings.gradle
│
└── pembelajaran-kaidah-web/     # Web App (CodeIgniter 4)
    ├── app/
    │   ├── Config/              # Konfigurasi aplikasi
    │   ├── Controllers/         # Controller untuk routing
    │   ├── Models/              # Model untuk database
    │   ├── Views/               # View templates
    │   ├── Database/            # Migrations & Seeds
    │   ├── Helpers/             # Helper functions
    │   ├── Libraries/           # Custom libraries (LCM Algorithm)
    │   └── Services/            # Business logic services
    ├── public/                  # Public assets (CSS, JS, images)
    └── writable/                # Cache, logs, uploads
```

---

## Design System & UI/UX Guidelines

### Color Palette - Soft Green Theme

#### Primary Colors
```css
--primary-50:  #E8F5E9   /* Lightest - backgrounds */
--primary-100: #C8E6C9   /* Light - hover states */
--primary-200: #A5D6A7   /* Light accent */
--primary-300: #81C784   /* Medium light */
--primary-400: #66BB6A   /* Medium */
--primary-500: #4CAF50   /* Main brand color */
--primary-600: #43A047   /* Main dark */
--primary-700: #388E3C   /* Dark - active states */
--primary-800: #2E7D32   /* Darker */
--primary-900: #1B5E20   /* Darkest - text on light bg */
```

#### Secondary Colors
```css
--secondary-50:  #F1F8E9   /* Lime tint */
--secondary-100: #DCEDC8
--secondary-200: #C5E1A5
--secondary-300: #AED581
--secondary-400: #9CCC65
--secondary-500: #8BC34A   /* Secondary brand */
--secondary-600: #7CB342
--secondary-700: #689F38
--secondary-800: #558B2F
--secondary-900: #33691E
```

#### Accent & Supporting Colors
```css
--accent-teal:   #26A69A  /* Success states, achievements */
--accent-amber:  #FFA726  /* Warnings, highlights */
--accent-blue:   #42A5F5  /* Info, links */
--accent-red:    #EF5350  /* Errors, delete actions */

--neutral-50:    #FAFAFA  /* White background */
--neutral-100:   #F5F5F5  /* Light background */
--neutral-200:   #EEEEEE  /* Borders, dividers */
--neutral-300:   #E0E0E0  /* Disabled backgrounds */
--neutral-400:   #BDBDBD  /* Disabled text */
--neutral-500:   #9E9E9E  /* Placeholder text */
--neutral-600:   #757575  /* Secondary text */
--neutral-700:   #616161  /* Body text */
--neutral-800:   #424242  /* Heading text */
--neutral-900:   #212121  /* Primary text */
```

#### Semantic Colors
```css
--success:  #4CAF50   /* Correct answers */
--warning:  #FF9800   /* Needs attention */
--error:    #F44336   /* Wrong answers */
--info:     #2196F3   /* Informational */
```

### Typography

#### Font Families
```css
/* Arabic Text */
--font-arabic: 'Amiri', 'Scheherazade New', 'Traditional Arabic', serif;

/* Latin Text - Headers */
--font-heading: 'Poppins', 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;

/* Latin Text - Body */
--font-body: 'Nunito', 'Open Sans', -apple-system, BlinkMacSystemFont, sans-serif;

/* Monospace - Code, IDs */
--font-mono: 'JetBrains Mono', 'Fira Code', 'Courier New', monospace;
```

#### Font Sizes (Mobile-First)
```css
/* Mobile */
--text-xs:   12px / 0.75rem   /* Small labels */
--text-sm:   14px / 0.875rem  /* Secondary text */
--text-base: 16px / 1rem      /* Body text */
--text-lg:   18px / 1.125rem  /* Large body */
--text-xl:   20px / 1.25rem   /* Small headings */
--text-2xl:  24px / 1.5rem    /* H3 */
--text-3xl:  30px / 1.875rem  /* H2 */
--text-4xl:  36px / 2.25rem   /* H1 */

/* Arabic text: +2px larger for better readability */
--text-arabic-base: 18px / 1.125rem
--text-arabic-lg:   20px / 1.25rem
--text-arabic-xl:   24px / 1.5rem
```

#### Font Weights
```css
--font-light:    300
--font-normal:   400
--font-medium:   500
--font-semibold: 600
--font-bold:     700
```

### Spacing System (8px Base Grid)
```css
--space-1:  4px    /* 0.25rem */
--space-2:  8px    /* 0.5rem */
--space-3:  12px   /* 0.75rem */
--space-4:  16px   /* 1rem */
--space-5:  20px   /* 1.25rem */
--space-6:  24px   /* 1.5rem */
--space-8:  32px   /* 2rem */
--space-10: 40px   /* 2.5rem */
--space-12: 48px   /* 3rem */
--space-16: 64px   /* 4rem */
--space-20: 80px   /* 5rem */
```

### Border Radius
```css
--radius-sm:  4px    /* Small elements */
--radius-md:  8px    /* Cards, buttons */
--radius-lg:  12px   /* Large cards */
--radius-xl:  16px   /* Modals */
--radius-2xl: 24px   /* Special elements */
--radius-full: 9999px /* Circular */
```

### Shadows (Soft & Subtle)
```css
--shadow-sm:  0 1px 2px rgba(0, 0, 0, 0.05);
--shadow-md:  0 4px 6px rgba(0, 0, 0, 0.07);
--shadow-lg:  0 10px 15px rgba(0, 0, 0, 0.08);
--shadow-xl:  0 20px 25px rgba(0, 0, 0, 0.10);

/* Colored shadows for primary actions */
--shadow-primary: 0 4px 14px rgba(76, 175, 80, 0.25);
--shadow-success: 0 4px 14px rgba(76, 175, 80, 0.30);
--shadow-error:   0 4px 14px rgba(244, 67, 54, 0.25);
```

### Component Styles

#### Buttons
```css
/* Primary Button */
.btn-primary {
  background: var(--primary-500);
  color: white;
  padding: 12px 24px;
  border-radius: var(--radius-md);
  font-weight: var(--font-semibold);
  box-shadow: var(--shadow-primary);
  transition: all 0.3s ease;
}
.btn-primary:hover {
  background: var(--primary-600);
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

/* Secondary Button */
.btn-secondary {
  background: white;
  color: var(--primary-600);
  border: 2px solid var(--primary-500);
  padding: 10px 24px;
  border-radius: var(--radius-md);
}

/* Icon Button */
.btn-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-full);
  background: var(--primary-50);
  color: var(--primary-700);
}
```

#### Cards
```css
.card {
  background: white;
  border-radius: var(--radius-lg);
  padding: var(--space-6);
  box-shadow: var(--shadow-md);
  border: 1px solid var(--neutral-200);
  transition: all 0.3s ease;
}
.card:hover {
  box-shadow: var(--shadow-lg);
  transform: translateY(-4px);
}

/* Card with Green Accent */
.card-kaidah {
  border-left: 4px solid var(--primary-500);
}
```

#### Input Fields
```css
.input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid var(--neutral-200);
  border-radius: var(--radius-md);
  font-size: var(--text-base);
  transition: all 0.3s ease;
}
.input:focus {
  border-color: var(--primary-500);
  box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
  outline: none;
}

/* Arabic Input */
.input-arabic {
  font-family: var(--font-arabic);
  font-size: var(--text-arabic-base);
  direction: rtl;
  text-align: right;
}
```

#### Progress Bars
```css
.progress-bar {
  height: 8px;
  background: var(--neutral-200);
  border-radius: var(--radius-full);
  overflow: hidden;
}
.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--primary-500), var(--secondary-500));
  border-radius: var(--radius-full);
  transition: width 0.5s ease;
}
```

### UI/UX Guidelines - Mobile App (Siswa)

#### Design Principles
1. **Simplicity**: Minimalis, fokus pada konten pembelajaran
2. **Clarity**: Informasi jelas, tidak membingungkan
3. **Consistency**: Konsisten dalam layout, warna, typography
4. **Feedback**: Selalu berikan feedback untuk setiap action
5. **Accessibility**: Mudah diakses, readable, touch-friendly

#### Screen Layouts

**1. Login Screen**
- Logo aplikasi (dengan accent hijau soft)
- Minimalis clean white background
- Input fields dengan icon
- Primary button login (full width)
- Link register di bawah

**2. Home Dashboard (Siswa)**
- Header: Avatar, nama siswa, notifikasi badge
- Welcome card dengan progress overview (circular progress)
- Quick stats: Total kaidah dipelajari, skor rata-rata, streak
- List kaidah dengan card design
- Bottom navigation: Home, Progress, History, Profile

**3. Materi Kaidah List**
- Search bar di atas
- Filter: Semua, Belum dimulai, Sedang belajar, Selesai
- Card untuk setiap kaidah:
  - Icon/ilustrasi kaidah
  - Judul kaidah (Arab + Latin)
  - Progress bar
  - Badge tingkat kesulitan
  - CTA button "Belajar" / "Lanjutkan"

**4. Detail Materi Kaidah**
- Hero section dengan judul kaidah
- Tab navigation: Penjelasan, Contoh, Latihan
- Penjelasan dengan typography Arab yang jelas
- Contoh dalam card terpisah
- CTA button "Mulai Latihan"

**5. Quiz/Latihan Soal**
- Top bar: Nomor soal, timer, progress
- Card pertanyaan (Arabic text besar, jelas)
- Pilihan jawaban dalam card terpisah
- Button "Lanjut" di bawah
- Feedback instant: Hijau (benar), Merah (salah)

**6. Hasil Quiz**
- Illustration/icon celebration
- Skor besar di tengah
- Breakdown: Benar/Salah
- Progress chart
- Button: "Ulangi" atau "Kembali ke Home"

**7. Progress Screen**
- Overview: Total skor, ranking (optional)
- Chart progress per kaidah
- List achievement badges
- Calendar view aktivitas belajar

**8. Profile Screen**
- Avatar edit
- Info personal
- Statistik pembelajaran
- Settings: Notifikasi, Bahasa, Dark mode
- Logout button

#### Interaction Patterns

**Touch Targets**
- Minimum 48x48 dp untuk semua touchable elements
- Spacing minimal 8dp antar elements

**Animations**
```
- Page transitions: Slide left/right (300ms)
- Button press: Scale 0.95 (150ms)
- Card hover: Lift up 4px dengan shadow (300ms)
- Success feedback: Bounce animation
- Error shake: Horizontal shake (400ms)
```

**Loading States**
- Skeleton screens untuk loading content
- Progress indicator untuk submit data
- Shimmer effect untuk images

### UI/UX Guidelines - Web App (Admin & Guru)

#### Design Principles
1. **Efficiency**: Cepat akses ke fungsi utama
2. **Information Density**: Tampilkan data penting di dashboard
3. **Data Visualization**: Chart & graph untuk statistik
4. **Batch Operations**: Support untuk operasi massal
5. **Responsive**: Desktop-first, responsive untuk tablet
6. **Clean Interface**: Hindari menampilkan technical ID di UI untuk user experience yang lebih baik
7. **Solid Colors**: Gunakan solid colors instead of gradients untuk konsistensi visual

#### UI Display Guidelines

**What NOT to Display in User Interface:**
- ❌ **Database IDs**: Jangan tampilkan primary key (id_materi, id_soal, id_pengguna, dll) di UI
- ❌ **Technical Fields**: Hindari field yang hanya berguna untuk debugging
- ❌ **Internal Codes**: Jangan tampilkan internal system codes

**What to Display Instead:**
- ✅ **User-Friendly Identifiers**: Gunakan field yang bermakna untuk user
  - NIS untuk siswa (bukan `id_siswa`)
  - Username untuk pengguna (bukan `id_pengguna`)
  - Judul kaidah untuk materi (bukan `id_materi`)
  - Nomor urutan untuk daftar (bukan database ID)
- ✅ **Meaningful Information**: Tampilkan data yang berguna untuk user
- ✅ **Business Logic Fields**: Field yang relevan dengan proses bisnis

**Examples:**
```php
// ❌ BAD - Menampilkan database ID
echo "ID Materi: " . $kaidah['id_materi'];

// ✅ GOOD - Menampilkan informasi yang bermakna
echo "Materi #" . $kaidah['urutan'] . ": " . $kaidah['judul_kaidah'];
```

**Benefits:**
- **Better UX**: User tidak bingung dengan technical information
- **Cleaner Interface**: Tampilan lebih rapi dan profesional
- **Security**: Mengurangi exposure struktur database ke user
- **Internationalization**: Memudahkan untuk lokalisasi ke bahasa lain

#### UI Styling Guidelines

**Color Usage Principles:**
- ✅ **Solid Colors**: Gunakan solid colors untuk semua elemen UI
- ✅ **Consistent Palette**: Gunakan color palette yang sudah ditentukan
- ✅ **Semantic Colors**: Gunakan colors yang memiliki makna (success=green, danger=red, dll)

**What AVOID in Styling:**
- ❌ **Gradients**: Jangan gunakan linear-gradient atau radial-gradient
- ❌ **Complex Patterns**: Hindari pola yang terlalu kompleks
- ❌ **Inconsistent Colors**: Jangan sembarangan memilih colors

**Examples:**
```css
/* ❌ BAD - Using gradients */
.btn-primary {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-header {
    background: linear-gradient(90deg, #4CAF50, #8BC34A);
}

/* ✅ GOOD - Using solid colors */
.btn-primary {
    background: #4CAF50;
}

.card-header {
    background: #4CAF50;
}
```

**Approved Color Palette:**
```css
/* Primary Colors */
--primary: #4CAF50;          /* Main brand color */
--primary-dark: #388E3C;     /* Darker variant */
--primary-light: #81C784;    /* Lighter variant */

/* Semantic Colors */
--success: #4CAF50;          /* Success states */
--warning: #FF9800;          /* Warning states */
--danger: #F44336;           /* Error/danger states */
--info: #2196F3;             /* Information states */

/* Neutral Colors */
--white: #FFFFFF;
--gray-100: #F5F5F5;
--gray-200: #EEEEEE;
--gray-500: #9E9E9E;
--gray-800: #424242;
--black: #212121;
```

**Benefits of Solid Colors:**
- **Performance**: Faster rendering dan loading
- **Consistency**: Tampilan yang konsisten across browsers
- **Accessibility**: Better contrast ratios
- **Professional Look**: Clean dan modern appearance
- **Maintainability**: Easier to modify dan debug

---

## 🎯 Tenant UI/UX Pattern Framework (Based on Siswa Management)

### Pattern Overview
Pattern ini dihasilkan dari analisis mendalam pada **Manajemen Siswa** dan dapat dijadikan template standar untuk semua modul lain (Kaidah, Soal, Pengguna, dll).

## 🧩 Reusable Stats Card Components (DRY Implementation)

### Overview
Untuk menghindari duplikasi kode dan mengikuti prinsip DRY (Don't Repeat Yourself), telah dibuat komponen reusable untuk statistics cards yang digunakan di seluruh modul.

### Components Structure
```
app/Views/partials/
├── stats_card.php    # Individual stats card component
└── stats_row.php     # Wrapper untuk multiple stats cards
```

### Individual Stats Card Component (`partials/stats_card.php`)
**Usage:**
```php
<?= view('partials/stats_card', [
    'title' => 'Total Siswa',
    'value' => $stats['total'] ?? 0,
    'subtitle' => 'Terdaftar',
    'icon' => 'users',
    'variant' => 'primary'
]) ?>
```

**Available Parameters:**
- `title` (required): Card title/label
- `value` (required): Main statistic value
- `subtitle` (optional): Small subtitle text
- `icon` (required): Tabler icon name (tanpa 'ti ti-')
- `variant` (required): primary, success, warning, info, danger
- `columnClass` (optional): Bootstrap column class (auto-set)
- `attributes` (optional): Additional HTML attributes

### Stats Row Wrapper Component (`partials/stats_row.php`)
**Usage:**
```php
<?= view('partials/stats_row', [
    'stats' => [
        [
            'title' => 'Total Siswa',
            'value' => $stats['total'] ?? 0,
            'subtitle' => 'Terdaftar',
            'icon' => 'users',
            'variant' => 'primary'
        ],
        [
            'title' => 'Aktif',
            'value' => $stats['aktif'] ?? 0,
            'subtitle' => 'Siswa',
            'icon' => 'circle-check',
            'variant' => 'success'
        ]
        // ... more stats
    ]
]) ?>
```

**Features:**
- ✅ **Automatic Column Sizing:** Menyesuaikan berdasarkan jumlah stats cards
  - 1 card = `col-12` (full width)
  - 2 cards = `col-md-6` (half width)
  - 3 cards = `col-md-4` (one-third width)
  - 4+ cards = `col-md-3` (quarter width)
- ✅ **Error Handling:** Validasi input data
- ✅ **Default Values:** Otomatis menggunakan nilai default jika kosong

### Modules Using Components
- ✅ **Siswa Management** (`siswa/index.php`)
- ✅ **Pengguna Management** (`pengguna/index.php`)
- ✅ **Guru Management** (`guru/index.php`)
- ✅ **Kaidah Management** (`kaidah/index.php`)
- ✅ **Soal Management** (`soal/index.php`)

### Code Reduction Impact
- **Before:** ~267 lines of duplicated HTML
- **After:** ~121 lines total
- **Eliminated:** ~146 lines (55% reduction)
- **Maintenance:** Single source of truth untuk stats cards styling

### Benefits
1. **DRY Principle:** Tidak ada duplikasi kode
2. **Consistency:** Semua stats cards menggunakan styling yang sama
3. **Maintainability:** Perubahan styling hanya perlu dilakukan di satu tempat
4. **Reusability:** Mudah digunakan di modul baru
5. **Flexibility:** Banyak pilihan parameter untuk kustomisasi

## 📋 Pengguna Management Interface Updates

### Recent Changes (November 2025)
Interface manajemen pengguna telah diperbarui untuk meningkatkan user experience dan mengurangi kompleksitas tampilan.

### Table Structure Changes
**Columns Removed:**
- ❌ **Email** - Kolom email dihapus untuk menyederhanakan tampilan

**Updated Table Structure:**
| Column | Description | Status |
|--------|-------------|--------|
| ID | User ID | ✅ Active |
| Username | Username dengan avatar | ✅ Active |
| Nama Lengkap | Full name | ✅ Active |
| Role | Admin/Guru badge | ✅ Active |
| Status | AKTIF/NONAKTIF badge | ✅ Active |
| Aksi | Action buttons | ✅ Updated |

### Action Buttons Enhancement
**New Format: Icon + Text**
Semua tombol aksi sekarang menggunakan format ikon + teks untuk meningkatkan kejelasan:

```html
<!-- Detail Button -->
<a href="<?= site_url('pengguna/show/' . $id) ?>" class="btn btn-sm btn-info me-1">
    <i class="ti ti-eye me-1"></i>Detail
</a>

<!-- Edit Button -->
<a href="<?= site_url('pengguna/edit/' . $id) ?>" class="btn btn-sm btn-warning me-1">
    <i class="ti ti-edit me-1"></i>Edit
</a>

<!-- Status Toggle Button -->
<button type="button" class="btn btn-sm btn-success me-1" onclick="toggleStatus(<?= $id ?>)">
    <i class="ti ti-toggle-<?= $status === 'AKTIF' ? 'left' : 'right' ?> me-1"></i>Status
</button>

<!-- Delete Button -->
<button type="button" class="btn btn-sm btn-danger" onclick="confirmDelete(<?= $id ?>)">
    <i class="ti ti-trash me-1"></i>Hapus
</button>
```

### Benefits of Updates
1. **Cleaner Interface:** Menghapus kolom email mengurangi clutter visual
2. **Better UX:** Tombol dengan ikon + teks lebih mudah dipahami
3. **Consistent Pattern:** Mengikuti pattern yang sama dengan modul lain
4. **Responsive:** Lebih baik di layar kecil dengan kolom yang lebih sedikit

### Technical Details
- **Empty State Colspan:** Diupdate dari `colspan="7"` ke `colspan="6"`
- **Table Headers:** Menghapus header "Email"
- **Data Rows:** Menghapus cell data email
- **Button Styling:** Menggunakan `me-1` class untuk spacing antara ikon dan teks

## 🔧 Bug Fixes & Improvements

### Siswa Edit Form Status Field Fix (November 2025)
**Problem:** Status field di form edit siswa tidak menampilkan selected value yang benar.

**Root Cause:**
- Database menyimpan status dalam format UPPERCASE (`AKTIF`, `NONAKTIF`)
- Form checking menggunakan lowercase (`'aktif'`, `'nonaktif'`)
- Mismatch causing selection failure

**Fix Applied:**
```php
// Before (incorrect)
<option value="aktif" <?= ($siswa['status'] === 'aktif') ? 'selected' : '' ?>>Aktif</option>
<option value="nonaktif" <?= ($siswa['status'] === 'nonaktif') ? 'selected' : '' ?>>Nonaktif</option>

// After (correct)
<option value="AKTIF" <?= ($siswa['status'] === 'AKTIF') ? 'selected' : '' ?>>Aktif</option>
<option value="NONAKTIF" <?= ($siswa['status'] === 'NONAKTIF') ? 'selected' : '' ?>>Nonaktif</option>
```

**Files Fixed:**
- `app/Views/siswa/edit.php:113-114` - Status dropdown selection
- `app/Views/siswa/edit.php:56` - Info badge status display

**Impact:**
- ✅ Status field sekarang menampilkan value yang benar saat edit
- ✅ Status badge di info section menampilkan warna yang benar
- ✅ Konsistensi dengan database format (UPPERCASE)

**Best Practice Note:**
Selalu gunakan format yang konsisten dengan database. Jika database menggunakan UPPERCASE untuk enum values, form harus menggunakan values yang sama.

### Date Helper Functions Implementation (November 2025)
**Problem:** Fungsi `format_date_time()` dan fungsi date/time lainnya tidak tersedia, menyebabkan error "Call to undefined function".

**Root Cause:**
- Helper functions belum diimplementasikan secara benar di CodeIgniter 4
- Namespace conflict dalam file helper
- Konfigurasi autoloading tidak sesuai

**Solution Implemented:**
```php
// app/Helpers/Date_helper.php (procedural, no namespace)
<?php
if (!function_exists('format_date_time')) {
    function format_date_time($datetime, $format = 'd M Y H:i:s') {
        if (!$datetime) return '-';
        return date($format, strtotime($datetime));
    }
}

if (!function_exists('time_ago')) {
    function time_ago($datetime) {
        if (!$datetime) return 'Tidak ada data';
        $time = strtotime($datetime);
        $now = time();
        $diff = $now - $time;

        if ($diff < 60) return 'Baru saja';
        elseif ($diff < 3600) return floor($diff / 60) . ' menit yang lalu';
        elseif ($diff < 86400) return floor($diff / 3600) . ' jam yang lalu';
        // ... more logic
    }
}

if (!function_exists('calculate_days_since')) {
    function calculate_days_since($datetime) {
        if (!$datetime) return 0;
        $created = strtotime($datetime);
        $today = time();
        $days = floor(($today - $created) / (60 * 60 * 24));
        return max(1, $days);
    }
}
```

**Configuration Update:**
```php
// app/Config/Autoload.php
public $helpers = [
    'date_helper'  // Changed from 'date' to avoid conflicts
];
```

**Files Fixed:**
- `app/Helpers/Date_helper.php` - Created procedural helper functions
- `app/Config/Autoload.php` - Updated helper configuration
- Renamed from `DateHelper.php` to `Date_helper.php` for CI4 conventions

**Impact:**
- ✅ DRY principle implementation untuk date/time functions
- ✅ Fungsi `format_date_time()` tersedia di semua views
- ✅ Fungsi `time_ago()` untuk relative time display
- ✅ Fungsi `calculate_days_since()` untuk statistics
- ✅ Consistent date formatting across application

### UI/UX Enhancements - Avatar & Table Borders (November 2025)

#### Avatar Icon Enhancement
**Problem:** Avatar icons di halaman show (guru & pengguna) terlalu kecil dan tidak proporsional dengan layout modern.

**Solution Applied:**
```php
<!-- Before -->
<div class="avatar-xl bg-primary text-white rounded-circle">
    <i class="ti ti-user fs-1"></i>
</div>

<!-- After -->
<div class="avatar-xxl bg-primary text-white rounded-circle d-flex align-items-center justify-content-center mx-auto mb-3">
    <i class="ti ti-user" style="font-size: 6rem;"></i>
</div>
```

**CSS Addition:**
```css
.avatar-xxl {
    width: 180px;
    height: 180px;
}
```

**Files Updated:**
- `app/Views/guru/show.php:38-40, 326-329` - Enhanced avatar and statistics icons
- `app/Views/pengguna/show.php:38-40, 325-328` - Enhanced avatar with CSS support

**Impact:**
- ✅ Avatar icons 50% larger (180px × 180px vs 120px × 120px)
- ✅ Perfect circular shape maintained
- ✅ Better visual hierarchy in profile pages
- ✅ Modern, professional appearance

#### Statistics Card Icons Enhancement (Guru Show)
**Icons upscalled for better visibility:**
- Calendar: `fs-2` → `style="font-size: 5rem;"`
- Shield-check: `fs-2` → `style="font-size: 5rem;"`
- School: `fs-2` → `style="font-size: 5rem;"`
- Clock: `fs-2` → `style="font-size: 5rem;"`

#### UI Cleanup - Guru Show Page
**Removed unnecessary sections:**
- ❌ **"Tindakan Cepat"** section - Simplified interface
- ❌ **"Navigasi"** section - Reduced visual clutter

#### Table Border Implementation
**Problem:** Tabel di halaman index tidak memiliki border yang jelas, mengurangi readability data.

**Solution Applied:**
```html
<!-- Before -->
<table class="table text-nowrap mb-0 align-middle datatable">
    <th class="border-bottom-0">ID</th>
    <td class="border-bottom-0">Data</td>

<!-- After -->
<table class="table table-bordered text-nowrap mb-0 align-middle datatable">
    <th>ID</th>
    <td>Data</td>
```

**Files Updated:**
- `app/Views/guru/index.php:61, 64-69, 75, 88, 96` - Added table-bordered, removed border-bottom-0
- `app/Views/pengguna/index.php:61, 64-70, 76, 89, 97, 102` - Consistent table styling
- `app/Views/siswa/index.php:68, 71-76, 83, 86, 96, 101, 109` - Unified table appearance

**Benefits:**
1. **Enhanced Readability:** Grid structure yang jelas antara sel
2. **Visual Separation:** Border yang konsisten memisahkan data
3. **Professional Appearance:** Tabel terstruktur dengan rapi
4. **Consistency:** Semua halaman index menggunakan styling yang sama
5. **Better Data Scanning:** User dapat dengan mudah memindai data baris/kolom

#### Consistency Maintenance
**Action Buttons Alignment:**
- **Decision:** Maintained left-aligned action buttons across all index pages
- **Reasoning:** Consistency with existing pengguna index pattern
- **Result:** Unified user experience across all management modules

**Files Affected:**
- **Guru Index:** ✅ table-bordered, left-aligned actions
- **Pengguna Index:** ✅ table-bordered, left-aligned actions
- **Siswa Index:** ✅ table-bordered, left-aligned actions

### CodeIgniter 4 Validation Rule Format Fix (November 2025)
**Problem:** Error "The field must be in the format 'table.field' or 'dbGroup.table.field'" saat update data siswa.

**Root Cause:**
- CodeIgniter 4 memerlukan format spesifik untuk validation rule `is_unique`
- Format yang salah: `is_unique[table,field,except_id]`
- Format yang benar: `is_unique[table.field,field,except_id]`

**Fix Applied:**
```php
// Before (incorrect)
'nis' => 'required|is_unique[siswa,nis]',                           // Store method
'nis' => "required|is_unique[siswa,nis,{$id}]",                    // Update method

// After (correct)
'nis' => 'required|is_unique[siswa.nis]',                          // Store method
'nis' => "required|is_unique[siswa.nis,nis,{$id}]",                 // Update method
```

**Files Fixed:**
- `app/Controllers/SiswaController.php:61` - Store method validation
- `app/Controllers/SiswaController.php:102` - Update method validation

**Impact:**
- ✅ Validasi NIS unik sekarang berfungsi dengan benar
- ✅ Error saat update siswa sudah teratasi
- ✅ Consistent dengan CodeIgniter 4 best practices

**CodeIgniter 4 Validation Rule Format Reference:**
```php
// Basic is_unique
'username' => 'required|is_unique[users.username]'

// is_unique with exception (for update)
'username' => "required|is_unique[users.username,username,{$id}]"
```

**Best Practice Note:**
Gunakan format `table.field` untuk semua validation rule yang memerlukan referensi tabel di CodeIgniter 4.

### Guru Management Implementation (November 2025)
**Problem:** Need to separate teacher management from user management while using the same database table.

**Root Cause:**
- Existing `pengguna` table contains both admin and teacher records
- User requested separate management interfaces for different roles
- Need role-based filtering while maintaining data integrity

**Solution Implemented:**
```php
// Custom model methods instead of overriding built-in methods
class GuruModel extends Model
{
    public function getAllGurus(?int $limit = null, int $offset = 0)
    {
        return $this->where('hak_akses', 'GURU')
                     ->orderBy('waktu_dibuat', 'DESC')
                     ->findAll($limit, $offset);
    }

    public function getGuruById($id)
    {
        return $this->where('id_pengguna', $id)
                     ->where('hak_akses', 'GURU')
                     ->first();
    }
}
```

**Key Features Implemented:**
- ✅ **Role Filtering**: Automatic filtering by `hak_akses = 'GURU'`
- ✅ **Complete CRUD**: Create, Read, Update, Delete operations
- ✅ **Statistics Dashboard**: Teacher-specific statistics
- ✅ **Enhanced Alert System**: Custom confirm dialogs with Notyf.js
- ✅ **Route Protection**: Only admin can access teacher management
- ✅ **Status Toggle**: Quick activate/deactivate functionality

**Route Configuration:**
```php
// app/Config/Routes.php
$routes->group('guru', ['filter' => 'auth:admin'], function($routes) {
    $routes->get('/', 'GuruController::index');
    $routes->post('(:num)/toggleStatus', 'GuruController::toggleStatus/$1');
    $routes->post('delete/(:num)', 'GuruController::delete/$1');
    // ... other routes
});
```

**Enhanced Alert System Integration:**
```javascript
// Custom confirm dialog with loading states
function toggleStatus(id) {
    toast.confirm(
        `Apakah Anda yakin ingin mengubah status guru ini?`,
        function() {
            const loading = toast.loading('Mengubah status...');

            fetch(`/guru/${id}/toggleStatus`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    toast.success('Status berhasil diperbarui!');
                    setTimeout(() => location.reload(), 1000);
                } else {
                    toast.error(data.message);
                }
            })
            .finally(() => loading.dismiss());
        }
    );
}
```

**Files Created/Modified:**
- `app/Controllers/GuruController.php` - Complete CRUD controller
- `app/Models/GuruModel.php` - Custom model methods (no overriding)
- `app/Views/guru/` - Complete view files (index, create, edit, show)
- `app/Config/Routes.php` - Route configuration
- `app/Views/layouts/sidebar.php` - Navigation menu addition

**Benefits Achieved:**
- 🎯 **Clear Separation**: Teacher management separate from admin management
- 🔒 **Role Security**: Proper access control and role filtering
- 🎨 **Consistent UI**: Follows existing application patterns
- 📊 **Statistics**: Real-time teacher statistics and metrics
- 🔄 **Enhanced UX**: Modern confirm dialogs and loading states

**Best Practice Note:**
When creating role-specific management interfaces, use custom model methods instead of overriding CodeIgniter built-in methods to avoid signature compatibility issues.

## 📝 DRY Validation Rules Implementation (November 2025)

### Problem Solved
Validation rules di CodeIgniter 4 seringkali mengalami duplikasi kode yang menyebabkan:
- Maintenance yang sulit
- Inconsistensi error messages
- Code yang tidak efisien

### Solution Implemented: Base Validation Rules dengan Pattern Merge
Implementasi pattern DRY (Don't Repeat Yourself) untuk validation rules menggunakan base array dan merge pattern.

### Implementation Structure
```php
class SiswaController extends BaseController
{
    // Base validation rules untuk menghindari DRY
    private $siswaBaseRules = [
        'nis' => [
            'rules' => 'required|min_length[5]|max_length[20]',
            'errors' => [
                'required' => 'NIS wajib diisi',
                'min_length' => 'NIS minimal 5 karakter',
                'max_length' => 'NIS maksimal 20 karakter'
            ]
        ],
        'nama_lengkap' => [
            'rules' => 'required|min_length[3]|max_length[100]',
            'errors' => [
                'required' => 'Nama lengkap wajib diisi',
                'min_length' => 'Nama lengkap minimal 3 karakter',
                'max_length' => 'Nama lengkap maksimal 100 karakter'
            ]
        ],
        'jenis_kelamin' => [
            'rules' => 'required|in_list[L,P]',
            'errors' => [
                'required' => 'Jenis kelamin wajib dipilih',
                'in_list' => 'Jenis kelamin harus Laki-laki atau Perempuan'
            ]
        ],
        'kelas' => [
            'rules' => 'required|max_length[10]',
            'errors' => [
                'required' => 'Kelas wajib diisi',
                'max_length' => 'Kelas maksimal 10 karakter'
            ]
        ]
    ];
}
```

### Usage Patterns

#### **Store Method Pattern**
```php
public function store()
{
    // Clone base rules dan tambahkan is_unique untuk NIS
    $rules = $this->siswaBaseRules;
    $rules['nis']['rules'] .= '|is_unique[siswa.nis]';
    $rules['nis']['errors']['is_unique'] = 'NIS sudah digunakan, gunakan NIS lain';

    if (!$this->validate($rules)) {
        return redirect()->back()->withInput()->with('errors', $this->validator->getErrors());
    }
}
```

#### **Update Method Pattern**
```php
public function update($id)
{
    // Clone base rules dan modifikasi untuk update
    $rules = $this->siswaBaseRules;

    // Tambahkan is_unique dengan exception untuk current record
    $rules['nis']['rules'] .= "|is_unique[siswa.nis,nis,{$id}]";
    $rules['nis']['errors']['is_unique'] = 'NIS sudah digunakan oleh siswa lain';

    // Tambahkan status field untuk update
    $rules['status'] = [
        'rules' => 'required|in_list[AKTIF,NONAKTIF]',
        'errors' => [
            'required' => 'Status wajib dipilih',
            'in_list' => 'Status harus Aktif atau Nonaktif'
        ]
    ];

    if (!$this->validate($rules)) {
        return redirect()->back()->withInput()->with('errors', $this->validator->getErrors());
    }
}
```

### Benefits Achieved
1. **Code Reduction:** ~50% reduction in validation code (40 lines → 15 lines per method)
2. **Maintainability:** Base rules changes affect all methods
3. **Consistency:** Consistent error messages across all forms
4. **Flexibility:** Easy to add/modify rules per method
5. **DRY Principle:** Single source of truth for validation rules

### Error Messages in Indonesian
All validation error messages now use clear Indonesian language:
- ✅ "NIS wajib diisi"
- ✅ "NIS sudah digunakan, gunakan NIS lain"
- ✅ "NIS sudah digunakan oleh siswa lain"
- ✅ "Nama lengkap wajib diisi"
- ✅ "Jenis kelamin wajib dipilih"
- ✅ "Status wajib dipilih"

### Best Practice Template for Future Controllers
```php
// Base validation pattern template
private $modelNameBaseRules = [
    'field1' => [
        'rules' => 'validation_rules',
        'errors' => [
            'required' => 'Field wajib diisi',
            // ... other error messages
        ]
    ],
    // ... other base fields
];

// Usage in methods
$rules = $this->modelNameBaseRules;
$rules['field1']['rules'] .= '|additional_rule';
$rules['field1']['errors']['additional_rule'] = 'Error message in Indonesian';
```

### Files Modified
- `app/Controllers/SiswaController.php` - Implemented DRY validation pattern

**Best Practice Note:** Gunakan base validation rules dengan merge pattern untuk semua controllers yang memiliki similar validation requirements.

### 1. Page Structure Pattern

#### A. List/Index Page Structure
```html
<!-- 1. Page Header -->
<div class="mb-4">
    <h4 class="fw-bold text-dark">[Module Name]</h4>
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="<?= site_url('dashboard') ?>">Dashboard</a></li>
            <li class="breadcrumb-item active">[Module Name]</li>
        </ol>
    </nav>
</div>

<!-- 2. Statistics Cards (Optional but recommended) -->
<div class="row mb-4">
    <div class="col-md-3">
        <div class="card stats-card stats-card-primary border-0 d-print-none">
            <div class="card-body">
                <div class="d-flex align-items-center">
                    <div class="stats-icon">
                        <i class="ti ti-[icon] text-white"></i>
                    </div>
                    <div class="ms-3">
                        <h6 class="mb-0">[Label]</h6>
                        <h2 class="mb-0 fw-bold"><?= $stats['key'] ?></h2>
                        <small>[Unit/Description]</small>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- ... more stat cards ... -->
</div>

<!-- 3. Action Section -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h4 class="fw-bold text-dark">Data [Module]</h4>
        <small class="text-muted">[Help text/instruction]</small>
    </div>
    <div>
        <a href="<?= site_url('[module]/create') ?>" class="btn btn-primary">
            <i class="ti ti-circle-plus me-2"></i>Tambah [Module]
        </a>
    </div>
</div>

<!-- 4. Data Table/Card -->
<div class="card border-0 shadow-sm dataTables-card">
    <div class="card-body">
        <!-- DataTable implementation -->
    </div>
</div>
```

#### B. Create/Edit Form Structure
```html
<!-- 1. Page Header -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h4 class="fw-bold text-dark">[Action] [Module Name]</h4>
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb mb-0">
                <li class="breadcrumb-item"><a href="<?= site_url('dashboard') ?>">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="<?= site_url('[module] ?>">[Module Name]</a></li>
                <li class="breadcrumb-item active">[Action]</li>
            </ol>
        </nav>
    </div>
    <div>
        <a href="<?= site_url('[module]') ?>" class="btn btn-secondary">
            <i class="ti ti-arrow-left me-2"></i>Kembali
        </a>
    </div>
</div>

<!-- 2. Form Card -->
<div class="card border-0 shadow-sm">
    <div class="card-body p-4">
        <!-- Flash Messages -->
        <?= $this->include('partials/flash_messages') ?>

        <!-- Alert/Info (for edit forms) -->
        <div class="alert alert-info d-flex align-items-center" role="alert">
            <i class="ti ti-info-circle me-2"></i>
            <div><strong>Info:</strong> [Context information]</div>
        </div>

        <!-- Form -->
        <form method="[method]" action="[action]" class="needs-validation" novalidate>
            <!-- Form fields organized in rows -->
        </form>
    </div>
</div>
```

### 2. DataTable Pattern (Client-Side - Recommended)

#### A. DataTable Structure (Client-Side Pattern)
```html
<table id="[module]Table" class="table text-nowrap mb-0 align-middle datatable">
    <thead class="text-dark">
        <tr>
            <th class="border-bottom-0">ID</th>
            <th class="border-bottom-0">[Main Column]</th>
            <th class="border-bottom-0">[Secondary Column]</th>
            <th class="border-bottom-0">[Status Column]</th>
            <th class="border-bottom-0">[Created Column]</th>
            <th class="border-bottom-0 text-center">Aksi</th>
        </tr>
    </thead>
    <tbody>
        <?php if (empty($[module])): ?>
        <tr>
            <td colspan="[column_count]" class="text-center py-5">
                <i class="ti ti-inbox fs-1 text-muted mb-3"></i>
                <h5 class="text-muted">[Empty Message]</h5>
                <p class="text-muted">[Description]</p>
                <a href="<?= site_url('[module]/create') ?>" class="btn btn-primary">
                    <i class="ti ti-circle-plus me-2"></i>[Action Button]
                </a>
            </td>
        </tr>
        <?php else: ?>
            <?php foreach ($[module] as $item): ?>
            <tr>
                <td><?= $item['id_field'] ?></td>
                <td><?= esc($item['field_name']) ?></td>
                <td><?= esc($item['field_name2']) ?></td>
                <td>
                    <?php if ($item['status'] === 'AKTIF'): ?>
                        <span class="badge bg-success rounded-3">Aktif</span>
                    <?php else: ?>
                        <span class="badge bg-secondary rounded-3">Nonaktif</span>
                    <?php endif; ?>
                </td>
                <td><small><?= date('d M Y', strtotime($item['created_date'])) ?></small></td>
                <td class="text-center">
                    <div class="table-actions">
                        <a href="<?= site_url('[module]/show/' . $item['id']) ?>"
                           class="btn btn-sm btn-info me-1" title="Detail">
                            <i class="ti ti-eye"></i>
                        </a>
                        <a href="<?= site_url('[module]/edit/' . $item['id']) ?>"
                           class="btn btn-sm btn-warning me-1" title="Edit">
                            <i class="ti ti-edit"></i>
                        </a>
                        <button type="button" class="btn btn-sm btn-success me-1"
                                onclick="toggleStatus(<?= $item['id'] ?>)" title="Ubah Status">
                            <i class="ti ti-toggle-<?= $item['status'] === 'AKTIF' ? 'left' : 'right' ?>"></i>
                        </button>
                        <button type="button" class="btn btn-sm btn-danger"
                                onclick="confirmDelete(<?= $item['id'] ?>)" title="Hapus">
                            <i class="ti ti-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
            <?php endforeach; ?>
        <?php endif; ?>
    </tbody>
</table>
```

#### B. DataTables JavaScript Initialization (Client-Side)
```javascript
// DataTables will be auto-initialized by datatables-helper.js
$(document).ready(function() {
    // Initialize DataTable
    if (typeof $.fn.DataTable !== 'undefined') {
        $('#[module]Table').DataTable({
            "language": {
                "processing": "Sedang memproses...",
                "search": "Cari:",
                "lengthMenu": "Tampilkan _MENU_ data",
                "info": "Menampilkan _START_ sampai _END_ dari _TOTAL_ data",
                "zeroRecords": "Tidak ada data yang ditemukan",
                "emptyTable": "Tidak ada data dalam tabel",
                "paginate": {
                    "first": "Pertama",
                    "last": "Terakhir",
                    "next": "Selanjutnya",
                    "previous": "Sebelumnya"
                }
            }
        });
    }
});

// Toggle status function
function toggleStatus(id) {
    fetch(`<?= site_url('[module]/toggleStatus/') ?>${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'success') {
            location.reload();
        } else {
            alert(data.message || 'Gagal mengubah status');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Terjadi kesalahan saat mengubah status');
    });
}

// Confirm delete function
function confirmDelete(id) {
    if (confirm('Apakah Anda yakin ingin menghapus data ini?')) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = `<?= site_url('[module]/delete/') ?>${id}`;

        const csrfToken = document.querySelector('meta[name="csrf-token"]');
        if (csrfToken) {
            const csrfInput = document.createElement('input');
            csrfInput.type = 'hidden';
            csrfInput.name = '<?= csrf_token() ?>';
            csrfInput.value = '<?= csrf_hash() ?>';
            form.appendChild(csrfInput);
        }

        document.body.appendChild(form);
        form.submit();
    }
}
```

#### C. Controller Pattern (Client-Side)
```php
// In Controller - Simple data retrieval for client-side DataTables
public function index()
{
    $this->requireRole('ADMIN');

    // Get all data (no pagination needed for client-side)
    $data = $this->[module]Model->findAll();

    // Calculate statistics
    $stats = [
        'total' => $this->[module]Model->countAll(),
        'aktif' => $this->[module]Model->where('status', 'AKTIF')->countAll(),
        'nonaktif' => $this->[module]Model->where('status', 'NONAKTIF')->countAll(),
        'admin' => $this->[module]Model->where('hak_akses', 'ADMIN')->countAll()
    ];

    $this->data = array_merge($this->data, [
        'page_title' => 'Manajemen [Module]',
        '[module]' => $data,  // Data for DataTables
        'stats' => $stats
    ]);

    return view('[module]/index', $this->data);
}
```

**📖 Note: Client-Side vs Server-Side DataTables**

**Client-Side DataTables (Recommended for this project):**
- ✅ Simpler implementation
- ✅ Auto-initialization via datatables-helper.js
- ✅ Built-in search, sort, pagination
- ✅ No complex AJAX handling needed
- ✅ Perfect for small to medium datasets (< 10,000 records)
- ✅ Consistent with existing siswa pattern

**Server-Side DataTables (Use only for large datasets):**
- ❌ Complex implementation
- ❌ Manual AJAX handling required
- ❌ More backend code needed
- ✅ Better for very large datasets (> 50,000 records)

#### D. Action Buttons Pattern (Row Level)
```html
<div class="table-actions">
    <!-- Primary Action -->
    <a href="[detail_url]" class="btn btn-sm btn-info me-1" title="[Title]">
        <i class="ti ti-[icon]"></i> [Label]
    </a>

    <!-- Edit Action -->
    <a href="[edit_url]" class="btn btn-sm btn-warning me-1" title="Edit">
        <i class="ti ti-edit"></i> Edit
    </a>

    <!-- Secondary Action -->
    <button type="button" class="btn btn-sm btn-secondary me-1"
            onclick="[action_function]" title="[Title]">
        <i class="ti ti-[icon]"></i> [Label]
    </button>

    <!-- Delete Action -->
    <button type="button" class="btn btn-sm btn-danger"
            onclick="confirmDelete([id])" title="Hapus">
        <i class="ti ti-trash"></i> Hapus
    </button>
</div>
```

### 3. Best Practices for DataTables Implementation

#### ✅ **Recommended: Client-Side DataTables Pattern**
**Use when:**
- Dataset size: < 10,000 records
- Need rapid development
- Want consistent UI across modules
- Building standard CRUD applications

**Benefits:**
- 🚀 **Faster Development**: Minimal code required
- 🔧 **Auto-Initialization**: `datatables-helper.js` handles setup
- 📱 **Responsive**: Built-in mobile support
- 🔍 **Built-in Features**: Search, sort, pagination included
- 🎯 **Consistency**: Same pattern across all modules

**Implementation (Complete Pattern):**
```html
<!-- Page Header -->
<div class="mb-4">
    <h4 class="fw-bold text-dark">[Module Name]</h4>
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="<?= site_url('dashboard') ?>" class="text-muted">Dashboard</a></li>
            <li class="breadcrumb-item active">[Module Name]</li>
        </ol>
    </nav>
</div>

<!-- Statistics Cards -->
<div class="row mb-4">
    <div class="col-md-3">
        <div class="card stats-card stats-card-primary border-0 d-print-none">
            <div class="card-body">
                <div class="d-flex align-items-center">
                    <div class="stats-icon">
                        <i class="ti ti-[icon-total] text-white"></i>
                    </div>
                    <div class="ms-3">
                        <h6 class="mb-0">Total [Module]</h6>
                        <h2 class="mb-0 fw-bold"><?= $stats['total'] ?? 0 ?></h2>
                        <small>Terdaftar</small>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card stats-card stats-card-success border-0 d-print-none">
            <div class="card-body">
                <div class="d-flex align-items-center">
                    <div class="stats-icon">
                        <i class="ti ti-circle-check text-white"></i>
                    </div>
                    <div class="ms-3">
                        <h6 class="mb-0">Aktif</h6>
                        <h2 class="mb-0 fw-bold"><?= $stats['aktif'] ?? 0 ?></h2>
                        <small>[Module]</small>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card stats-card stats-card-warning border-0 d-print-none">
            <div class="card-body">
                <div class="d-flex align-items-center">
                    <div class="stats-icon">
                        <i class="ti ti-circle-x text-white"></i>
                    </div>
                    <div class="ms-3">
                        <h6 class="mb-0">Nonaktif</h6>
                        <h2 class="mb-0 fw-bold"><?= $stats['nonaktif'] ?? 0 ?></h2>
                        <small>[Module]</small>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card stats-card stats-card-info border-0 d-print-none">
            <div class="card-body">
                <div class="d-flex align-items-center">
                    <div class="stats-icon">
                        <i class="ti ti-[icon-special] text-white"></i>
                    </div>
                    <div class="ms-3">
                        <h6 class="mb-0">[Special Metric]</h6>
                        <h2 class="mb-0 fw-bold"><?= $stats['special'] ?? 0 ?></h2>
                        <small>[Unit]</small>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Action Buttons -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h4 class="fw-bold text-dark">Data [Module]</h4>
        <small class="text-muted">Gunakan search bar di bawah untuk mencari data</small>
    </div>
    <div>
        <a href="<?= site_url('[module]/create') ?>" class="btn btn-primary">
            <i class="ti ti-circle-plus me-2"></i>Tambah [Module]
        </a>
    </div>
</div>

<!-- [Module] List -->
<div class="card border-0 shadow-sm dataTables-card">
    <div class="card-body">
        <div class="table-responsive">
            <table id="[module]Table" class="table text-nowrap mb-0 align-middle datatable" data-type="basic">
                <thead class="text-dark">
                    <tr>
                        <th class="border-bottom-0">ID</th>
                        <th class="border-bottom-0">[Main Field]</th>
                        <th class="border-bottom-0">[Secondary Field]</th>
                        <th class="border-bottom-0">[Status Field]</th>
                        <th class="border-bottom-0">Aksi</th>
                    </tr>
                </thead>
                <tbody>
                    <?php if (!empty($[module])): ?>
                        <?php foreach ($[module] as $item): ?>
                            <tr>
                                <td class="border-bottom-0">
                                    <span class="fw-semibold"><?= esc($item['id']) ?></span>
                                </td>
                                <td class="border-bottom-0">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-circle bg-primary bg-opacity-10 p-2 me-2">
                                            <i class="ti ti-[icon] text-primary fs-4"></i>
                                        </div>
                                        <div>
                                            <div class="fw-semibold"><?= esc($item['main_field']) ?></div>
                                        </div>
                                    </div>
                                </td>
                                <td class="border-bottom-0">
                                    <span class="fw-semibold"><?= esc($item['secondary_field']) ?></span>
                                </td>
                                <td class="border-bottom-0">
                                    <span class="badge bg-<?= ($item['status'] === 'AKTIF') ? 'success' : 'secondary' ?> rounded-3">
                                        <?= esc($item['status']) ?>
                                    </span>
                                </td>
                                <td class="border-bottom-0">
                                    <div class="table-actions">
                                        <a href="<?= site_url('[module]/show/' . $item['id']) ?>"
                                           class="btn btn-sm btn-info me-1"
                                           title="Detail">
                                            <i class="ti ti-eye"></i>
                                        </a>
                                        <a href="<?= site_url('[module]/edit/' . $item['id']) ?>"
                                           class="btn btn-sm btn-warning me-1"
                                           title="Edit">
                                            <i class="ti ti-edit"></i>
                                        </a>
                                        <button type="button" class="btn btn-sm btn-success me-1"
                                                onclick="toggleStatus(<?= $item['id'] ?>)"
                                                title="Ubah Status">
                                            <i class="ti ti-toggle-<?= $item['status'] === 'AKTIF' ? 'left' : 'right' ?>"></i>
                                        </button>
                                        <button type="button" class="btn btn-sm btn-danger"
                                                onclick="confirmDelete(<?= $item['id'] ?>)"
                                                title="Hapus">
                                            <i class="ti ti-trash"></i>
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        <?php endforeach; ?>
                    <?php else: ?>
                        <tr>
                            <td colspan="5" class="text-center py-5">
                                <i class="ti ti-[icon-empty] fs-1 text-muted mb-3"></i>
                                <h5 class="text-muted">[Empty Message]</h5>
                                <p class="text-muted">[Empty Description]</p>
                                <a href="<?= site_url('[module]/create') ?>" class="btn btn-primary">
                                    <i class="ti ti-circle-plus me-2"></i>[Action Button]
                                </a>
                            </td>
                        </tr>
                    <?php endif; ?>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
// Toggle status
function toggleStatus(id) {
    fetch(`<?= site_url('[module]/toggleStatus/') ?>${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'success') {
            location.reload();
        } else {
            alert(data.message || 'Gagal mengubah status');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Terjadi kesalahan saat mengubah status');
    });
}

// Confirm delete
function confirmDelete(id) {
    if (confirm('Apakah Anda yakin ingin menghapus data ini?')) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = `<?= site_url('[module]/delete/') ?>${id}`;

        const csrfToken = document.querySelector('meta[name="csrf-token"]');
        if (csrfToken) {
            const csrfInput = document.createElement('input');
            csrfInput.type = 'hidden';
            csrfInput.name = '<?= csrf_token() ?>';
            csrfInput.value = '<?= csrf_hash() ?>';
            form.appendChild(csrfInput);
        }

        document.body.appendChild(form);
        form.submit();
    }
}
</script>
```

**Controller Pattern:**
```php
// Controller - Simple & Clean (Client-Side DataTables)
public function index()
{
    $this->requireRole('ADMIN');

    // Get all data for client-side DataTables
    $data = $this->[module]Model->findAll();

    // Calculate statistics
    $stats = [
        'total' => $this->[module]Model->countAll(),
        'aktif' => $this->[module]Model->where('status', 'AKTIF')->countAll(),
        'nonaktif' => $this->[module]Model->where('status', 'NONAKTIF')->countAll(),
        'special' => $this->[module]Model->where('[field]', '[value]')->countAll()
    ];

    $data = [
        '[module]' => $data,
        'stats' => $stats
    ];

    return view('[module]/index', $data);
}
```

**Key Features:**
- 📊 **Statistics Cards**: Real-time data display with gradients
- 🔍 **Auto Search**: DataTables built-in search functionality
- 📄 **Auto Pagination**: Client-side pagination for small datasets
- 🎨 **Consistent Styling**: Same pattern across all modules
- 📱 **Responsive Design**: Mobile-friendly table layout
- 🔄 **Status Toggle**: Quick status change functionality
- 🗑️ **Safe Delete**: CSRF-protected delete confirmation
- ⚡ **Fast Loading**: No complex AJAX handling required

#### ⚠️ **Advanced: Server-Side DataTables Pattern**
**Use only when:**
- Dataset size: > 50,000 records
- Performance is critical
- Need custom filtering/sorting
- Have complex data processing requirements

**Complexity Level: HIGH**
- Requires custom AJAX handlers
- Complex controller logic
- Manual error handling
- More maintenance overhead

### 4. Module Implementation Template

Untuk setiap module baru, ikuti pattern client-side DataTables ini:

#### Required Files:
```
app/Views/[module]/
├── index.php      # List view dengan client-side DataTables
├── create.php     # Create form
├── edit.php       # Edit form
└── show.php       # Detail view
```

#### Quick Implementation Steps:
1. **Controller**: Simpel data retrieval tanpa AJAX complexity
2. **View**: Gunakan class="datatable" untuk auto-initialization
3. **JavaScript**: Minimal code, cukup DataTable initialization
4. **Styling**: Menggunakan pattern yang sama dengan siswa module

#### Example Implementation:
```php
// Controller - Simple & Clean
public function index()
{
    $this->requireRole('ADMIN');

    $data = [
        'users' => $this->userModel->findAll(),
        'stats' => $this->getStatistics()
    ];

    return view('users/index', $data);
}
```

```html
<!-- View - Client-side DataTables -->
<table id="usersTable" class="table text-nowrap mb-0 align-middle datatable">
    <thead>
        <tr>
            <th>ID</th>
            <th>Nama</th>
            <th>Status</th>
            <th>Aksi</th>
        </tr>
    </thead>
    <tbody>
        <?php foreach ($users as $user): ?>
        <tr>
            <td><?= $user['id'] ?></td>
            <td><?= esc($user['name']) ?></td>
            <td>
                <span class="badge bg-<?= $user['status'] === 'AKTIF' ? 'success' : 'secondary' ?>">
                    <?= $user['status'] ?>
                </span>
            </td>
            <td>
                <div class="table-actions">
                    <a href="<?= site_url('users/edit/'.$user['id']) ?>" class="btn btn-sm btn-warning">
                        <i class="ti ti-edit"></i>
                    </a>
                </div>
            </td>
        </tr>
        <?php endforeach; ?>
    </tbody>
</table>

<script>
$(document).ready(function() {
    $('#usersTable').DataTable({
        "language": { "search": "Cari:" }
    });
});
</script>
```

### 5. Pattern Summary

**✅ DO:**
- Gunakan client-side DataTables untuk module CRUD standard
- Tambahkan class="datatable" untuk auto-initialization
- Ikuti pattern yang sudah ada di siswa module
- Gunakan empty state yang konsisten
- Implement statistics cards di dashboard

**❌ DON'T:**
- Gunakan server-side DataTables untuk dataset kecil (< 10,000 records)
- Buat manual filter/search yang kompleks
- Implement bulk operations yang tidak diperlukan
- Lupakan CSRF protection di forms
- Gunakan pattern yang berbeda-beda untuk setiap module
```

#### E. Action Buttons Pattern (Row Level)
```html
<div class="table-actions">
    <!-- Primary Action -->
    <a href="[detail_url]" class="btn btn-sm btn-info me-1" title="[Title]">
        <i class="ti ti-[icon]"></i>
    </a>

    <!-- Edit Action -->
    <a href="[edit_url]" class="btn btn-sm btn-warning me-1" title="Edit">
        <i class="ti ti-edit"></i>
    </a>

    <!-- Status Toggle Action -->
    <button type="button" class="btn btn-sm btn-success me-1"
            onclick="toggleStatus([id])" title="Ubah Status">
        <i class="ti ti-toggle-[left/right]"></i>
    </button>

    <!-- Delete Action -->
    <button type="button" class="btn btn-sm btn-danger"
            onclick="confirmDelete([id])" title="Hapus">
        <i class="ti ti-trash"></i>
    </button>
</div>
```

#### F. Bulk Actions Pattern
```html
<!-- Bulk Actions Section -->
<div class="row mt-3" id="bulkActions" style="display: none;">
    <div class="col-md-6">
        <div class="btn-group" role="group">
            <button type="button" class="btn btn-success" onclick="bulkAction('activate')">
                <i class="ti ti-user-check me-2"></i>Aktifkan
            </button>
            <button type="button" class="btn btn-warning" onclick="bulkAction('deactivate')">
                <i class="ti ti-user-off me-2"></i>Nonaktifkan
            </button>
            <button type="button" class="btn btn-danger" onclick="bulkAction('delete')">
                <i class="ti ti-trash me-2"></i>Hapus
            </button>
        </div>
    </div>
    <div class="col-md-6 text-end">
        <small class="text-muted">
            <span id="selectedCount">0</span> data dipilih
        </small>
    </div>
</div>

<!-- Bulk Action JavaScript -->
function bulkAction(action) {
    const checkedBoxes = document.querySelectorAll('.row-checkbox:checked');
    const ids = Array.from(checkedBoxes).map(cb => cb.value);

    if (ids.length === 0) {
        alert('Pilih data terlebih dahulu');
        return;
    }

    const confirmMessages = {
        'activate': 'Aktifkan data yang dipilih?',
        'deactivate': 'Nonaktifkan data yang dipilih?',
        'delete': 'Hapus data yang dipilih?'
    };

    if (confirm(confirmMessages[action] || 'Lanjutkan?')) {
        // Submit bulk action via AJAX
        fetch('<?= site_url('[module]/bulkAction') ?>', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: JSON.stringify({
                action: action,
                ids: ids
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                reloadDataTable();
                showNotification(data.message, 'success');
            } else {
                showNotification(data.message, 'error');
            }
        })
        .catch(error => {
            showNotification('Terjadi kesalahan', 'error');
        });
    }
}

// AJAX Actions
function toggleStatus(id) {
    fetch(`<?= site_url('[module]/toggleStatus/') ?>${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'success') {
            reloadDataTable();
            showNotification(data.message, 'success');
        } else {
            showNotification(data.message, 'error');
        }
    });
}

function confirmDelete(id) {
    if (confirm('Apakah Anda yakin ingin menghapus data ini?')) {
        fetch(`<?= site_url('[module]/delete/') ?>${id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                reloadDataTable();
                showNotification(data.message, 'success');
            } else {
                showNotification(data.message, 'error');
            }
        });
    }
}

// Notification helper
function showNotification(message, type = 'info') {
    const alertClass = type === 'success' ? 'alert-success' :
                      type === 'error' ? 'alert-danger' : 'alert-info';

    const notification = document.createElement('div');
    notification.className = `alert ${alertClass} alert-dismissible fade show position-fixed`;
    notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    notification.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    document.body.appendChild(notification);

    setTimeout(() => {
        if (notification.parentNode) {
            notification.remove();
        }
    }, 5000);
}
```

### 3. Form Field Patterns

#### A. Standard Form Layout
```html
<div class="row">
    <!-- Two Column Layout -->
    <div class="col-md-6 mb-3">
        <label for="[field_id]" class="form-label">[Label] *</label>
        <input type="[type]" class="form-control" id="[field_id]" name="[field_name]"
               placeholder="[Placeholder]" required>
        <div class="invalid-feedback">
            [Error message]
        </div>
    </div>
    <div class="col-md-6 mb-3">
        <!-- Second field -->
    </div>
</div>

<!-- Single Column for Complex Fields -->
<div class="mb-3">
    <label for="[field_id]" class="form-label">[Label] *</label>
    <textarea class="form-control" id="[field_id]" name="[field_name]" rows="4" required></textarea>
    <div class="invalid-feedback">[Error message]</div>
</div>
```

#### B. Form Actions Pattern
```html
<div class="d-flex gap-2">
    <!-- Primary Action -->
    <button type="submit" class="btn btn-primary">
        <i class="ti ti-device-floppy me-2"></i>[Action Text]
    </button>

    <!-- Secondary Actions -->
    <a href="[secondary_url]" class="btn btn-warning">
        <i class="ti ti-key me-2"></i>[Action Text]
    </a>

    <!-- Cancel -->
    <a href="[cancel_url]" class="btn btn-danger">
        <i class="ti ti-circle-x me-2"></i>Batal
    </a>
</div>
```

### 4. Visual Patterns & Styling

#### A. Statistics Cards
```css
.stats-card {
    border-radius: var(--bs-border-radius);
    transition: transform 0.2s;
}
.stats-card:hover {
    transform: translateY(-2px);
}
.stats-icon {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.25rem;
}
.stats-card-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.stats-card-success { background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%); }
.stats-card-warning { background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%); }
.stats-card-info { background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); }
```

#### B. Badge Patterns
```html
<!-- Status Badges -->
<span class="badge bg-success rounded-3">Active</span>
<span class="badge bg-secondary rounded-3">Inactive</span>

<!-- Type Badges -->
<span class="badge bg-info rounded-3">Laki-laki</span>
<span class="badge bg-danger rounded-3">Perempuan</span>
```

#### C. Icon Patterns
📖 **Complete Icon Reference**: [docs/TABLER_ICONS.md](./docs/TABLER_ICONS.md)

- **Common Patterns**: See "Common Patterns in This Project" section in Tabler Icons docs
- **Quick Reference**: Navigation, Actions, Status, Authentication icons are documented

### 5. Interaction Patterns

#### A. Empty State Pattern
```html
<tr>
    <td colspan="[column_count]" class="text-center py-5">
        <i class="ti ti-inbox fs-1 text-muted mb-3"></i>
        <h5 class="text-muted">[Empty Message]</h5>
        <p class="text-muted">[Description]</p>
        <a href="[create_url]" class="btn btn-primary">
            <i class="ti ti-circle-plus me-2"></i>[Action Button]
        </a>
    </td>
</tr>
```

#### B. Confirmation Patterns
```javascript
// Delete Confirmation
function confirmDelete(id) {
    if (confirm('Apakah Anda yakin ingin menghapus data ini?')) {
        // Submit form
    }
}

// Form Validation
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
});
```

### 6. Responsive Behavior

#### A. Desktop (>768px)
- Statistics cards: 4 columns
- Table: Full width with all columns
- Form fields: 2 columns where appropriate

#### B. Tablet (768px - 992px)
- Statistics cards: 2 columns
- Table: Horizontal scroll
- Form fields: 1-2 columns

#### C. Mobile (<768px)
- Statistics cards: 1 column
- Table: Stacked or simplified view
- Form fields: 1 column
- Action buttons: Stacked vertically

### 7. Color & Theme Guidelines

#### A. Button Hierarchy
```css
/* Primary Actions */
.btn-primary { background: #4CAF50; }

/* Secondary Actions */
.btn-warning { background: #FF9800; }

/* Danger Actions */
.btn-danger { background: #F44336; }

/* Neutral Actions */
.btn-secondary { background: #6C757D; }
```

#### B. Status Colors
- **Success**: Active, Complete, Verified
- **Warning**: Pending, Review Required
- **Danger**: Inactive, Error, Blocked
- **Info**: Neutral, Informational
- **Secondary**: Disabled, Archived

### 8. Module Implementation Template

Untuk setiap module baru (Kaidah, Soal, dll), ikuti pattern ini:

#### Required Files:
```
app/Views/[module]/
├── index.php      # List view
├── create.php     # Create form
├── edit.php       # Edit form
└── [partials].php # Reusable components
```

#### Required Components:
1. **Page Header** dengan breadcrumb
2. **Statistics Cards** (opsional tapi direkomendasikan)
3. **Data Table** dengan DataTables
4. **Action Buttons** dengan icon + text
5. **Form Validation** dengan Bootstrap 5
6. **Flash Messages** untuk feedback
7. **Empty States** untuk data kosong

#### Variable Naming Conventions:
- `$stats` untuk statistics data
- `$[module]` untuk main data list
- `$[module]_item` untuk single item
- Field names: snake_case (e.g., `nama_lengkap`)

#### Screen Layouts

**1. Login Page**
- Split screen: Left (illustration/pattern), Right (form)
- Clean form dengan logo
- Remember me checkbox
- Primary button login

**2. Dashboard Admin/Guru**
- Sidebar navigation (collapsible)
- Top bar: Search, notifications, profile dropdown
- Cards untuk key metrics:
  - Total users (siswa/guru)
  - Total materi kaidah
  - Total soal
  - Aktivitas hari ini
- Chart: Progress pembelajaran, aktivitas mingguan
- Tabel recent activities

**3. User Management (Admin)**
- DataTable dengan features:
  - Search & filter
  - Sorting
  - Pagination
  - Bulk actions (activate, deactivate, delete)
- Button "Tambah User" di kanan atas
- Badges untuk role & status

**4. Materi Kaidah Management**
- List view / Card view toggle
- Drag & drop untuk reorder
- Quick actions: Edit, Delete, Duplicate
- Modal untuk add/edit (large form)
- Rich text editor untuk penjelasan

**5. Soal Management**
- Filter by kaidah
- Preview mode untuk melihat soal
- Form wizard untuk create soal:
  - Step 1: Pilih kaidah
  - Step 2: Tulis pertanyaan
  - Step 3: Tambah jawaban
  - Step 4: Review & submit
- Support Arabic keyboard input

**6. Laporan & Statistik**
- Filter: Date range, kaidah, siswa
- Export options: PDF, Excel, CSV
- Charts:
  - Bar chart: Skor per kaidah
  - Line chart: Progress over time
  - Pie chart: Distribusi tingkat kesulitan
- DataTable dengan detail data

#### Layout Structure
```
┌─────────────────────────────────────────────┐
│  Top Navigation Bar                         │
│  [Logo] [Search] [Notif] [Profile]         │
├─────────┬───────────────────────────────────┤
│         │                                   │
│ Sidebar │  Main Content Area               │
│         │                                   │
│ - Home  │  [Breadcrumb]                    │
│ - Users │                                   │
│ - Kaidah│  [Page Title]  [Actions]         │
│ - Soal  │                                   │
│ - Report│  [Content Cards/Tables]          │
│         │                                   │
│         │                                   │
└─────────┴───────────────────────────────────┘
```

### Icon Set
**Recommendations:**
- **Web**: Tabler Icons (primary), Feather Icons, Heroicons, Font Awesome (free)
- **Mobile**: Material Design Icons, Feather Icons

**Tabler Icons Setup:**
```html
<!-- Include Tabler Icons CSS -->
<link rel="stylesheet" href="<?= base_url('assets/css/icons/tabler-icons/tabler-icons.css') ?>" />

<!-- Usage -->
<i class="ti ti-circle-x me-2"></i>
<i class="ti ti-device-floppy me-2"></i>
<i class="ti ti-arrow-left me-2"></i>
```

## 🚀 Recent UI/UX Improvements (November 2025)

### Action Buttons Enhancement (All Modules)
**Problem:** Tombol aksi hanya menggunakan ikon tanpa teks, kurang jelas untuk user.

**Solution Applied:**
- ✅ **Kaidah Module**: Added text labels (Detail, Edit, Hapus)
- ✅ **Soal Module**: Added text labels (Preview, Edit, Test, Hapus)
- ✅ **Icon + Text Format**: Menggunakan `me-1` class untuk spacing

**Code Pattern:**
```html
<!-- Universal Action Button Pattern -->
<a href="[url]" class="btn btn-sm btn-info me-1">
    <i class="ti ti-eye me-1"></i>Detail
</a>
<a href="[url]" class="btn btn-sm btn-warning me-1">
    <i class="ti ti-edit me-1"></i>Edit
</a>
<button class="btn btn-sm btn-danger">
    <i class="ti ti-trash me-1"></i>Hapus
</button>
```

### Solid Color Implementation (No Gradients)
**Problem:** Beberapa halaman masih menggunakan gradient yang tidak konsisten dengan design system.

**Files Updated:**
- ✅ **soal/test-lcm.php**: Removed all gradient styling
  - LCM Header: `linear-gradient` → `#4CAF50` → `white`
  - Chi-square results: `linear-gradient` → solid colors
  - Distribution bars: `linear-gradient` → `#4CAF50`
  - Alert info: `linear-gradient` → `#d1ecf1`
  - Button test: `linear-gradient` → `#007bff`

**Benefits:**
- **Consistency**: Semua elemen menggunakan solid colors
- **Performance**: Faster rendering tanpa gradient calculations
- **Professional Look**: Clean dan modern appearance
- **Accessibility**: Better contrast ratios

### LCM Algorithm Header White Background
**Problem:** LCM header menggunakan background hijau yang kurang konsisten dengan theme aplikasi.

**Changes Made:**
```css
/* Before */
.lcm-header {
    background: #4CAF50;
    color: white;
}

/* After */
.lcm-header {
    background: white;
    color: #212121;
    border: 1px solid #e9ecef;
}
```

### Topbar Notification Removal
**Problem:** Notifikasi bell di topbar tidak fungsional dan mengganggu clean interface.

**Action Taken:**
- ❌ **Removed**: Bell icon dengan notification badge dari `app/Views/layouts/navbar.php`
- ✅ **Result**: Cleaner topbar dengan hanya menu toggle dan user profile

**Code Removed:**
```html
<!-- Removed from navbar.php -->
<li class="nav-item">
    <a class="nav-link nav-icon-hover" href="javascript:void(0)">
        <i class="ti ti-bell-ringing"></i>
        <div class="notification bg-primary rounded-circle"></div>
    </a>
</li>
```

### UI Documentation Updates
**Added to CLAUDE.md:**
- ✅ **UI Display Guidelines**: Section tentang tidak menampilkan ID di UI
- ✅ **UI Styling Guidelines**: Section tentang penggunaan solid colors
- ✅ **Design Principle #7**: "Solid Colors"
- ✅ **Design Principle #6**: "Clean Interface"

**Documentation Coverage:**
```markdown
## UI Display Guidelines
❌ Database IDs, Technical Fields, Internal Codes
✅ User-Friendly Identifiers, Meaningful Information

## UI Styling Guidelines
❌ Gradients, Complex Patterns, Inconsistent Colors
✅ Solid Colors, Consistent Palette, Semantic Colors
```

## 📚 Documentation Resources

### Complete Documentation
- **Project Overview**: [CLAUDE.md](./CLAUDE.md) ← Current file
- **UI/UX Pattern Framework**: See "🎯 Tenant UI/UX Pattern Framework" section below
- **Tabler Icons Documentation**: [docs/TABLER_ICONS.md](./docs/TABLER_ICONS.md) ← Comprehensive guide

### Tabler Icons Quick Reference
📖 **Complete Guide**: [docs/TABLER_ICONS.md](./docs/TABLER_ICONS.md)

**Quick Setup:**
```html
<link rel="stylesheet" href="assets/css/icons/tabler-icons/tabler-icons.css" />
<i class="ti ti-circle-x me-2"></i>Batal
<i class="ti ti-device-floppy me-2"></i>Simpan
```

**Popular Icons:**
- Navigation: `ti ti-arrow-left`, `ti ti-home`, `ti ti-menu`
- Actions: `ti ti-edit`, `ti ti-trash`, `ti ti-plus`, `ti ti-circle-plus`
- Status: `ti ti-circle-check`, `ti ti-x-circle`, `ti ti-alert-triangle`
- Authentication: `ti ti-key`, `ti ti-lock`, `ti ti-user`, `ti ti-users`

**Total Available**: 4964+ icons across 40+ categories

📖 **Complete Icon Reference**: [docs/TABLER_ICONS.md](./docs/TABLER_ICONS.md)

**Usage Example:**
```html
<!-- In HTML/PHP views -->
<i class="ti ti-users text-primary"></i>
<i class="ti ti-circle-check text-success"></i>
<i class="ti ti-edit text-warning"></i>
<i class="ti ti-trash text-danger"></i>
```

---

## Arsitektur Aplikasi

**FOKUS UTAMA**: Algoritma Linear Congruent Method (LCM) untuk pengacakan soal

**Authentication**: Simple Session-based (Web) & Simple API Authentication (Mobile)

### A. Web Application (CodeIgniter 4)

**Role Users:**

#### 1. **Admin (Super User)** - Full Access
   - ✅ Login/Logout (Session sederhana)
   - ✅ Kelola Pengguna Guru (CRUD)
   - ✅ Kelola Pengguna Siswa (CRUD)
   - ✅ **Manajemen Siswa Master Data** ⭐⭐⭐
     - CRUD data siswa (NIS, nama, jenis kelamin, kelas, status)
     - Generate random password otomatis untuk siswa baru
     - Reset password siswa
     - Track login history siswa (mobile app)
     - Statistics dashboard (total, aktif, nonaktif, per kelas)
     - Search & filtering berdasarkan nama, NIS, kelas
     - Pagination untuk data yang besar
     - Export data (PDF/Excel)
   - ✅ Kelola Materi Kaidah (CRUD)
   - ✅ Kelola Soal & Jawaban (CRUD)
   - ✅ Lihat Hasil Belajar Siswa
   - ✅ Lihat Laporan

#### 2. **Guru (Teacher)** - Limited Access
   - ✅ Login/Logout (Session sederhana)
   - ✅ Kelola Materi Kaidah (CRUD)
   - ✅ Kelola Soal & Jawaban (CRUD)
   - ✅ Lihat Hasil Belajar Siswa
   - ✅ Lihat Laporan
   - ❌ Tidak bisa kelola pengguna

**Catatan**: Siswa **TIDAK** bisa login ke web. Web hanya untuk Admin dan Guru.

### B. Mobile Application (Java/Android)

**Role User:**

#### 1. **Siswa (Student)** - Mobile Only
   - ✅ Register & Login (Simple username/password, simpan di SharedPreferences)
   - ✅ Lihat daftar materi kaidah
   - ✅ Baca materi kaidah (penjelasan + contoh)
   - ✅ **Mengerjakan latihan soal (FOKUS: Pengacakan dengan LCM)** ⭐⭐⭐
   - ✅ Lihat hasil skor
   - ✅ Lihat riwayat pembelajaran
   - ✅ Kelola profil
   - ❌ Tidak bisa login ke web

## 🎉 Features Implemented

### ✅ **Completed Features (November 2025)**

#### **Mobile Application (Java/Android)**
1. **Login Screen UI/UX Improvements** ⭐⭐⭐
   - ✅ **Removed Register Link** - Clean login interface without "Belum punya akun? Daftar" link
   - ✅ **Primary Green Login Button** - Updated button color to match app theme (`@color/primary_green`)
   - ✅ **Fixed Password Toggle Icon Conflicts** - Resolved conflicts between password visibility toggle and validation error icons
   - ✅ **Working Password Visibility Toggle** - Implemented Material Design `endIconMode="password_toggle"` with visible gray icons (`#BDBDBD`)
   - ✅ **Enhanced Error Handling** - Modified validation to prevent icon conflicts by clearing password field errors
   - ✅ **Clean Professional Design** - Consistent with app's green theme and Material Design principles

10. **Room Database Query Optimization** ⭐⭐⭐
   - ✅ **Fixed CURSOR_MISMATCH Warnings** - Eliminated all Room Database warnings about unused columns
   - ✅ **Query Performance Optimization** - Removed unused columns from SELECT statements to improve performance
   - ✅ **Clean Database Queries** - Optimized MateriKaidahDao, SoalDao, JawabanDao, SesiLatihanDao, and DetailJawabanSiswaDao
   - ✅ **User Feedback Implementation** - Following user's request to "hapus saja yang unused" instead of suppressing warnings
   - ✅ **Syntax Error Fixes** - Fixed corrupted method signatures and compilation errors
   - **Key Optimizations:**
     - `MateriKaidahDao.getRekomendasiMateri()`: Removed unused JOIN with riwayat_belajar
     - `SoalDao.getSoalSulitIds()`: Changed to return only IDs instead of full objects
     - `JawabanDao.getJawabanJarangDipilihIds()`: Optimized to return only IDs
     - `SesiLatihanDao.getSesiWithMateriInfo()`: Selected only specific needed columns
     - `DetailJawabanSiswaDao`: Fixed method signature and optimized queries

#### **Web Application (CodeIgniter 4)**
1. **Authentication System**
   - ✅ Login/Logout with session management
   - ✅ Role-based access control (Admin/Guru)
   - ✅ Secure password hashing (bcrypt)
   - ✅ Remember me functionality

2. **Manajemen Siswa Master Data** ⭐⭐⭐
   - ✅ CRUD operations (Create, Read, Update, Delete)
   - ✅ Generate random password otomatis
   - ✅ Reset password functionality
   - ✅ **Login History Tracking** (mobile app)
     - Device info & IP tracking
     - 50 most recent logins
     - Timestamp recording
   - ✅ **Statistics Dashboard**
     - Total siswa count
     - Aktif/Nonaktif statistics
     - Per kelas statistics
     - Beautiful gradient cards with Tabler Icons
   - ✅ **Search & Filtering**
     - Search by nama/NIS
     - Filter by kelas
     - Real-time filtering
   - ✅ **Pagination System**
     - 10 items per page
     - Page navigation
     - Total count display

3. **UI/UX Improvements**
   - ✅ **Tabler Icons Integration** (primary icon set)
   - ✅ **Solid Button Design** (icon + text)
   - ✅ **Responsive Tables** with hover effects
   - ✅ **Green Theme** consistency throughout
   - ✅ **Flash Messages** for user feedback
   - ✅ **Modal Confirmations** for destructive actions
   - ✅ **Enhanced Toast Notifications** - Notyf.js integration with Tabler Icons
   - ✅ **Better Error Icons** - Updated from `ti ti-x-circle` to `ti ti-circle-x`
   - ✅ **Consistent Success Messages** - User-friendly feedback for all actions

4. **Manajemen Pengguna (Admin)** ⭐⭐⭐
   - ✅ CRUD operations (Create, Read, Update, Delete)
   - ✅ **Fixed routing pattern** - Using standard CI4 pattern with `_method` override
   - ✅ **Clean forms** - Removed unused email field
   - ✅ **Consistent UI** - Following same pattern as other modules
   - ✅ **Role management** - Admin/Guru role assignment
   - ✅ **Status management** - Active/Nonactive toggle
   - ✅ **Password management** - Generate and reset functionality
   - ✅ **Statistics Dashboard** - User count and role distribution
   - ✅ **Advanced Error Handling** - Comprehensive exception handling and logging
   - ✅ **Indonesian Validation Messages** - User-friendly validation in Indonesian
   - ✅ **Smart Update Logic** - Detects data changes and handles no-change scenarios
   - ✅ **Enhanced Toast Notifications** - Using Notyf.js with Tabler Icons
   - ✅ **Better UX** - Consistent success messages for all scenarios

5. **Manajemen Guru (Teacher Management)** ⭐⭐⭐
   - ✅ **Separate Teacher Module** - Independent from user management system
   - ✅ **Role-Based Access** - Admin-only access to teacher management
   - ✅ **CRUD Operations** - Complete Create, Read, Update, Delete for teachers
   - ✅ **Statistics Dashboard** - Total/Active/Inactive teacher counts with gradient cards
   - ✅ **DataTables Integration** - Client-side DataTables with search and pagination
   - ✅ **Custom GuruModel** - Filtered queries by role (hak_akses = 'GURU')
   - ✅ **Tabler Icons Integration** - Consistent icon usage (ti ti-school, ti ti-edit, etc.)
   - ✅ **Status Toggle** - Quick active/inactive status changes with confirmation
   - ✅ **Bulk Operations** - Multi-select for activate/deactivate/delete actions
   - ✅ **Profile Management** - Detailed teacher profiles with tabbed interface
   - ✅ **Enhanced Validation** - Indonesian validation messages for teacher-specific fields
   - ✅ **Route Management** - Proper routing pattern with RESTful endpoints
   - ✅ **UI/UX Consistency** - Following same patterns as other management modules

6. **Data Validation & Security**
   - ✅ Form validation (server & client side)
   - ✅ Unique NIS validation
   - ✅ Password complexity requirements
   - ✅ XSS protection with `esc()` helper
   - ✅ CSRF protection

6. **Database Design**
   - ✅ **siswa** table with proper relationships
   - ✅ **siswa_login_history** for tracking
   - ✅ **pengguna** table (Admin/Guru management)
   - ✅ Indonesian field naming conventions
   - ✅ UPPERCASE status values (AKTIF/NONAKTIF)

#### **Mobile API (REST)**
1. **Siswa Authentication API**
   - ✅ `POST /api/siswa/auth/login` - Login siswa
   - ✅ `GET /api/siswa/auth/profile` - Get profile
   - ✅ Simple token-based authentication
   - ✅ CORS support for cross-origin requests

#### **Mobile Application (Java/Android)**
1. **API Integration & Data Sync** ⭐⭐⭐
   - ✅ **Custom Response Wrapper Implementation** - Created `KaidahListResponse` class to handle nested API response structure
   - ✅ **API Response Parsing Fix** - Resolved "Expected BEGIN_ARRAY but was BEGIN_OBJECT" parsing error
   - ✅ **Background Data Synchronization** - Automatic API sync when local database is empty
   - ✅ **Progressive Data Loading** - Fallback to local database when API unavailable
   - ✅ **Network Error Handling** - Comprehensive error handling with user-friendly messages

2. **Database & Performance** ⭐⭐⭐
   - ✅ **Room Database Implementation** - Complete offline support with SQLite
   - ✅ **Database Schema Migration** - Proper versioning with fallback to destructive migration
   - ✅ **Foreign Key Optimization** - Added indexes for better query performance
   - ✅ **Background Thread Execution** - All database operations on background threads
   - ✅ **Memory Management** - Proper resource cleanup and memory leak prevention

3. **UI/UX Improvements** ⭐⭐⭐
   - ✅ **Welcome Card Background Fix** - Changed from dark mode to white background for better readability
   - ✅ **Statistics Cards Update** - All cards (Welcome, Kaidah, Quiz) now use white background
   - ✅ **Recent Kaidah List** - Item cards updated with white background for consistency
   - ✅ **Material Design 3 Compliance** - Modern UI components and styling
   - ✅ **Responsive Layout** - Proper handling of different screen sizes

4. **Architecture & Code Quality** ⭐⭐⭐
   - ✅ **MVVM Architecture Pattern** - Proper separation of concerns with ViewModel and LiveData
   - ✅ **Repository Pattern Implementation** - Clean data access layer
   - ✅ **Dependency Injection Ready** - Structure supports future DI implementation
   - ✅ **Clean Code Principles** - Proper method naming and code organization
   - ✅ **Exception Handling** - Comprehensive try-catch blocks with logging

5. **Authentication & Session Management** ⭐⭐⭐
   - ✅ **SessionManager Implementation** - SharedPreferences-based session storage
   - ✅ **Secure Login Flow** - Proper credential validation and token storage
   - ✅ **Auto-Login Feature** - Session persistence across app restarts
   - ✅ **Logout Functionality** - Complete session cleanup on logout
   - ✅ **User Profile Management** - Dynamic user data display and updates

6. **Learning Progress & Auto-Completion** ⭐⭐⭐
   - ✅ **Auto-Completion Logic** - Automatic materi completion when moving to next materi
   - ✅ **Progress Persistence** - Local database storage for completion status
   - ✅ **Background Processing** - Non-blocking progress updates with proper threading
   - ✅ **Progress Bar Updates** - Real-time progress indication in kaidah list view
   - ✅ **Toast Notifications** - User feedback when materi completed successfully

7. **Simplified Kaidah List Interface** ⭐⭐⭐
   - ✅ **Removed Group/Flat View Toggle** - Simplified interface without expand/collapse complexity
   - ✅ **Always-Visible Materi Lists** - Direct access to all materi items without hiding
   - ✅ **Clean Progress Indicators** - Progress bars below bab descriptions without clutter
   - ✅ **Streamlined Navigation** - Direct materi access with improved user experience
   - ✅ **Consistent Card Design** - Material Design cards with proper elevation and spacing

8. **API Fallback with Offline Support** ⭐⭐⭐
   - ✅ **Smart Data Loading** - Check local database first, fallback to API if needed
   - ✅ **Automatic Caching** - API responses automatically saved to Room database
   - ✅ **Offline Access** - Kaidah data available for offline viewing after first load
   - ✅ **Background Sync** - Database operations on separate threads to avoid UI blocking
   - ✅ **Error Recovery** - Graceful handling of network failures with local data fallback

9. **UI Refinements & UX Improvements** ⭐⭐⭐
   - ✅ **Removed Progress Card from Detail View** - Cleaner interface without redundant progress indicators
   - ✅ **Preserved Auto-Completion Logic** - Progress tracking continues working in background
   - ✅ **Optimized Toast Notifications** - Reduced unnecessary toasts for cleaner UX
   - ✅ **Consistent Navigation Flow** - Smooth transitions between materi with proper state management
   - ✅ **Enhanced Error Handling** - User-friendly error messages and recovery options

10. **Navigation Enhancement - Bab Completion Flow** ⭐⭐⭐
   - ✅ **Fixed Bab Congrats Navigation** - Resolved timing issue when navigating from congratulations to next bab
   - ✅ **Added Delay for Tab Switching** - 300ms delay ensures proper tab switching before detail navigation
   - ✅ **Improved Back Stack Management** - Clear back stack to prevent unwanted navigation back
   - ✅ **Enhanced Logging** - Added detailed logging for debugging navigation flow
   - ✅ **Direct Materi Navigation** - Now correctly navigates to first materi of next bab instead of just bab list

#### **Technical Improvements (November 2025)**
7. **Room Database Query Optimization** ⭐⭐⭐
   - ✅ **CURSOR_MISMATCH Warning Elimination** - Fixed all Room Database warnings by removing unused columns
   - ✅ **Query Performance Enhancement** - Optimized SELECT statements to fetch only required columns
   - ✅ **Clean Database Architecture** - Improved query efficiency across all DAO classes
   - ✅ **User-Centric Approach** - Implemented user's preference for removing unused columns instead of suppressing warnings
   - ✅ **Compilation Error Resolution** - Fixed method signatures and syntax errors in DetailJawabanSiswaDao
   - **Performance Benefits:**
     - Reduced memory usage by selecting only needed columns
     - Faster query execution with optimized SELECT statements
     - Eliminated unnecessary JOIN operations
     - Improved database cursor efficiency

8. **Error Handling & Validation System** ⭐⭐⭐
   - ✅ **Advanced Exception Handling** - Try-catch blocks with detailed logging
   - ✅ **Indonesian Validation Messages** - Complete localization for better UX
   - ✅ **Smart Update Detection** - Compares old vs new data to prevent unnecessary updates
   - ✅ **Comprehensive Logging** - Error tracking with context data for debugging
   - ✅ **User-Friendly Error Messages** - Clear, actionable error descriptions
   - ✅ **No-Change Scenario Handling** - Graceful handling when no data changes

8. **Toast Notification System**
   - ✅ **Notyf.js Integration** - Modern toast notifications with animations
   - ✅ **Tabler Icons Support** - Consistent icon usage throughout notifications
   - ✅ **Multiple Notification Types** - Success, error, warning, info with proper styling
   - ✅ **Auto-Dismiss Configuration** - 5-second auto-dismiss with manual dismiss option
   - ✅ **Enhanced Error Icons** - Updated to more visible `ti ti-circle-x`
   - ✅ **Flash Message Integration** - Automatic conversion of server flash messages

9. **Code Quality Improvements**
   - ✅ **PHP 8.3 Compatibility** - Full compatibility with PHP 8.3.26 and modern practices
   - ✅ **PHP 8.2+ Support** - Fixed deprecated dynamic property warnings
   - ✅ **Property Declarations** - Proper typed properties in BaseController
   - ✅ **Clean Architecture** - Separation of concerns and proper MVC patterns
   - ✅ **DRY Principles** - Reusable components and validation patterns

### 📊 **Current Statistics**

#### **Web Application (CodeIgniter 4)**
- **6 Tables** created and seeded (siswa, pengguna, kaidah_materi, soal, siswa_login_history)
- **4 Controllers** (SiswaController, PenggunaController, GuruController, AuthController) with complete CRUD
- **4 Models** (SiswaModel, PenggunaModel, GuruModel) with complete CRUD
- **18+ View files** (index, create, edit, show, login_history, guru views, pengguna views, and partials)
- **All icons** updated to Tabler Icons (4964+ icons available)
- **All buttons** using solid design with icon + text
- **Fixed routing patterns** following CI4 standards
- **Clean forms** with removed unused fields
- **Complete Indonesian validation system** with user-friendly messages
- **Advanced error handling** with logging and exception management
- **Enhanced Toast Notification System** with custom confirm dialogs and loading states
- **Custom Confirm Dialog Implementation** using Bootstrap modals with Tabler Icons
- **PHP 8.3 compatibility** with modern PHP practices and strict typing

#### **Mobile Application (Java/Android)**
- **7 Database Entities** (Siswa, MateriKaidah, Soal, Jawaban, SesiLatihan, DetailJawabanSiswa, RiwayatBelajar)
- **8 DAO Interfaces** with comprehensive query methods and indexing
- **6 Custom Response Models** for API integration (ApiResponse, LoginResponse, KaidahListResponse, etc.)
- **5 Fragment Classes** (Login, Home, KaidahList, KaidahDetail, Quiz, Progress, Profile)
- **3 Adapter Classes** (KaidahAdapter, KaidahSmallAdapter, JawabanAdapter)
- **Complete Offline Support** with Room database and API synchronization
- **MVVM Architecture** with LiveData and Repository pattern
- **Material Design 3** implementation with custom styling
- **Responsive UI Components** with proper accessibility support
- **API Integration Layer** with Retrofit and custom response parsing
- **Enhanced Login Screen** with password visibility toggle and primary green theme
- **Clean UI/UX** without register link and improved error handling

### ✅ **Latest UI/UX Enhancements (November 2025)**

#### **Mobile Application (Java/Android)**
- **Login Screen Improvements** - Clean interface without register link, primary green button, working password toggle
- **Password Visibility Toggle** - Material Design implementation with visible gray icons and no validation conflicts
- **Enhanced Error Handling** - Improved validation feedback with icon conflict prevention
- **Consistent Theme Application** - Primary green color (#4CAF50) applied to login button and interface elements
- **Material Design Compliance** - Proper TextInputLayout configuration with endIconMode and icon tinting

#### **Web Application**
- **Avatar Enhancement**: 50% larger avatar icons (180px × 180px) with perfect circular shape
- **Statistics Icons Upscaling**: 5rem icon size for better visibility in guru show page
- **Table Borders Implementation**: `table-bordered` class applied to all index pages
- **UI Cleanup**: Removed unnecessary "Tindakan Cepat" and "Navigasi" sections from guru show
- **Date Helper Functions**: DRY implementation for `format_date_time()`, `time_ago()`, and `calculate_days_since()`
- **Consistent Action Buttons**: Left-aligned action buttons across all management modules
- **Professional Appearance**: Modern, clean interface with enhanced visual hierarchy

#### **Mobile Application (Android)**
- **Welcome Card Background Fix**: Changed from dark mode to white background for better readability
- **Statistics Cards Update**: All cards (Welcome, Kaidah, Quiz) now use white background for consistency
- **Recent Kaidah List**: Item cards updated with white background for better visual hierarchy
- **Material Design 3 Compliance**: Modern UI components with proper elevation and shadows
- **Responsive Layout**: Proper handling of different screen sizes and orientations
- **Enhanced Typography**: Improved text readability with proper color contrast
- **Accessibility Improvements**: Better touch targets and content descriptions

### 🚧 **Next Development**
1. Materi Kaidah Management
2. Soal Management with LCM Algorithm
3. Mobile App Development
4. Reporting & Analytics

---

## Best Practices & Coding Standards

### CodeIgniter 4 Best Practices

#### 1. MVC Architecture Pattern
```php
// ❌ BAD - Business logic di Controller
public function saveKaidah() {
    $data = $this->request->getPost();
    $this->db->table('kaidah_materi')->insert($data);
}

// ✅ GOOD - Business logic di Model/Service
public function saveKaidah() {
    $data = $this->request->getPost();
    return $this->kaidahService->create($data);
}
```

#### 2. Service Layer Pattern
```
app/Services/
├── KaidahService.php
├── SoalService.php
├── LCMService.php
└── PembelajaranService.php
```

**Example Service:**
```php
<?php
namespace App\Services;

use App\Models\KaidahModel;
use App\Libraries\LCMAlgorithm;

class KaidahService {
    protected $kaidahModel;
    protected $lcm;

    public function __construct() {
        $this->kaidahModel = new KaidahModel();
        $this->lcm = new LCMAlgorithm();
    }

    public function getAllKaidah(array $filters = []) {
        // Business logic here
        return $this->kaidahModel->getFiltered($filters);
    }

    public function createKaidah(array $data) {
        // Validation, processing, etc
        if (!$this->validate($data)) {
            throw new \InvalidArgumentException('Invalid data');
        }
        return $this->kaidahModel->insert($data);
    }
}
```

#### 3. Repository Pattern (Optional - untuk project besar)
```php
<?php
namespace App\Repositories;

interface KaidahRepositoryInterface {
    public function find($id);
    public function all();
    public function create(array $data);
    public function update($id, array $data);
    public function delete($id);
}

class KaidahRepository implements KaidahRepositoryInterface {
    protected $model;

    public function __construct(KaidahModel $model) {
        $this->model = $model;
    }

    public function find($id) {
        return $this->model->find($id);
    }

    // ... implement other methods
}
```

#### 4. Validation Rules
```php
// app/Config/Validation.php - Custom Rules
protected $kaidah_rules = [
    'judul_kaidah' => [
        'rules'  => 'required|min_length[3]|max_length[255]|is_unique[kaidah_materi.judul_kaidah]',
        'errors' => [
            'required'   => 'Judul kaidah harus diisi',
            'min_length' => 'Judul minimal 3 karakter',
            'is_unique'  => 'Judul kaidah sudah digunakan'
        ]
    ],
    'tingkat_kesulitan' => [
        'rules'  => 'required|in_list[mudah,sedang,sulit]',
        'errors' => [
            'required' => 'Tingkat kesulitan harus dipilih',
            'in_list'  => 'Tingkat kesulitan tidak valid'
        ]
    ]
];

// Usage in Controller
public function store() {
    if (!$this->validate('kaidah_rules')) {
        return redirect()->back()->withInput()->with('errors', $this->validator->getErrors());
    }
    // Process data
}
```

#### 5. Error Handling & Logging
```php
<?php
namespace App\Controllers;

use CodeIgniter\Exceptions\PageNotFoundException;
use App\Exceptions\KaidahNotFoundException;

class KaidahController extends BaseController {

    public function show($id) {
        try {
            $kaidah = $this->kaidahService->findById($id);

            if (!$kaidah) {
                throw new KaidahNotFoundException("Kaidah dengan ID {$id} tidak ditemukan");
            }

            return view('kaidah/show', ['kaidah' => $kaidah]);

        } catch (KaidahNotFoundException $e) {
            log_message('warning', $e->getMessage());
            throw PageNotFoundException::forPageNotFound($e->getMessage());

        } catch (\Exception $e) {
            log_message('error', 'Error saat mengambil kaidah: ' . $e->getMessage());
            return redirect()->back()->with('error', 'Terjadi kesalahan sistem');
        }
    }
}
```

#### 6. API Response Format
```php
<?php
namespace App\Controllers\API;

class BaseAPIController extends Controller {

    protected function respondWithSuccess($data = null, $message = 'Success', $code = 200) {
        return $this->response->setJSON([
            'status'  => 'success',
            'message' => $message,
            'data'    => $data
        ])->setStatusCode($code);
    }

    protected function respondWithError($message = 'Error', $code = 400, $errors = null) {
        $response = [
            'status'  => 'error',
            'message' => $message
        ];

        if ($errors) {
            $response['errors'] = $errors;
        }

        return $this->response->setJSON($response)->setStatusCode($code);
    }
}

// Usage
public function login() {
    $credentials = $this->request->getJSON(true);

    if (!$token = $this->authService->attempt($credentials)) {
        return $this->respondWithError('Invalid credentials', 401);
    }

    return $this->respondWithSuccess(['token' => $token], 'Login successful');
}
```

#### 7. Database Query Optimization
```php
// ❌ BAD - N+1 Query Problem
$kaidahs = $this->kaidahModel->findAll();
foreach ($kaidahs as $kaidah) {
    $kaidah->soal_count = $this->soalModel->where('kaidah_id', $kaidah->id)->countAllResults();
}

// ✅ GOOD - Use JOIN or eager loading
$kaidahs = $this->kaidahModel
    ->select('kaidah_materi.*, COUNT(soal.id) as soal_count')
    ->join('soal', 'soal.kaidah_id = kaidah_materi.id', 'left')
    ->groupBy('kaidah_materi.id')
    ->findAll();
```

#### 8. Security Best Practices
```php
// CSRF Protection - enabled by default
// app/Config/Filters.php
public $globals = [
    'before' => ['csrf'],
];

// XSS Protection - use esc() helper
echo esc($user_input); // Escapes HTML
echo esc($user_input, 'js'); // For JavaScript context
echo esc($user_input, 'url'); // For URLs

// SQL Injection Protection - use Query Builder
$this->db->table('users')->where('id', $id)->get(); // Safe
// NEVER: $this->db->query("SELECT * FROM users WHERE id = $id"); // Unsafe!

// Password Hashing
$hashedPassword = password_hash($password, PASSWORD_DEFAULT);
password_verify($inputPassword, $hashedPassword);
```

### Android Java Best Practices

#### Build Instructions & Guidelines

**🚨 IMPORTANT: Build Process**
- **❌ DO NOT use `./gradlew assembleDebug`** - Can cause conflicts and resource issues
- **✅ USE Android Studio** for building APKs - Recommended approach
- **✅ USE `./gradlew build`** only for compilation checking
- **✅ Build manually through Android Studio** → Build → Build Bundle(s) / APK(s) → Build APK(s)

**Build Steps:**
1. Open project in Android Studio
2. Sync Gradle files (if prompted)
3. Build → Clean Project
4. Build → Rebuild Project (for compilation check)
5. Build → Build Bundle(s) / APK(s) → Build APK(s) (for final build)

**Why Manual Build?**
- Prevents resource conflicts
- Better dependency management
- Proper ProGuard/R8 optimization
- Controlled signing process
- Stable build environment

#### 1. MVVM Architecture Implementation
```
app/src/main/java/
├── data/
│   ├── model/           # Data models
│   ├── repository/      # Repository classes
│   ├── local/          # Room Database
│   └── remote/         # API services
├── ui/
│   ├── login/
│   │   ├── LoginActivity.java
│   │   └── LoginViewModel.java
│   ├── home/
│   │   ├── HomeFragment.java
│   │   └── HomeViewModel.java
│   └── ...
├── utils/              # Utility classes
└── di/                 # Dependency Injection (optional)
```

**Example ViewModel:**
```java
public class KaidahViewModel extends ViewModel {
    private MutableLiveData<List<Kaidah>> kaidahList;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessage;
    private KaidahRepository repository;

    public KaidahViewModel() {
        kaidahList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        repository = new KaidahRepository();
    }

    public LiveData<List<Kaidah>> getKaidahList() {
        return kaidahList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchKaidahList() {
        isLoading.setValue(true);
        repository.getAllKaidah(new RepositoryCallback<List<Kaidah>>() {
            @Override
            public void onSuccess(List<Kaidah> data) {
                kaidahList.setValue(data);
                isLoading.setValue(false);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
                isLoading.setValue(false);
            }
        });
    }
}
```

**Example Activity:**
```java
public class KaidahListActivity extends AppCompatActivity {
    private KaidahViewModel viewModel;
    private RecyclerView recyclerView;
    private KaidahAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaidah_list);

        initViews();
        setupViewModel();
        observeData();

        viewModel.fetchKaidahList();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(KaidahViewModel.class);
    }

    private void observeData() {
        viewModel.getKaidahList().observe(this, kaidahList -> {
            adapter.setData(kaidahList);
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

#### 2. Repository Pattern
```java
public class KaidahRepository {
    private KaidahApiService apiService;
    private KaidahDao localDao;

    public KaidahRepository() {
        apiService = RetrofitClient.getInstance().create(KaidahApiService.class);
        localDao = AppDatabase.getInstance().kaidahDao();
    }

    public void getAllKaidah(RepositoryCallback<List<Kaidah>> callback) {
        // Try local first
        List<Kaidah> localData = localDao.getAll();
        if (!localData.isEmpty()) {
            callback.onSuccess(localData);
        }

        // Fetch from network
        apiService.getKaidahList().enqueue(new Callback<ApiResponse<List<Kaidah>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Kaidah>>> call,
                                   Response<ApiResponse<List<Kaidah>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Kaidah> data = response.body().getData();
                    // Save to local
                    localDao.insertAll(data);
                    callback.onSuccess(data);
                } else {
                    callback.onError("Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Kaidah>>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
```

#### 3. Retrofit Setup
```java
public class RetrofitClient {
    private static final String BASE_URL = "https://api.yourapp.com/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }
}

// Simple Auth - credentials disimpan di SharedPreferences
// Setiap API request sertakan username/user_id dari SharedPreferences
public class ApiHelper {
    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return prefs.getString("user_id", null);
    }

    public static boolean isLoggedIn(Context context) {
        return getUserId(context) != null;
    }
}
```

#### 4. Room Database
```java
@Database(entities = {Kaidah.class, Soal.class, Progress.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract KaidahDao kaidahDao();
    public abstract SoalDao soalDao();
    public abstract ProgressDao progressDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "pembelajaran_kaidah_db"
            )
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }
}

@Dao
public interface KaidahDao {
    @Query("SELECT * FROM kaidah_materi ORDER BY urutan ASC")
    List<Kaidah> getAll();

    @Query("SELECT * FROM kaidah_materi WHERE id = :id")
    Kaidah getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Kaidah> kaidahList);

    @Delete
    void delete(Kaidah kaidah);
}
```

#### 5. Error Handling
```java
public class ErrorHandler {

    public static String handleApiError(Throwable error) {
        if (error instanceof HttpException) {
            HttpException httpException = (HttpException) error;
            switch (httpException.code()) {
                case 400:
                    return "Bad Request";
                case 401:
                    return "Unauthorized. Please login again.";
                case 403:
                    return "Forbidden";
                case 404:
                    return "Data not found";
                case 500:
                    return "Server error. Please try again later.";
                default:
                    return "Something went wrong";
            }
        } else if (error instanceof SocketTimeoutException) {
            return "Connection timeout. Please check your internet.";
        } else if (error instanceof IOException) {
            return "No internet connection";
        } else {
            return error.getMessage() != null ? error.getMessage() : "Unknown error";
        }
    }
}
```

#### 6. Memory Management
```java
// ❌ BAD - Memory Leak
public class MyActivity extends AppCompatActivity {
    private static Context context; // Static reference causes leak!
}

// ✅ GOOD - Use Application Context for static
public class MyApp extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
}

// ✅ GOOD - Unregister listeners
@Override
protected void onDestroy() {
    super.onDestroy();
    // Unregister broadcast receivers
    // Cancel pending network requests
    // Remove callbacks
}
```

#### 7. RecyclerView Optimization
```java
public class KaidahAdapter extends RecyclerView.Adapter<KaidahAdapter.ViewHolder> {
    private List<Kaidah> kaidahList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Use ViewBinding for better performance
        ItemKaidahBinding binding = ItemKaidahBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(kaidahList.get(position));
    }

    public void setData(List<Kaidah> newList) {
        // Use DiffUtil for efficient updates
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
            new KaidahDiffCallback(this.kaidahList, newList)
        );
        this.kaidahList = newList;
        diffResult.dispatchUpdatesTo(this);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemKaidahBinding binding;

        ViewHolder(ItemKaidahBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Kaidah kaidah) {
            binding.tvJudul.setText(kaidah.getJudul());
            binding.tvDeskripsi.setText(kaidah.getDeskripsi());
            // Use Glide for image loading
            Glide.with(itemView.getContext())
                .load(kaidah.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(binding.ivKaidah);
        }
    }
}
```

### Naming Conventions

#### CodeIgniter 4
```
Controllers:  PascalCase + Controller suffix
  ✅ KaidahController.php, SoalController.php

Models:       PascalCase + Model suffix
  ✅ KaidahModel.php, UserModel.php

Services:     PascalCase + Service suffix
  ✅ KaidahService.php, LatihanService.php

Views:        snake_case, organized by controller
  ✅ kaidah/index.php, kaidah/create.php (View files, bukan URL)

Variables:    camelCase
  ✅ $kaidahData, $userId, $isActive

Constants:    UPPER_SNAKE_CASE
  ✅ MAX_FILE_SIZE, DEFAULT_SEED

Database:     snake_case
  ✅ kaidah_materi, detail_jawaban_siswa
```

#### Android Java
```
Classes:      PascalCase
  ✅ KaidahActivity, LoginFragment, KaidahAdapter

Interfaces:   PascalCase + Interface/Callback/Listener suffix
  ✅ OnItemClickListener, ApiCallback

Methods:      camelCase, verb-based
  ✅ fetchKaidahList(), saveToDatabase(), isValid()

Variables:    camelCase
  ✅ kaidahList, userId, isLoading

Constants:    UPPER_SNAKE_CASE
  ✅ MAX_RETRY, BASE_URL, REQUEST_CODE

Resources:
  Layouts:    activity_*, fragment_*, item_*
    ✅ activity_login.xml, fragment_home.xml, item_kaidah.xml

  IDs:        type_name
    ✅ tv_title, btn_submit, rv_kaidah_list

  Drawables:  ic_*, bg_*, img_*
    ✅ ic_book.xml, bg_rounded.xml, img_splash.png

  Colors:     descriptive names
    ✅ primary_green, text_secondary, bg_light
```

### Git Workflow & Branching Strategy

```
main/master          - Production-ready code
├── develop          - Integration branch
    ├── feature/login-page
    ├── feature/kaidah-crud
    ├── feature/lcm-algorithm
    ├── bugfix/quiz-timer
    └── hotfix/api-error
```

**Commit Message Format:**
```
<type>(<scope>): <subject>

<body>

<footer>

Types:
- feat:     New feature
- fix:      Bug fix
- docs:     Documentation
- style:    Formatting, missing semicolons, etc
- refactor: Code refactoring
- test:     Adding tests
- chore:    Maintenance

Examples:
✅ feat(auth): add simple login authentication
✅ fix(quiz): correct timer countdown issue
✅ docs(api): update API documentation
✅ refactor(lcm): optimize algorithm performance
```

---

## Database Design (Menggunakan Bahasa Indonesia)

### Catatan Penting:
- **Semua nama tabel, field, dan variabel menggunakan Bahasa Indonesia**
- Database: MySQL/MariaDB
- Charset: utf8mb4_unicode_ci (untuk support teks Arab)

### Standarisasi Field & Konvensi Naming:
- **Timestamp fields**: `waktu_dibuat`, `waktu_diubah` (bukan `created_at`, `updated_at`)
- **Password field**: `kata_sandi` (bukan `password`)
- **Status fields**: Menggunakan UPPERCASE untuk konsistensi
  - `pengguna.status`: `AKTIF`, `NONAKTIF`
  - `siswa.status`: `AKTIF`, `NONAKTIF`
- **Hak akses**: Menggunakan UPPERCASE
  - `pengguna.hak_akses`: `ADMIN`, `GURU`
- **Enum constraints**: Selalu gunakan UPPERCASE di database
- **Validation rules**: Sesuaikan dengan UPPERCASE values
- **API responses**: Return values sesuai database (UPPERCASE)

### Tabel-tabel yang diperlukan:

#### 1. **pengguna**
```sql
- id_pengguna (PK, INT, AUTO_INCREMENT)
- nama_pengguna (VARCHAR(50), UNIQUE)
- kata_sandi (VARCHAR(255), hashed with bcrypt)
- email (VARCHAR(100), UNIQUE)
- nama_lengkap (VARCHAR(100))
- hak_akses (ENUM: 'ADMIN', 'GURU', DEFAULT 'GURU')
  -- admin & guru: untuk web
- foto_profil (VARCHAR(255), NULLABLE)
- status (ENUM: 'AKTIF', 'NONAKTIF', DEFAULT 'AKTIF')
- waktu_dibuat (DATETIME)
- waktu_diubah (DATETIME)
```

**Catatan Penting**:
- `hak_akses = 'ADMIN'` atau `'GURU'` → Bisa login ke web
- Siswa tidak ada di tabel pengguna web (hanya di mobile)

#### 2. **siswa**
```sql
- id (PK, INT, AUTO_INCREMENT)
- nis (VARCHAR(20), UNIQUE) -- Nomor Induk Siswa
- nama_lengkap (VARCHAR(100))
- kata_sandi (VARCHAR(255), hashed with bcrypt)
- jenis_kelamin (ENUM: 'L', 'P')
- kelas (VARCHAR(10))
- status (ENUM: 'AKTIF', 'NONAKTIF', DEFAULT 'AKTIF')
- waktu_dibuat (DATETIME)
- waktu_diubah (DATETIME)
```

**Catatan**:
- Tabel siswa khusus untuk mobile app authentication
- Password digenerate otomatis oleh system
- Login history di track di `siswa_login_history`

#### 3. **siswa_login_history**
```sql
- id (PK, INT, AUTO_INCREMENT)
- nis (FK siswa.nis, VARCHAR(20))
- login_time (DATETIME)
- device_info (VARCHAR(100), NULLABLE)
- ip_address (VARCHAR(45), NULLABLE)
- waktu_dibuat (DATETIME)
```

#### 5. **materi_kaidah**
```sql
- id_materi (PK, INT, AUTO_INCREMENT)
- judul_kaidah (VARCHAR(255))
- deskripsi (TEXT)
- penjelasan (TEXT)
- contoh (TEXT)
- tingkat_kesulitan (ENUM: 'mudah', 'sedang', 'sulit')
- urutan (INT)
- dibuat_oleh (FK pengguna.id_pengguna)
- waktu_dibuat (DATETIME)
- waktu_diubah (DATETIME)
```

#### 6. **soal**
```sql
- id_soal (PK, INT, AUTO_INCREMENT)
- id_materi (FK materi_kaidah.id_materi)
- pertanyaan (TEXT)
- tipe_soal (ENUM: 'pilihan_ganda', DEFAULT 'pilihan_ganda')
- tingkat_kesulitan (ENUM: 'mudah', 'sedang', 'sulit')
- poin (INT, DEFAULT 10)
- dibuat_oleh (FK pengguna.id_pengguna)
- waktu_dibuat (DATETIME)
- waktu_diubah (DATETIME)
```

#### 7. **sesi_latihan**
```sql
- id_sesi (PK, INT, AUTO_INCREMENT)
- id_siswa (FK pengguna.id_pengguna)
- id_materi (FK materi_kaidah.id_materi)
- seed_digunakan (BIGINT) -- Seed LCM yang digunakan (timestamp + user_id)
- total_soal (INT, DEFAULT 20)
- soal_benar (INT, DEFAULT 0)
- skor (DECIMAL(5,2), DEFAULT 0.00)
- waktu_mulai (DATETIME)
- waktu_selesai (DATETIME, NULLABLE)
- durasi_detik (INT, NULLABLE) -- Durasi pengerjaan dalam detik
- status (ENUM: 'sedang_berjalan', 'selesai', DEFAULT 'sedang_berjalan')
- waktu_dibuat (DATETIME)
```

**Catatan LCM**:
- Parameter LCM (a, c, m, X0) **hardcoded di kode** sesuai penelitian:
  - a (pengali) = 10
  - c (penambah) = 23
  - m (modulus) = 29
  - X0 (nilai awal) = seed dari (timestamp + user_id)

#### 8. **detail_jawaban_siswa**
```sql
- id_detail (PK, INT, AUTO_INCREMENT)
- id_sesi (FK sesi_latihan.id_sesi)
- id_soal (FK soal.id_soal)
- id_pilihan (FK pilihan_jawaban.id.pilihan)
- urutan_soal (INT) -- Urutan soal setelah diacak LCM (1-20)
- is_benar (BOOLEAN)
- waktu_jawab (DATETIME)
```

#### 9. **riwayat_belajar**
```sql
- id_riwayat (PK, INT, AUTO_INCREMENT)
- id_siswa (FK pengguna.id_pengguna)
- id_materi (FK materi_kaidah.id_materi)
- status (ENUM: 'belum_dimulai', 'sedang_belajar', 'selesai')
- persentase_penguasaan (DECIMAL(5,2))
- waktu_akses_terakhir (DATETIME)
- waktu_dibuat (DATETIME)
- waktu_diubah (DATETIME)
```

---

## Linear Congruent Method (LCM) Implementation

### Formula LCM:
```
Xn+1 = (a × Xn + c) mod m
```

**Parameter:**
- `Xn` = nilai seed/angka awal
- `a` = multiplier (faktor pengali)
- `c` = increment
- `m` = modulus
- `Xn+1` = bilangan acak berikutnya

### Contoh Implementasi:
```
Seed (X0) = 12
a = 25
c = 16
m = 100

X1 = (25 × 12 + 16) mod 100 = 316 mod 100 = 16
X2 = (25 × 16 + 16) mod 100 = 416 mod 100 = 16
X3 = (25 × 16 + 16) mod 100 = 416 mod 100 = 16
...
```

### Penggunaan dalam Aplikasi:
1. **Generate Random Numbers**: Menghasilkan sequence angka acak
2. **Shuffle Soal**: Mengacak urutan soal berdasarkan ID
3. **Shuffle Jawaban**: Mengacak urutan pilihan jawaban
4. **Reproducible**: Dengan seed yang sama, urutan acak akan sama (untuk tracking/debugging)

### Library Location:
- **Web**: `pembelajaran-kaidah-web/app/Libraries/LCMAlgorithm.php`
- **Mobile**: `PembelajaranKaidah/app/src/main/java/utils/LCMAlgorithm.java`

---

## Development Plan

### Phase 1: Setup & Database (Week 1)
**Web (CodeIgniter 4):**
- [ ] Setup environment & database connection
- [ ] Create migrations untuk semua tabel
- [ ] Create seeders untuk data dummy
- [ ] Setup authentication & authorization
- [ ] Create base models untuk semua tabel

**Mobile (Java):**
- [ ] Setup project structure & dependencies
- [ ] Setup Retrofit/Volley untuk API communication
- [ ] Create utility classes (SharedPreferences, Constants, etc)
- [ ] Setup Room Database (untuk offline support)

### Phase 2: Algorithm Implementation (Week 1-2)
**Web:**
- [ ] Create `LCMAlgorithm.php` library dengan parameter hardcoded
  - Method: `generate($seed, $count)` - generate N random numbers
  - Method: `shuffleArray($array, $seed)` - shuffle array menggunakan LCM
  - Hardcoded parameters: a=10, c=23, m=29
- [ ] Unit testing untuk LCM algorithm

**Mobile:**
- [ ] Create `LCMAlgorithm.java` class dengan parameter hardcoded
  - Method: `generate(long seed, int count)` - generate N random numbers
  - Method: `shuffleList(List items, long seed)` - shuffle list menggunakan LCM
  - Hardcoded parameters: a=10, c=23, m=29
- [ ] Unit testing untuk LCM algorithm

### Phase 3: Web Backend - Admin & Guru Features (Week 2-3)
**Controllers:**
- [ ] `AuthController.php` - Login, Logout (Simple session-based)
- [ ] `DashboardController.php` - Dashboard admin/guru
- [ ] `UserController.php` - CRUD users (admin only)
- [ ] `KaidahController.php` - CRUD materi kaidah
- [ ] `SoalController.php` - CRUD soal & jawaban
- [ ] `LaporanController.php` - Laporan & statistik

**Models:**
- [ ] `UserModel.php`
- [ ] `KaidahModel.php`
- [ ] `SoalModel.php`
- [ ] `JawabanModel.php`
- [ ] `SesiPembelajaranModel.php`
- [ ] `DetailJawabanSiswaModel.php`
- [ ] `ProgressBelajarModel.php`

**Views (Admin/Guru):**
- [ ] Login page (simple session)
- [ ] Dashboard
- [ ] User management
- [ ] Materi kaidah (list, create, edit, detail)
- [ ] Soal management (list, create, edit, detail)
- [ ] Laporan & statistik

### Phase 4: REST API untuk Mobile (Week 3-4)
**API Endpoints:**

**Authentication (Simple):**
- `POST /api/auth/login` - Login siswa (username + password, return user_id)
- `POST /api/auth/register` - Register siswa
- `GET /api/auth/profile` - Get profile siswa (kirim user_id di param)

**Materi Kaidah:**
- `GET /api/kaidah` - List semua materi kaidah
- `GET /api/kaidah/{id}` - Detail materi kaidah
- `GET /api/kaidah/{id}/progress` - Progress belajar siswa untuk kaidah tertentu

**Soal & Pembelajaran:**
- `POST /api/sesi/start` - Mulai sesi pembelajaran baru (generate soal dengan LCM)
- `GET /api/sesi/{id}` - Get detail sesi pembelajaran
- `POST /api/sesi/{id}/jawab` - Submit jawaban soal
- `POST /api/sesi/{id}/finish` - Selesaikan sesi pembelajaran
- `GET /api/sesi/{id}/hasil` - Get hasil sesi pembelajaran

**Progress & History:**
- `GET /api/progress` - Progress belajar siswa (semua kaidah)
- `GET /api/history` - Riwayat pembelajaran siswa
- `GET /api/statistik` - Statistik pembelajaran siswa


### Phase 5: Mobile App Development (Week 4-6)
**Activities/Fragments:**
- [ ] `SplashActivity` - Splash screen
- [ ] `LoginActivity` - Login siswa
- [ ] `RegisterActivity` - Register siswa
- [ ] `MainActivity` - Main container dengan bottom navigation
- [ ] `HomeFragment` - Dashboard siswa
- [ ] `KaidahListFragment` - List materi kaidah
- [ ] `KaidahDetailActivity` - Detail & penjelasan materi kaidah
- [ ] `QuizActivity` - Mengerjakan soal (soal telah diacak dengan LCM)
- [ ] `HasilActivity` - Hasil & skor setelah mengerjakan soal
- [ ] `ProgressFragment` - Progress belajar siswa
- [ ] `HistoryFragment` - Riwayat pembelajaran
- [ ] `ProfileFragment` - Profile siswa

**Features:**
- [ ] Local caching dengan Room Database
- [ ] Offline mode untuk membaca materi
- [ ] Timer untuk sesi pembelajaran
- [ ] Notifikasi reminder belajar
- [ ] Share hasil belajar
- [ ] Dark mode support

### Phase 6: Integration & Testing (Week 6-7)
- [ ] Integration testing web & mobile
- [ ] Testing LCM algorithm dengan berbagai parameter
- [ ] Testing randomness quality (chi-square test, dll)
- [ ] User acceptance testing (UAT)
- [ ] Performance testing
- [ ] Security testing

### Phase 7: Deployment & Documentation (Week 7-8)
- [ ] Setup production environment
- [ ] Deploy web application
- [ ] **Build & sign APK untuk mobile (Manual via Android Studio)**
  - ❌ **Tidak gunakan** `./gradlew assembleDebug`
  - ✅ **Gunakan** Android Studio → Build → Build Bundle(s) / APK(s) → Build APK(s)
  - ✅ **Gunakan** `./gradlew build` hanya untuk compilation check
- [ ] Create user documentation
- [ ] Create technical documentation
- [ ] Training untuk admin & guru

---

## LCM Algorithm Flow

### 1. Generate Soal Acak untuk Siswa
```
1. Siswa memilih materi kaidah
2. Sistem ambil semua soal untuk kaidah tersebut
3. Sistem ambil active LCM config (atau gunakan default)
4. Generate seed berdasarkan: user_id + timestamp + kaidah_id
5. Gunakan LCM untuk generate random indices
6. Ambil soal berdasarkan indices yang sudah diacak
7. Untuk setiap soal, acak juga urutan jawabannya dengan LCM
8. Simpan seed yang digunakan ke sesi_pembelajaran
9. Return soal yang sudah diacak ke siswa
```

### 2. Validasi Jawaban
```
1. Siswa submit jawaban
2. Sistem cari soal berdasarkan ID
3. Cek jawaban benar/salah
4. Simpan ke detail_jawaban_siswa
5. Update progress sesi
6. Return feedback ke siswa
```

### 3. Selesai Pembelajaran
```
1. Siswa finish sesi
2. Sistem hitung total skor
3. Update sesi_pembelajaran (status = completed)
4. Update progress_belajar
5. Generate laporan hasil
6. Return hasil ke siswa
```

---

## Tech Stack

### Web Application (CodeIgniter 4)
- **Framework**: CodeIgniter 4.x
- **PHP Version**: 7.4 atau lebih tinggi
- **Database**: MySQL/MariaDB
- **Frontend**:
  - AdminLTE 3 / Bootstrap 5
  - jQuery
  - Chart.js (untuk statistik)
  - DataTables (untuk tabel)
  - **Notyf.js** (Toast notifications - <3KB gzipped)
  - Tabler Icons (Icon library)
- **Authentication**: Simple Session-based (Web) & Simple API (Mobile)

### Mobile Application (Java/Android)
- **Language**: Java
- **Min SDK**: API 21 (Android 5.0 Lollipop)
- **Target SDK**: API 33 (Android 13)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Libraries**:
  - Retrofit 2 (HTTP client)
  - Gson (JSON parsing)
  - Room (Local database)
  - Glide/Picasso (Image loading)
  - Material Design Components
  - RecyclerView
  - ViewModel & LiveData
  - SharedPreferences

---

## 📚 Notyf.js Documentation

### Overview
**Notyf.js** is a minimalistic toast notification library used in this project for displaying user-friendly notifications instead of traditional Bootstrap alerts.

**Official Repository**: https://github.com/caroso1222/notyf
**License**: MIT
**Size**: <3KB gzipped

### Key Features
- 📱 **Responsive design** - Works on all screen sizes
- 👓 **A11Y compatible** - Accessible for screen readers
- 🔥 **TypeScript typings** - Full TypeScript support
- ⚡️ **Multiple bundle types** - ES6, CommonJS, UMD, IIFE
- 🐣 **Tiny footprint** - Less than 3KB when gzipped
- ✨ **Optional ripple effects** - Beautiful animation effects
- 🎃 **Custom HTML content** - Support for custom content

### Installation & Setup

#### Local Files (This Project)
```html
<!-- CSS -->
<link rel="stylesheet" href="<?= base_url('assets/libs/notyf/notyf.min.css') ?>" />

<!-- JavaScript -->
<script src="<?= base_url('assets/libs/notyf/notyf.min.js') ?>"></script>
<script src="<?= base_url('assets/js/toast-helper.js') ?>"></script>
```

#### NPM Installation
```bash
npm i notyf
```

### Basic Usage

#### Simple Notifications
```javascript
// Create instance
const notyf = new Notyf();

// Show notifications
notyf.error('Form validation failed');
notyf.success('Changes saved successfully');
```

#### One-Liner Methods (This Project)
```javascript
// Success toast
toast.success('Data berhasil disimpan!');

// Error toast
toast.error('Terjadi kesalahan');

// Warning toast
toast.warning('Periksa input Anda');

// Info toast
toast.info('Proses sedang berjalan');
```

### Configuration Options

#### Basic Configuration
```javascript
const notyf = new Notyf({
  duration: 5000,              // Auto-dismiss after 5 seconds
  position: { x: 'right', y: 'top' },  // Position
  ripple: true,                  // Enable ripple effect
  dismissible: true              // Allow manual dismiss
});
```

#### Custom Types
```javascript
const notyf = new Notyf({
  types: [
    {
      type: 'warning',
      background: 'orange',
      className: 'notyf__toast--warning',
      icon: {
        className: 'ti ti-alert-triangle',
        tagName: 'i',
        text: '',
        color: 'white'
      }
    }
  ]
});
```

### Icon Configuration (This Project)
Icons use Tabler Icons library with proper configuration:

```javascript
icon: {
  className: 'ti ti-circle-check',  // Tabler Icons class
  tagName: 'i',                     // HTML tag
  text: '',                        // Empty for font icons
  color: 'white'                   // Icon color
}
```

#### Available Icons in This Project
- ✅ **Success**: `ti ti-circle-check` (Green #4CAF50)
- ❌ **Error**: `ti ti-x-circle` (Red #F44336)
- ⚠️ **Warning**: `ti ti-alert-triangle` (Orange #FF9800)
- ℹ️ **Info**: `ti ti-info-circle` (Blue #2196F3)

### Enhanced Alert System (Custom Implementation)

**Important Note**: Notyf.js does NOT include native confirm dialog functionality. The `toast.confirm()` function in this project is a **custom implementation** using Bootstrap modals.

#### Custom Confirm Dialog Features
```javascript
// Custom confirm dialog with Bootstrap modals
toast.confirm(
    'Apakah Anda yakin ingin menghapus data ini?',
    function() { /* onConfirm */ },
    function() { /* onCancel */ },
    {
        title: 'Konfirmasi Hapus',
        confirmText: 'Hapus',
        cancelText: 'Batal',
        confirmClass: 'btn-danger',
        cancelClass: 'btn-secondary'
    }
);
```

#### Key Features of Custom Implementation
- 🎨 **Bootstrap Modal Integration** - Uses native Bootstrap 5 modal component
- 🎯 **Tabler Icons Support** - Integrated with project's icon system
- ⌨️ **Keyboard Accessibility** - ESC key to close, Enter to confirm
- 📱 **Responsive Design** - Works on all screen sizes
- 🔄 **Loading States** - Integrated with `toast.loading()` for async operations
- 🎭 **Backdrop Click** - Click outside modal to cancel
- 🎪 **Customizable Options** - Flexible configuration for different use cases

#### Implementation Details
```javascript
// Location: public/assets/js/toast-helper.js

confirm: function(message, onConfirm, onCancel = null, options = {}) {
    // Creates Bootstrap modal dynamically
    const modalOverlay = document.createElement('div');
    modalOverlay.className = 'modal fade show';
    modalOverlay.style.cssText = 'display: block; background-color: rgba(0,0,0,0.5);';

    // Modal structure with Tabler Icons
    modalOverlay.innerHTML = `
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header border-0">
                    <h5 class="modal-title">
                        <i class="ti ti-help-circle me-2 text-primary"></i>
                        ${config.title}
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p class="mb-0">${message}</p>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn ${config.cancelClass} btn-cancel">
                        <i class="ti ti-x me-1"></i>${config.cancelText}
                    </button>
                    <button type="button" class="btn ${config.confirmClass} btn-confirm">
                        <i class="ti ti-check me-1"></i>${config.confirmText}
                    </button>
                </div>
            </div>
        </div>
    `;

    // Event listeners and keyboard support
    // ... (see full implementation in toast-helper.js)
}
```

#### Usage Examples from the Project

**1. Status Toggle Confirmation**
```javascript
// From: app/Views/guru/index.php
function toggleStatus(id) {
    const currentStatus = document.querySelector(`button[onclick="toggleStatus(${id})"]`).getAttribute('data-status');
    const actionText = currentStatus === 'AKTIF' ? 'menonaktifkan' : 'mengaktifkan';

    toast.confirm(
        `Apakah Anda yakin ingin ${actionText} guru ini?`,
        function() {
            const loading = toast.loading('Mengubah status...');

            fetch(`<?= site_url('guru/') ?>${id}/toggleStatus`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    toast.success('Status guru berhasil diperbarui!');
                    setTimeout(() => location.reload(), 1000);
                } else {
                    toast.error(data.message || 'Gagal mengubah status');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                toast.error('Terjadi kesalahan saat mengubah status');
            })
            .finally(() => {
                loading.dismiss();
            });
        }
    );
}
```

**2. Delete Confirmation with Custom Options**
```javascript
// From: app/Views/guru/index.php
function confirmDelete(id) {
    toast.confirm(
        'Apakah Anda yakin ingin menghapus guru ini? Data yang dihapus tidak dapat dikembalikan.',
        function() {
            const loading = toast.loading('Menghapus guru...');

            // Submit form via POST
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = `<?= site_url('guru/delete/') ?>${id}`;

            // Add CSRF token
            const csrfToken = document.querySelector('meta[name="csrf-token"]');
            if (csrfToken) {
                const csrfInput = document.createElement('input');
                csrfInput.type = 'hidden';
                csrfInput.name = '<?= csrf_token() ?>';
                csrfInput.value = '<?= csrf_hash() ?>';
                form.appendChild(csrfInput);
            }

            document.body.appendChild(form);
            form.submit();

            setTimeout(() => loading.dismiss(), 2000);
        },
        null, // No cancel callback needed
        {
            title: 'Hapus Guru',
            confirmText: 'Hapus',
            confirmClass: 'btn-danger' // Red button for destructive action
        }
    );
}
```

#### Integration with Loading States
The custom confirm dialog integrates seamlessly with the loading toast system:

```javascript
// Show loading state
const loading = toast.loading('Processing...');

// Perform async operation
fetch('/api/endpoint', { /* options */ })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            toast.success('Operation completed!');
        } else {
            toast.error('Operation failed!');
        }
    })
    .catch(error => toast.error('Network error!'))
    .finally(() => loading.dismiss()); // Dismiss loading toast
```

#### Best Practices for Custom Confirm Dialogs

✅ **DO:**
- Use clear, specific action text (e.g., "Hapus Guru" instead of "Konfirmasi")
- Use appropriate button colors (danger for delete, success for confirm)
- Provide context in the confirmation message
- Use loading states for async operations
- Handle both success and error scenarios

❌ **DON'T:**
- Use generic confirm messages without context
- Forget to handle the cancel callback
- Use the same button style for all actions
- Overload users with too many confirmations
- Forget proper error handling

#### Benefits Over Native Browser Confirms

1. **Better UX**: Modern, styled dialogs vs. browser-native ugly dialogs
2. **Icon Integration**: Consistent with project's Tabler Icons system
3. **Keyboard Support**: Full accessibility with keyboard navigation
4. **Customization**: Flexible styling and behavior options
5. **Loading Integration**: Seamless integration with loading states
6. **Mobile Friendly**: Responsive design that works on all devices
7. **Brand Consistency**: Matches overall application design system

### Event Handling
```javascript
const notification = notyf.success('Message');

// Handle click events
notification.on('click', ({target, event}) => {
  console.log('Toast clicked!');
});

// Handle dismiss events
notification.on('dismiss', ({target, event}) => {
  console.log('Toast dismissed!');
});
```

### Advanced Usage

#### Custom Helper Functions (This Project)
```javascript
// AppMessages helper for common operations
AppMessages.saveSuccess('Siswa');
AppMessages.updateSuccess('Data');
AppMessages.deleteSuccess('Pengguna');

// Loading toast (no auto-dismiss)
const loadingToast = AppMessages.loading('Processing...');
// Manual dismiss: loadingToast.dismiss();
```

#### Custom Toast with Specific Options
```javascript
toast.custom({
  message: 'Custom notification',
  icon: 'ti ti-upload',
  color: '#4CAF50',
  duration: 7000
});
```

### Integration with CodeIgniter 4

#### Flash Messages Integration
```php
// In Controller
return redirect()->to('/siswa')
    ->with('success', 'Data siswa berhasil ditambahkan');
```

#### Automatic Toast Conversion
Flash messages are automatically converted to toasts via `partials/flash_messages.php`:
```php
<?php if (session()->has('success')): ?>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            toast.success('<?= esc(str_replace("'", "\'", session('success'))) ?>');
        });
    </script>
<?php endif; ?>
```

### Best Practices

#### Do's
✅ Use consistent messaging
✅ Keep messages short and clear
✅ Use appropriate toast types (success/error/warning/info)
✅ Set appropriate duration (3-7 seconds)
✅ Test on different screen sizes

#### Don'ts
❌ Overuse toasts for minor actions
❌ Show multiple toasts simultaneously
❌ Use long paragraphs in toasts
❌ Ignore accessibility requirements

### Troubleshooting

#### Common Issues

1. **Icons not showing**
   - Verify Tabler Icons CSS is loaded
   - Check `tabler-icons.css` in `<head>`

2. **Toast not appearing**
   - Check browser console for errors
   - Verify `toast-helper.js` is loaded
   - Ensure `DOMContentLoaded` event fires

3. **Flash messages not working**
   - Verify `flash_messages.php` is included
   - Check session data is available
   - Ensure proper escaping of quotes

### Performance Impact
- **File Size**: 13KB total (6KB CSS + 7KB JS)
- **Load Time**: < 100ms on 3G connection
- **Memory Usage**: Minimal (< 1MB)
- **No jQuery dependency**: Faster than alternatives

---

## API Documentation Template (Updated November 2025)

### 🔑 **Authentication & Token**
**Note:** AuthController telah dihapus karena user login via web. Gunakan token manual untuk testing:
```bash
# Generate token untuk siswa ID 1
echo -n "1:$(date +%s)" | base64
# Result: MToxNzYyNTkxMDYw (example)
```

### 📋 **Complete API Routes List**

#### **1. Kaidah Management API**
- `GET /api/kaidah` - Get all kaidah list with pagination
- `GET /api/kaidah/{id}` - Get kaidah detail by ID

#### **2. Session Management API**
- `POST /api/sesi/start` - Start new learning session
- `GET /api/sesi/active` - Get active session for user
- `GET /api/sesi/{id}` - Get session details by ID
- `POST /api/sesi/{id}/jawab` - Submit answer for current question
- `POST /api/sesi/{id}/finish` - Complete/finish session
- `GET /api/sesi/{id}/hasil` - Get session results

#### **3. Progress & Analytics API**
- `GET /api/progress` - Get overall student progress
- `GET /api/progress/statistics` - Get detailed statistics
- `GET /api/progress/detail` - Get detailed progress with filters
- `GET /api/progress/history` - Get learning history
- `GET /api/progress/chart` - Get chart data (line/bar/radar)

### 📝 **API Response Format**
**All API responses include:**
```json
{
  "status": "success|error",
  "message": "Description message",
  "code": 200, // Success code (added for Android validation)
  "data": { ... } // Response data
}
```

### 📖 **Detailed API Documentation**

#### **1. Kaidah Management API**

**GET /api/kaidah**
**Request Headers:**
```
Authorization: Bearer {token}
```

**Query Parameters:**
- `search` (optional) - Search by title or description
- `page` (optional) - Page number (default: 1)
- `limit` (optional) - Items per page (default: 20)

**Response Success (200):**
```json
{
  "status": "success",
  "message": "Daftar kaidah berhasil diambil",
  "code": 200,
  "data": {
    "kaidah": [
      {
        "id_materi": 1,
        "judul_kaidah": "Pengenalan Kalam",
        "deskripsi": "Pengenalan kalam dalam bahasa Arab",
        "total_soal": 10,
        "progress_percentage": 0,
        "status": "belum_dimulai",
        "is_locked": false
      }
    ],
    "pagination": {
      "current_page": 1,
      "per_page": 20,
      "total": 40,
      "total_pages": 2
    }
  }
}
```

**GET /api/kaidah/{id}**
**Request Headers:**
```
Authorization: Bearer {token}
```

**Response Success (200):**
```json
{
  "status": "success",
  "message": "Detail kaidah berhasil diambil",
  "code": 200,
  "data": {
    "kaidah": {
      "id_materi": 1,
      "judul_kaidah": "Pengenalan Kalam",
      "deskripsi": "Pengenalan kalam dalam bahasa Arab",
      "penjelasan": "Kalam adalah kata yang bermakna...",
      "contoh": "كتابٌ (kitabun) - buku",
      "total_soal": 10
    }
  }
}
```

#### **2. Session Management API**

**POST /api/sesi/start**
**Request Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "kaidah_id": 1,
  "jumlah_soal": 10
}
```

**Response Success (200):**
```json
{
  "status": "success",
  "message": "Sesi pembelajaran berhasil dimulai",
  "code": 200,
  "data": {
    "sesi": {
      "id_sesi": 123,
      "kaidah_id": 1,
      "judul_kaidah": "Pengenalan Kalam",
      "jumlah_soal": 10,
      "seed_used": 1731067652,
      "waktu_mulai": "2025-11-08 08:30:00"
    },
    "soal": [
      {
        "nomor": 1,
        "id_soal": 45,
        "pertanyaan": "Apa bentuk jamak dari كتاب؟",
        "tipe_soal": "pilihan_ganda",
        "poin": 10,
        "jawaban": [
          {
            "id_pilihan": 123,
            "jawaban": "كُتُب",
            "is_benar": true
          }
        ]
      }
    ],
    "lcm_info": {
      "algorithm": "Linear Congruent Method",
      "parameters": {
        "a": 10,
        "c": 23,
        "m": 29
      },
      "seed": 1731067652,
      "randomization_verified": true
    }
  }
}
```

**GET /api/sesi/active**
**Request Headers:**
```
Authorization: Bearer {token}
```

**Response Success (200):**
```json
{
  "status": "success",
  "message": "Sesi aktif ditemukan",
  "code": 200,
  "data": {
    "sesi": {
      "id_sesi": 123,
      "kaidah_id": 1,
      "judul_kaidah": "Pengenalan Kalam",
      "total_soal": 10,
      "soal_benar": 3,
      "skor": 30.0,
      "waktu_mulai": "2025-11-08 08:30:00",
      "durasi_detik": 1800,
      "status": "sedang_berjalan"
    }
  }
}
```

**POST /api/sesi/{id}/jawab**
**Request Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "id_soal": 45,
  "id_pilihan": 123
}
```

**Response Success (200):**
```json
{
  "status": "success",
  "message": "Jawaban berhasil disimpan",
  "code": 200,
  "data": {
    "is_benar": true,
    "id_soal": 45,
    "id_pilihan": 123
  }
}
```

**POST /api/sesi/{id}/finish**
**Request Headers:**
```
Authorization: Bearer {token}
```

**Response Success (200):**
```json
{
  "status": "success",
  "message": "Sesi pembelajaran selesai",
  "code": 200,
  "data": {
    "sesi": {
      "id_sesi": 123,
      "total_soal": 10,
      "soal_benar": 8,
      "skor_akhir": 80.0,
      "persentase_benar": 80.0,
      "durasi_detik": 3600,
      "waktu_selesai": "2025-11-08 09:30:00"
    }
  }
}
```

**GET /api/sesi/{id}/hasil**
**Request Headers:**
```
Authorization: Bearer {token}
```

**Response Success (200):**
```json
{
  "status": "success",
  "message": "Hasil sesi pembelajaran",
  "code": 200,
  "data": {
    "sesi": {
      "id_sesi": 123,
      "kaidah": {
        "id_materi": 1,
        "judul_kaidah": "Pengenalan Kalam"
      },
      "total_soal": 10,
      "soal_benar": 8,
      "soal_salah": 2,
      "skor": 80.0,
      "persentase_benar": 80.0,
      "waktu_mulai": "2025-11-08 08:30:00",
      "waktu_selesai": "2025-11-08 09:30:00",
      "durasi_detik": 3600,
      "status": "selesai"
    },
    "penilaian": {
      "predikat": "B",
      "deskripsi": "Baik"
    }
  }
}
```

#### **3. Progress & Analytics API**

**GET /api/progress**
**Request Headers:**
```
Authorization: Bearer {token}
```

**Response Success (200):**
```json
{
  "status": "success",
  "message": "Progress berhasil diambil",
  "code": 200,
  "data": {
    "siswa": {
      "id": "1",
      "nama_lengkap": "Ahmad Fauzi",
      "kelas": "XI-A",
      "status": "AKTIF"
    },
    "overview": {
      "total_kaidah": 40,
      "kaidah_selesai": 5,
      "kaidah_sedang_belajar": 2,
      "kaidah_belum_dimulai": 33,
      "total_sesi": 7,
      "rata_rata_skor": 75.5,
      "total_soal_dijawab": 140,
      "total_jawaban_benar": 105,
      "persentase_benar_keseluruhan": 75.0,
      "persentase_kemajuan": 12.5
    },
    "kaidah_progress": [...],
    "weekly_activity": [...],
    "achievements": [...]
  }
}
```

**GET /api/progress/statistics**
**Request Headers:**
```
Authorization: Bearer {token}
```

**Response Success (200):**
```json
{
  "status": "success",
  "message": "Statistik progress berhasil diambil",
  "code": 200,
  "data": {
    "session_statistics": {
      "total_sessions": 7,
      "completed_sessions": 7,
      "cancelled_sessions": 0,
      "completion_rate": 100.0,
      "average_score": 75.5,
      "best_score": 95.0,
      "worst_score": 60.0,
      "total_questions_answered": 140,
      "total_correct_answers": 105,
      "overall_accuracy": 75.0,
      "average_duration_minutes": 12.5
    },
    "kaidah_breakdown": [
      {
        "id_materi": 1,
        "judul_kaidah": "Pengenalan Kalam",
        "total_sessions": 2,
        "average_score": 85.0,
        "best_score": 95.0
      }
    ],
    "monthly_performance": [...],
    "learning_streak": {
      "current_streak": 3,
      "longest_streak": 7
    }
  }
}
```

**GET /api/progress/chart**
**Request Headers:**
```
Authorization: Bearer {token}
```

**Query Parameters:**
- `type` (optional) - Chart type: line, bar, radar (default: line)
- `period` (optional) - Period: week, month, year (default: month)

**Response Success (200):**
```json
{
  "status": "success",
  "message": "Data chart progress berhasil diambil",
  "code": 200,
  "data": {
    "chart_type": "radar",
    "period": "month",
    "chart_data": [
      {
        "kaidah": "Pengenalan Kalam",
        "score": 85.0,
        "sessions": 2
      },
      {
        "kaidah": "Huruf-huruf Kalam",
        "score": 70.0,
        "sessions": 1
      }
    ]
  }
}
```

### ⚠️ **Important Notes**

1. **Token Authentication**: All API calls require `Authorization: Bearer {token}` header
2. **Code Field**: All responses include `code: 200` for Android validation
3. **No AuthController**: Login via web only, use manual token for testing
4. **Error Handling**: All errors return consistent format with `status: "error"`
5. **Pagination**: Use `page` and `limit` parameters for large datasets
6. **LCM Algorithm**: Session questions are randomized using Linear Congruent Method

---

## Quick Reference for API Testing

### 🧪 **Generate Token**
```bash
# Generate token untuk siswa ID 1
echo -n "1:$(date +%s)" | base64
# Example result: MToxNzYyNTkxMDYw
```

### 🚀 **Test Commands**
```bash
# Test Kaidah List
curl -X GET "http://localhost:8080/api/kaidah" \
  -H "Authorization: Bearer MToxNzYyNTkxMDYw"

# Test Progress Statistics
curl -X GET "http://localhost:8080/api/progress/statistics" \
  -H "Authorization: Bearer MToxNzYyNTkxMDYw"

# Test Chart API
curl -X GET "http://localhost:8080/api/progress/chart?type=radar" \
  -H "Authorization: Bearer MToxNzYyNTkxMDYw"

# Start Session (POST)
curl -X POST "http://localhost:8080/api/sesi/start" \
  -H "Authorization: Bearer MToxNzYyNTkxMDYw" \
  -H "Content-Type: application/json" \
  -d '{"kaidah_id": 1, "jumlah_soal": 10}'
```

---


---

## 📝 Catatan Akhir

Dokumen ini mencakup dokumentasi lengkap untuk proyek **Aplikasi Pembelajaran Kaidah Bahasa Arab dengan Algoritma Linear Congruent Method (LCM)**. Untuk pertanyaan atau bantuan lebih lanjut, silakan hubungi tim pengembang.

**Status Terkini: November 2025**
- ✅ Backend API selesai dengan `code: 200` di semua response
- ✅ Android build tersedia (build manual melalui Android Studio)
- ✅ Dokumentasi API lengkap tersedia
- 🚧 Siap untuk testing dan deployment

**Catatan Build Android:**
- Build aplikasi Android dilakukan secara manual melalui Android Studio
- Tidak menggunakan `./gradlew assembleDebug` otomatis untuk menghindari konflik
- Proses build: `./gradlew build` untuk compilation check, build APK melalui Android Studio

---

## 🐛 Bug Fixes & Improvements Log (November 2025)

### **9 November 2025 - Progress Tracking & API Sync Fixes**

#### **Problem 1: Progress Count Always 0% in Kaidah List** ⭐⭐⭐
**Symptoms:**
- Bab progress bar menampilkan 0% meskipun sudah ada materi yang selesai
- Status materi stuck di "Belum Dimulai" tidak berubah
- Data progress tidak muncul di Android app

**Root Cause Analysis:**
1. API `/api/progress` menggunakan hardcoded completion_percentage (100, 50, atau 0)
2. API `/api/kaidah/grouped` tidak mengirim data progress sama sekali
3. Android code mencari `overview.kaidah_progress` yang tidak ada di response

**Fixes Applied:**

**A. API Progress Calculation (`ProgressController.php:76-131`)**
```php
// Before: Hardcoded values
'completion_percentage' => $status === 'selesai' ? 100 : ($status === 'sedang_belajar' ? 50 : 0),

// After: Real values from database
$completionPercentage = (float) $riwayat['persentase_penguasaan'];
if (!empty($kaidahSessions)) {
    $bestScore = max(array_column($kaidahSessions, 'skor'));
    if ($bestScore >= 80) {
        $status = 'selesai';
        $completionPercentage = 100;
    } else {
        $completionPercentage = (float) $bestScore;
    }
}
```

**B. Grouped Kaidah API Enhancement (`KaidahController.php:165-334`)**
```php
// Added progress data to each materi
foreach ($kaidahList as $kaidah) {
    $riwayat = $this->riwayatBelajarModel
        ->where('id_siswa', $userId)
        ->where('id_materi', $kaidah['id_materi'])
        ->first();

    $progressPercentage = $riwayat ? (float) $riwayat['persentase_penguasaan'] : 0;
    $status = $riwayat ? $riwayat['status'] : 'belum_dimulai';

    // Add to response
    $processedKaidah['progress_percentage'] = round($progressPercentage, 2);
    $processedKaidah['status'] = $status;
    $processedKaidah['completed'] = $progressPercentage >= 100;
}

// Calculate Bab progress
$babProgressPercentage = $totalMateri > 0
    ? round(($completedMateri / $totalMateri) * 100, 2)
    : 0;
```

**C. Progress API Overview Fix (`ProgressController.php:150-163`)**
```php
// Added kaidah_progress to overview for Android compatibility
'overview' => [
    // ... existing fields
    'kaidah_progress' => $kaidahProgress  // NEW: Added here
],
'kaidah_progress' => $kaidahProgress  // Also kept at root level
```

**Impact:**
- ✅ Bab progress bar shows correct percentage
- ✅ Materi status updates correctly (belum_dimulai → sedang_belajar → selesai)
- ✅ Completion percentage uses real values from database
- ✅ No more "kaidah_progress key not found" errors

---

#### **Problem 2: Materi Completion Sync - 404 Not Found** ⭐⭐
**Symptoms:**
```
POST http://192.168.1.4:8080/api/api/progress/materi/2/complete
→ 404 Not Found: Can't find a route for 'POST: api/api/progress/materi/2/complete'
```

**Root Cause:**
Double `/api/api` prefix caused by:
- `BASE_URL` = `http://192.168.1.4:8080/api/`
- `MATERI_COMPLETE` = `"api/progress/materi/{id}/complete"`
- Result: `api/` + `api/progress/...` = `api/api/progress/...` ❌

**Fix Applied:**
```java
// ApiConstants.java:29
// Before:
public static final String MATERI_COMPLETE = "api/progress/materi/{id}/complete";

// After:
public static final String MATERI_COMPLETE = "progress/materi/{id}/complete";
```

**Impact:**
- ✅ URL sekarang correct: `http://192.168.1.4:8080/api/progress/materi/2/complete`
- ✅ Route found successfully with 200 OK response

---

#### **Problem 3: Materi Completion Sync - 401 Unauthorized** ⭐⭐
**Symptoms:**
```
POST http://192.168.1.4:8080/api/progress/materi/2/complete
→ 401 Unauthorized: Token diperlukan
```

**Root Cause:**
- Backend API requires Authorization header
- Android app not sending Authorization header in API call

**Fixes Applied:**

**A. ApiService.java - Add auth parameter**
```java
// Before:
@POST(ApiConstants.MATERI_COMPLETE)
Call<ApiResponse<Map<String, Object>>> completeMateri(
    @Path("id") int materiId
);

// After:
@POST(ApiConstants.MATERI_COMPLETE)
Call<ApiResponse<Map<String, Object>>> completeMateri(
    @Header("Authorization") String authToken,
    @Path("id") int materiId
);
```

**B. KaidahDetailFragment.java - Pass auth token**
```java
// Before:
apiService.completeMateri(materiId);

// After:
apiService.completeMateri("Bearer " + authToken, materiId);
```

**Impact:**
- ✅ API call successful with 200 OK
- ✅ Progress synced with server correctly
- ✅ Toast message: "Progress berhasil disinkron dengan server"

---

### **Summary of Changes - 9 November 2025**

**Web Backend (CodeIgniter 4):**
- ✅ `app/Controllers/API/ProgressController.php` - Fixed completion_percentage calculation
- ✅ `app/Controllers/API/ProgressController.php` - Added kaidah_progress to overview
- ✅ `app/Controllers/API/KaidahController.php` - Enhanced grouped API with progress data

**Android App (Java):**
- ✅ `ApiConstants.java` - Fixed double /api prefix
- ✅ `ApiService.java` - Added Authorization header parameter
- ✅ `KaidahDetailFragment.java` - Pass auth token in API call

**Results:**
- 🎯 **Progress tracking** working correctly across web and mobile
- 🎯 **Bab progress bars** showing accurate completion percentages
- 🎯 **Materi status** updating dynamically based on user progress
- 🎯 **Server sync** functioning with proper authentication

**Testing Status:**
- ✅ Bab list shows correct progress percentages
- ✅ Materi status updates (belum_dimulai/sedang_belajar/selesai)
- ✅ Materi completion syncs to server successfully
- ✅ No 404 or 401 errors in API calls
- ✅ Progress data persists across app sessions

---

**Document Information:**
- **Last Updated:** 9 November 2025
- **Version:** 2.2
- **Status:** Progress Tracking System Fully Functional
- **Total API Routes:** 11 endpoints
- **Database Tables:** 6 primary tables seeded
- **Recent Fixes:** 3 major bugs fixed (Progress calculation, API routing, Authorization)
