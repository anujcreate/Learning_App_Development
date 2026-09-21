# YouCalculator 📱

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform" />
  <img src="https://img.shields.io/badge/Kotlin-2.1.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-2025.02.00-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/Architecture-MVI%20%2B%20Clean-FF6F00?style=for-the-badge" alt="Architecture" />
  <img src="https://img.shields.io/badge/Min%20SDK-26%2B-00C853?style=for-the-badge" alt="Min SDK" />
  <img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" alt="License" />
</p>

<p align="center">
  <strong>A modern, high-precision Android Calculator built with 100% Jetpack Compose, Clean Architecture, and MVI pattern.</strong>
  <br />
  Designed with an iOS-inspired aesthetic, vibrant orange accents, persistent Room history, and buttery-smooth screen transitions.
</p>

---

## ✨ Features

- 🎨 **Sleek Dual Themes**:
  - **OLED Dark Mode**: Deep `#000000` background with circular dark-gray keys and iOS-style vibrant orange operators (`#FF9F0A`).
  - **High-Contrast Light Mode**: Clean `#F2F2F7` background with elevated white cards and crisp typography.
- ⚡ **Dual-Line Dynamic Display**:
  - Top line: Real-time formula / expression preview formatted with locale-aware thousands grouping.
  - Bottom line: Prominent, auto-scaling calculation result.
- 🧮 **Arbitrary-Precision Math**:
  - Powered by `BigDecimal` to eliminate IEEE 754 floating-point errors (e.g. `0.1 + 0.2 = 0.3`).
  - Full support for percentage, sign toggle (`+/-`), backspace, and chain operations.
- 📜 **Persistent Calculation History**:
  - Saved automatically to a local SQLite database via **Room**.
  - Interactive history screen with tap-to-copy functionality and one-click clear.
- 🚀 **Edge-to-Edge & Safe Insets**:
  - Built with `WindowInsets.safeDrawing`, automatically adapting to notches, punch-hole cameras, and system navigation bars without awkward gaps.
- 🎬 **Shared Axis Transitions**:
  - Fluid, physics-based horizontal slide and parallax transitions (`FastOutSlowInEasing`) when navigating to and from the History screen.

---

## 🛠️ Tech Stack & Architecture

YouCalculator follows Google's official [Guide to App Architecture](https://developer.android.com/topic/architecture) paired with **Clean Architecture** and **MVI (Model-View-Intent)** principles.

```
┌────────────────────────────────────────────────────────┐
│                   Presentation Layer                   │
│   (Jetpack Compose UI • StateFlow • Channel Events)    │
└───────────────────────────┬────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────┐
│                      Domain Layer                      │
│     (Use Cases • Repository Interfaces • Domain Models)│
└───────────────────────────┬────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────┐
│                       Data Layer                       │
│    (Room Database • DAOs • Entities • Repositories)    │
└────────────────────────────────────────────────────────┘
```

### Core Technologies

| Layer / Concern | Technology | Description |
| :--- | :--- | :--- |
| **UI Framework** | [Jetpack Compose](https://developer.android.com/jetpack/compose) | Declarative UI toolkit with Material 3 components |
| **Language** | [Kotlin](https://kotlinlang.org/) (v2.1.0) | Modern, expressive, and concise programming language |
| **Architecture** | **MVI + Clean Architecture** | Unidirectional Data Flow (UDF) with immutable `@Immutable` State |
| **Dependency Injection**| [Koin](https://insert-koin.io/) (v4.0.0) | Lightweight, pragmatic dependency injection for Kotlin |
| **Navigation** | [Navigation Compose](https://developer.android.com/guide/navigation) (v2.8.7) | Type-safe navigation using Kotlin Serialization (`@Serializable` routes) |
| **Local Persistence** | [Room Database](https://developer.android.com/training/data-storage/room) (v2.6.1) | Robust abstraction layer over SQLite with KSP and Flow support |
| **Concurrency** | [Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html) | Reactive streams (`StateFlow`, `SharedFlow`, `Channel`) for asynchronous operations |
| **Serialization** | [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) | Compiler-generated, type-safe route serialization |
| **Testing** | [JUnit 5](https://junit.org/junit5/) + [Turbine](https://github.com/cashapp/turbine) + [AssertK](https://github.com/willowtreeapps/assertk) | Unit testing with Flow assertion and Compose Robot pattern |

---

## 📂 Project Structure

```text
com.anujsingh.youcalculator
├── core
│   ├── designsystem             # Theme, Color palettes, custom buttons & UI tokens
│   ├── domain                   # Result<D, E>, Error, DataError primitives
│   └── presentation             # UiText, ObserveAsEvents lifecycle observer
├── feature
│   ├── calculator
│   │   ├── data                 # Room DB, Entity, DAO, Repository implementation
│   │   ├── di                   # Koin modules (DataModule, PresentationModule)
│   │   ├── domain               # CalculationHistory, Operator, UseCases
│   │   └── presentation         # CalculatorState, Action, Event, ViewModel, Screen, Keypad
│   └── history
│       └── presentation         # HistoryState, Action, Event, ViewModel, Screen
├── CalculatorApp.kt             # Application class initializing Koin
└── MainActivity.kt              # Single Activity entry point with NavHost
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Ladybug (2024.2.1+) or newer
- **JDK**: Version 17
- **Android SDK**: Min SDK 26 (Android 8.0+), Target SDK 35 (Android 15)

### Installation & Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/YouCalculator.git
   cd YouCalculator
   ```

2. **Open in Android Studio**:
   - Open Android Studio and select `Open...`
   - Navigate to the cloned folder and click `OK`.

3. **Build the project via CLI**:
   ```bash
   # Windows
   .\gradlew.bat assembleDebug

   # macOS / Linux
   ./gradlew assembleDebug
   ```

4. **Install onto connected device or emulator**:
   ```bash
   # Windows
   .\gradlew.bat installDebug

   # macOS / Linux
   ./gradlew installDebug
   ```

---

## 🧪 Running Tests

YouCalculator includes unit tests for business logic, math evaluation, and ViewModels using **JUnit 5**, **Turbine**, and **AssertK**:

```bash
# Run all unit tests
.\gradlew.bat testDebugUnitTest
```

---

## 📄 License

```text
MIT License

Copyright (c) 2026 Anuj Singh

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
