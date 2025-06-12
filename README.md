# Strive Workout App (Prototype)
[![Ask DeepWiki](https://devin.ai/assets/askdeepwiki.png)](https://deepwiki.com/joaquinalmora/strive-app)

**Strive** is a **prototype Android application** designed to help users track their fitness goals, share their progress, and connect with friends in a supportive community. It combines personal goal management with social networking features, encouraging users to stay motivated and active.

---

## 📎 Quick Links

- **🧩 Figma Design:** [Strive UI Design on Figma](https://www.figma.com/design/wFugJGhi50llF3GCcyN0QN/strive-design?node-id=0-1&t=sq1zaeKJLqJSEOs6-1)
- **📹 Walkthrough of Designed Tasks:** [YouTube – Task Walkthrough](https://youtu.be/RvhzrlbUNYE)
- **📱 Demo Video:** [YouTube – App Demo](https://www.youtube.com/watch?v=Wld4pcqKDyM&ab_channel=SamiJaffri)

---

## 🚀 Features

### User Profile & Customization

- **Account Management:** Create and edit user profiles.
- **Avatar Selection:** Choose from default avatars or upload your own.
- **Stats Overview:** View level, streak, and friend count.
- **Editable Personal Info:** Update weight, height, address, etc.

### Goal Tracking & Progress

- **Daily Goals:** Track steps (goal: 20,000), sleep (8 hours), and calories (2,000).
- **Manual Input:** Log progress manually.
- **Progress Charts:** Daily and weekly visual summaries.
- **Leveling System:** Progress by consistently hitting goals.

### Social Feed & Interaction

- **Post Creation:** Share achievements or inspiration.
- **Feed View:** Toggle between “For You” and “Following.”
- **Like & Comment:** Engage with others’ posts.
- **Comment Threads:** Persistent, viewable discussions.

### Friends System

- **Friend Management:** Add and remove friends.
- **Friend Stats:** View friends’ step/sleep/calorie data.
- **Poke Feature:** Send friendly nudges.
- **Top Friends Panel:** Quick-access display of top friends.

### Workout Discovery (Home Screen)

- **Workout Categories:** Includes calisthenics, cardio, abs, etc.
- **YouTube Integration:** Direct links to video workouts.
- **Search Bar:** Look up workouts by keyword.
- **Today's Snapshot:** Summary of today’s fitness progress.

### Settings & Privacy

- **Central Settings Menu:** All configuration in one place.
- **Privacy Options:** Control who sees your posts.
- **Placeholder Settings:** Language, Notifications, Storage (planned).

---

## 🛠️ Tech Stack & Dependencies

- **Languages:** Java, Kotlin
- **Platform:** Android
- **UI Frameworks:** Android XML, Material Components
- **Build Tool:** Gradle

### Data Persistence

- `SharedPreferences`
- `Gson` for serializing/deserializing comment data

### Key Libraries

- `androidx.appcompat`
- `androidx.constraintlayout`
- `androidx.core-ktx`
- `com.google.android.material:material`
- `de.hdodenhof:circleimageview`
- `com.google.code.gson:gson`

---

## 📁 Project Structure
The project follows a standard Android application structure:

    341-Code--main/app/src/main/java/com/example/stiveworkoutapp/: Contains all the Java and Kotlin source code, including:
        Activities: Screen controllers like MainActivity, AccountActivity, GoalsActivity, PostsActivity, SettingsActivity, etc.
        Adapters: PostsAdapter, CommentsAdapter, FriendsAdapter for populating RecyclerViews.
        Models: Data classes like Post, Comment, Friend, UserProfile.
        Handlers/Utils: BottomNavigationHandler, UserLevel, CommentStorage.
    341-Code--main/app/src/main/res/: Contains all application resources:
        layout/: XML files defining the UI for each screen and list item.
        drawable/: Image assets, custom vector drawables, and shape definitions.
        values/: String constants, color definitions, styles, themes, and dimension arrays.
        menu/: XML defining the structure of the bottom navigation menu.
        mipmap/: Launcher icons for different screen densities.
        font/: Custom font resources.
        xml/: Configuration files like file_paths for FileProvider.

Setup and Build

    Clone the repository:

    git clone https://github.com/joaquinalmora/strive-app.git

    Navigate to the project directory:

    cd strive-app/341-Code--main

    Open in Android Studio:
        Launch Android Studio.
        Select "Open an Existing Project" and navigate to the 341-Code--main directory.
    Gradle Sync:
        Allow Android Studio to sync the Gradle files and download all necessary dependencies.
    Build the Project:
        From the Android Studio menu, select Build > Make Project.
        Alternatively, run the following command in the terminal from the 341-Code--main directory:

        ./gradlew build

    Run the Application:
        Select a target emulator or connect a physical Android device.
        Click the "Run 'app'" button in Android Studio.

Note: The application is configured with minSdk = 35 and compileSdk = 35, which targets Android 15 (Vanilla Ice Cream). This may require a very recent Android Studio version and an emulator/device running Android 15 or higher.
