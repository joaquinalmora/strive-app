// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
configurations.all {
    resolutionStrategy {
        resolutionStrategy.force("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    }
}