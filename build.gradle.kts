// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("compose_version", "1.7.2")
        set("compose_ui_version", "1.4.3")
        set("room_version", "2.5.2")
        set("datastore_version", "1.0.0")
    }
}

plugins {
    val agp_version = "8.0.2"
    id("com.android.application") version agp_version apply false
    id("com.android.library") version agp_version apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id ("com.google.dagger.hilt.android") version "2.44" apply false
}