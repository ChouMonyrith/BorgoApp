# 🍔 Borgo Food Ordering App

Borgo is a food ordering mobile app built with **Android (Java)** on the
frontend and **Spring Boot + MySQL** on the backend.\
This guide will help you set up the Android project in **Android
Studio**.

------------------------------------------------------------------------

## 📌 Prerequisites

Before running the project, make sure you have:

-   **Android Studio** (latest version recommended)\
-   **Java 17** (or compatible JDK version)\
-   **Gradle** (handled automatically by Android Studio)\
-   A running **Borgo Backend API** (Spring Boot + MySQL)
    -   Default backend URL: `http://localhost:8081/api/`

------------------------------------------------------------------------

## 🚀 Setup Instructions

### 1. Clone the Repository

``` bash
git clone https://github.com/your-username/borgo-android.git
cd borgo-android
```

### 2. Open in Android Studio

1.  Open **Android Studio**\
2.  Select **Open an Existing Project**\
3.  Choose the `borgo-android` folder

------------------------------------------------------------------------

### 3. Configure Backend API URL

-   Open `app/src/main/java/com/example/borgo/lib/ApiClient.java` (or
    your API config file).\
-   Update the **BASE_URL** to point to your backend server:

``` java
public class ApiClient {
    public static final String BASE_URL = "http://10.0.2.2:8081/api/"; 
}
```

👉 **Note:**\
- Use `10.0.2.2` if you're running the backend on your **local machine**
(Android Emulator).\
- If you're using a physical device, replace with your **PC's local IP**
(e.g., `http://192.168.1.10:8081/api/`).

------------------------------------------------------------------------

### 4. Sync Gradle

-   In Android Studio, click **File \> Sync Project with Gradle Files**\
-   Wait for dependencies to install (Glide, Retrofit, etc.)

------------------------------------------------------------------------

### 5. Run the App

-   Select your device (Emulator or Physical Android Phone)\
-   Click ▶ **Run**\
-   The Borgo app should launch 🚀

------------------------------------------------------------------------

## ⚡ Features

-   User authentication (Login/Register)\
-   Browse food categories & menu items\
-   Search & filter menu items\
-   Add to Cart & manage cart\
-   Checkout with multiple payment methods (ABA, Cash on Delivery)\
-   Order history

------------------------------------------------------------------------

## 🖥️ Backend Setup (Spring Boot + MySQL)

-   Clone the backend repo:

    ``` bash
    git clone https://github.com/your-username/borgo-backend.git
    ```

-   Import into **IntelliJ IDEA** or **Spring Tool Suite**\

-   Update `application.properties` with your MySQL credentials\

-   Run the Spring Boot application (`mvn spring-boot:run` or IDE run
    button)\

-   The backend API will start at `http://localhost:8081/api/`

------------------------------------------------------------------------

## 📱 Notes

-   Ensure your backend is running **before launching** the Android
    app.\
-   If using a **real device**, both device and backend server must be
    on the **same Wi-Fi network**.

------------------------------------------------------------------------

✅ That's it! You now have Borgo running on Android Studio connected to
your Spring Boot + MySQL backend.
