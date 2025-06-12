package com.example.stiveworkoutapp

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "DailyGoalsPrefs"

    // Workout categories data
    private val workoutCategories = listOf(
        WorkoutCategory("UPPERBODY CALISTHENICS", "Click for Youtube Link", "https://www.youtube.com/watch?v=SEqetqEW5uU"),
        WorkoutCategory("LOWERBODY WORKOUT", "Click for Youtube Link", "https://www.youtube.com/watch?v=o756cQQZ_Kk"),
        WorkoutCategory("CARDIO", "Click for Youtube Video", "https://www.youtube.com/watch?v=kZDvg92tTMc"),
        WorkoutCategory("ABS WORKOUT", "Click for Youtube Video", "https://www.youtube.com/watch?v=Dl8N_8UtWUU"),
        WorkoutCategory("FULL BODY", "Click for Youtube Video", "https://www.youtube.com/watch?v=jKTxe236-4U"),
        WorkoutCategory("HIIT WORKOUT", "Click for Youtube Video", "https://www.youtube.com/watch?v=cbKkB3POqaY")
    )

    data class WorkoutCategory(
        val name: String,
        val description: String,
        val youtubeLink: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val bottomAppBar = findViewById<com.google.android.material.bottomappbar.BottomAppBar>(R.id.bottom_app_bar)

        setSupportActionBar(bottomAppBar)

        fab.setOnClickListener {
            val intent = Intent(this, CreatePost::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        BottomNavigationHandler(this, R.id.nav_home).setupBottomNavigation(bottomNavigationView)

        // Initialize search functionality
        val searchView = findViewById<SearchView>(R.id.searchView)
        val searchEditTextId = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val searchEditText = searchView.findViewById<EditText>(searchEditTextId)

        searchEditText.setTextColor(Color.WHITE)
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.text_secondary)) // 👈 change hint color

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterWorkouts(newText.orEmpty())
                return true
            }
        })
        val goalsClickableArea = findViewById<View>(R.id.goalsClickableArea)
        goalsClickableArea.setOnClickListener {
            val intent = Intent(this, Goals::class.java)
            startActivity(intent)
        }

        // Load initial workouts
        showAllWorkouts()
        loadTodayGoals()
    }

    private fun showAllWorkouts() {
        updateWorkoutViews(workoutCategories)
    }

    private fun filterWorkouts(query: String) {
        val filtered = workoutCategories.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
        updateWorkoutViews(filtered)
    }

    private fun updateWorkoutViews(workouts: List<WorkoutCategory>) {
        val container = findViewById<LinearLayout>(R.id.workoutCardsContainer)
        container.removeAllViews()

        workouts.forEach { workout ->
            val card = createWorkoutCard(workout)
            container.addView(card)
        }
    }

    private fun createWorkoutCard(workout: WorkoutCategory): CardView {
        return CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16.dpToPx()
            }

            setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_background))
            radius = 8.dpToPx().toFloat()
            cardElevation = 4.dpToPx().toFloat()

            setOnClickListener { openYouTubeVideo(workout.youtubeLink) }

            addView(LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16.dpToPx(), 16.dpToPx(), 16.dpToPx(), 16.dpToPx())

                addView(TextView(context).apply {
                    text = workout.name
                    setTextColor(ContextCompat.getColor(context, android.R.color.white))
                    textSize = 18f
                    setTypeface(null, Typeface.BOLD)
                })

                addView(TextView(context).apply {
                    text = workout.description
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    textSize = 16f
                    setPadding(0, 8.dpToPx(), 0, 0)
                })
            })
        }
    }

    private fun openYouTubeVideo(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage("com.google.android.youtube")
        startActivity(intent)
    }

    private fun loadTodayGoals() {
        val dateKey = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        // Get today's goals from SharedPreferences
        val steps = sharedPreferences.getString("${dateKey}_steps", "0") ?: "0"
        val sleep = sharedPreferences.getString("${dateKey}_sleep", "0") ?: "0"
        val calories = sharedPreferences.getString("${dateKey}_calories", "0") ?: "0"

        // Update the TextViews
        findViewById<TextView>(R.id.tvStepsGoal).text = steps
        findViewById<TextView>(R.id.tvSleepGoal).text = formatSleepTime(sleep)
        findViewById<TextView>(R.id.tvCaloriesGoal).text = "$calories kcal"
    }

    private fun formatSleepTime(sleepHours: String): String {
        return try {
            val hours = sleepHours.toFloat()
            val wholeHours = hours.toInt()
            val minutes = ((hours - wholeHours) * 60).toInt()
            "${wholeHours}h ${minutes}m"
        } catch (e: NumberFormatException) {
            "0h 0m"
        }
    }

    // Helper extension to convert dp to pixels
    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}