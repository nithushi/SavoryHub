# 🍲 SavoryHub

**SavoryHub** is a modern Android application designed for food ordering and delivery, featuring a rich user experience, real-time order tracking, and seamless payment integration. This project is perfect for learning about Android app development, Firebase integration, and advanced UI/UX patterns.

---

## 🚀 Features

- **User Authentication**: Sign up, sign in, and email verification.
- **Food Catalog**: Browse, search, and view details of food items.
- **Cart & Checkout**: Add items to cart, manage quantities, and checkout.
- **Order Tracking**: Real-time order status and history.
- **Admin Dashboard**: Manage products, users, and analyze sales.
- **Payment Integration**: Secure payments via PayHere SDK.
- **Location Services**: Google Maps integration for delivery tracking.
- **Data Persistence**: Uses Firebase Firestore for real-time data and TinyDB for local storage.
- **Rich UI**: Material Design, charts (MPAndroidChart), and image loading (Glide).

---

## 🛠️ Tech Stack

- **Language**: Java 11
- **Framework**: Android SDK (minSdk 24, targetSdk 35)
- **UI**: Material Components, ConstraintLayout, ViewBinding
- **Architecture**: Modular package structure (UI, Model, Helper, Adapters, Navigation)
- **Backend**: Firebase Firestore
- **Authentication**: Firebase Auth
- **Payments**: PayHere Android SDK
- **Maps & Location**: Google Maps, Play Services Location
- **Image Loading**: Glide
- **Charts**: MPAndroidChart
- **Utilities**: Gson, TinyDB, JavaMail

---

## 📁 Project Structure

```
SavoryHub/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/lk/javainstitute/savoryhub/
│   │       │   ├── ui/         # Activities (Home, Cart, Profile, etc.)
│   │       │   ├── model/      # Data models (Foods, Orders, etc.)
│   │       │   ├── helper/     # Utilities (TinyDB, Cart Management)
│   │       │   ├── adapters/   # RecyclerView adapters
│   │       │   └── navigation/ # Fragments for navigation
│   │       ├── res/            # Layouts, drawables, values
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── ...
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🧑‍💻 Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/SavoryHub.git
   cd SavoryHub
   ```

2. **Open in Android Studio**  
   - File > Open > Select the project directory.

3. **Configure Firebase**  
   - Add your `google-services.json` to `app/`.

4. **Build & Run**  
   - Connect your device or start an emulator.
   - Click "Run" in Android Studio.

---

## 🔒 Permissions

The app requests the following permissions:
- Internet & Network State
- Location (for delivery tracking)
- Storage (for image uploads)
- Phone (for call functionality)

---

## 📦 Dependencies

- `com.google.firebase:firebase-firestore`
- `com.github.bumptech.glide:glide`
- `com.github.PayHereDevs:payhere-android-sdk`
- `com.github.PhilJay:MPAndroidChart`
- `com.google.android.gms:play-services-maps`
- `com.google.android.gms:play-services-location`
- `com.sun.mail:android-mail`
- `com.google.code.gson:gson`
- ...and more (see `build.gradle.kts`)

---

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

---

## ✨ Credits

Developed by [M. Nithushi Shavindi].  
Special thanks to open-source libraries and the Android developer community!

---
