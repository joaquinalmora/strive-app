# Strive Workout App (Prototype)

**Strive** is a **prototype Android application** designed to help users track their fitness goals, share their progress, and connect with friends in a supportive community. It combines personal goal management with social networking features, encouraging users to stay motivated and active.

---

## 📎 Quick Links

- **Figma Design:** [Strive UI Design on Figma](https://www.figma.com/design/wFugJGhi50llF3GCcyN0QN/strive-design?node-id=0-1&t=sq1zaeKJLqJSEOs6-1)
- **Walkthrough of Designed Tasks:** [YouTube – Task Walkthrough](https://youtu.be/RvhzrlbUNYE)
- **Demo Video:** [YouTube – App Demo](https://www.youtube.com/watch?v=Wld4pcqKDyM&ab_channel=SamiJaffri)

---
## Project Documents

Here are the key documents outlining the planning, research, design, and evaluation process behind the Strive Workout App:

### 1. **Usability and User Experience Plan**  
*Outlines the app’s core usability and UX goals, user profiles, and primary personas to guide design decisions in early development.*  
[📎 View File](strive-reports/1.%20Usability%20and%20User%20Experience%20Plan.pdf)


### 2. **User Research & Requirements Gathering**  
*Details primary and secondary user profiles, structured interview insights, and functional, data, and usability requirements based on user feedback.*  
[📎 View File](strive-reports/2.%20User%20Research%20and%20Requirements%20Gathering.pdf)


### 3. **Prototype Evaluation & Iteration Report**  
*Summarizes major tasks, wireframe designs, user feedback, and UI improvements based on two rounds of usability testing and critiques.*  
[📎 View File](strive-reports/3.%20Prototype%20Evaluation%20and%20Iteration%20Report.pdf)


### 4. **Design Principles & Heuristic Evaluation Report**  
*Presents the final system overview, demonstrates how key HCI design principles were implemented, and documents heuristic evaluation results with video demos.*  
[📎 View File](strive-reports/4.%20Design%20Principles%20and%20Heuristic%20Evaluation%20Report.pdf)

---

## Features

### User Profile & Customization
*   **Account Management:** Users can manage their profiles, including personal information like name, email, and bio.
*   **Avatar Selection:** Personalize profiles by choosing from a set of default avatars or uploading a custom image.
*   **Stats Overview:** The profile screen displays key stats such as user level, number of friends, and current activity streak.
*   **Personal Information Editing:** A dedicated section in settings allows users to update details like phone number, date of birth, gender, address, weight, and height.

### Goal Tracking & Progress
*   **Daily Goals:** Set and track daily targets for steps (goal: 20,000), sleep duration (goal: 8 hours), and calories burned (goal: 2,000).
*   **Progress Input:** Manually input daily achievements for steps, sleep, and calories.
*   **Daily & Weekly Visualization:** View current day's progress and a weekly summary chart showing consistency for each metric.
*   **Leveling System:** Users level up by consistently meeting their daily goals. Completing all three main goals for a day contributes to leveling up.

### Social Feed & Interaction
*   **Create Posts:** Share workout achievements, fitness milestones, or motivational content by creating posts with an image and a descriptive caption.
*   **Post Feed:** Browse a dynamic feed with "For You" (discoverable content) and "Following" (posts from users you follow) sections.
*   **Engagement:** Interact with posts by liking them and adding comments.
*   **Comment System:** View and participate in discussions on posts. Comments are saved per post and can be viewed by anyone accessing the post.

### Friends System
*   **Connect with Friends:** Add and manage a list of friends within the app.
*   **View Friend Profiles:** Access profiles of friends to see their stats (steps, sleep, calories) and their shared posts.
*   **"Poke" Feature:** Send a quick "poke" notification to friends.
*   **Top Friends Display:** The main account screen highlights top friends for quick access.

### Workout Discovery (Home Screen)
*   **Curated Workout Categories:** The main screen features various workout categories like "Upperbody Calisthenics," "Lowerbody Workout," "Cardio," "Abs Workout," etc.
*   **YouTube Integration:** Each workout category links to a relevant YouTube video providing workout guidance.
*   **Workout Search:** Users can search for specific types of workouts.
*   **Today's Goals Snapshot:** The home screen also provides a quick view of the user's progress towards today's goals.

### Application Settings
*   **Centralized Settings:** A comprehensive settings menu allows users to configure various aspects of the app.
*   **Privacy Controls:** Manage post sharing preferences.
*   **(Future/Placeholder Settings):** Sections for Language, Notifications, and Storage & Data are present, indicating potential future enhancements.

## Tech Stack & Dependencies
*   **Languages:** Java, Kotlin
*   **Platform:** Android
*   **Build Tool:** Gradle
*   **UI Framework:** Android XML, Material Components (BottomNavigationView, CardView, FloatingActionButton, Switch, RecyclerView)
*   **Data Persistence:**
    *   `SharedPreferences`: Used for storing user preferences, profile information, avatar paths, post URIs, daily goal progress, user levels, and privacy settings.
    *   `Gson`: For serializing and deserializing `Comment` objects stored in SharedPreferences.
*   **Key Libraries:**
    *   `androidx.appcompat`, `androidx.constraintlayout`, `androidx.core-ktx`, `androidx.activity`
    *   `com.google.android.material:material` (for Material Design components)
    *   `de.hdodenhof:circleimageview` (for circular image views, though primarily `ImageView` is used with custom shaping for avatars in layouts)
    *   `com.google.code.gson:gson`

## Project Structure
The project follows a standard Android application structure:
*   `341-Code--main/app/src/main/java/com/example/stiveworkoutapp/`: Contains all the Java and Kotlin source code, including:
    *   **Activities:** Screen controllers like `MainActivity`, `AccountActivity`, `GoalsActivity`, `PostsActivity`, `SettingsActivity`, etc.
    *   **Adapters:** `PostsAdapter`, `CommentsAdapter`, `FriendsAdapter` for populating RecyclerViews.
    *   **Models:** Data classes like `Post`, `Comment`, `Friend`, `UserProfile`.
    *   **Handlers/Utils:** `BottomNavigationHandler`, `UserLevel`, `CommentStorage`.
*   `341-Code--main/app/src/main/res/`: Contains all application resources:
    *   `layout/`: XML files defining the UI for each screen and list item.
    *   `drawable/`: Image assets, custom vector drawables, and shape definitions.
    *   `values/`: String constants, color definitions, styles, themes, and dimension arrays.
    *   `menu/`: XML defining the structure of the bottom navigation menu.
    *   `mipmap/`: Launcher icons for different screen densities.
    *   `font/`: Custom font resources.
    *   `xml/`: Configuration files like `file_paths` for `FileProvider`.

## Setup and Build
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/joaquinalmora/strive-app.git
    ```
2.  **Navigate to the project directory:**
    ```bash
    cd strive-app/341-Code--main
    ```
3.  **Open in Android Studio:**
    *   Launch Android Studio.
    *   Select "Open an Existing Project" and navigate to the `341-Code--main` directory.
4.  **Gradle Sync:**
    *   Allow Android Studio to sync the Gradle files and download all necessary dependencies.
5.  **Build the Project:**
    *   From the Android Studio menu, select `Build > Make Project`.
    *   Alternatively, run the following command in the terminal from the `341-Code--main` directory:
        ```bash
        ./gradlew build
        ```
6.  **Run the Application:**
    *   Select a target emulator or connect a physical Android device.
    *   Click the "Run 'app'" button in Android Studio.

**Note:** The application is configured with `minSdk = 35` and `compileSdk = 35`, which targets Android 15 (Vanilla Ice Cream). This may require a very recent Android Studio version and an emulator/device running Android 15 or higher.
