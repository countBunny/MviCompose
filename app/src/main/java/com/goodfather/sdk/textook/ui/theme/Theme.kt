package com.goodfather.sdk.textook.ui.theme

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat

@Stable
class ReadSdkColor(background: Color) {
    var background: Color by mutableStateOf(background)
        private set
}

private val LightColorPalette = ReadSdkColor(
    background = PurpleGrey80
)

private val LocalCustomColors = compositionLocalOf {
    LightColorPalette
}

object ReadSdkTheme {
    val colors: ReadSdkColor
        @Composable
        get() = LocalCustomColors.current
}

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ReadSdkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    orientation: Int = Configuration.ORIENTATION_PORTRAIT,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    val fontScale = LocalDensity.current.fontScale
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val widthPixels = displayMetrics.widthPixels

    val colors = LightColorPalette
//    val orientation = LocalContext.current.resources.configuration.orientation
    ScreenOrientationProvider(orientation = orientation) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = {
                CompositionLocalProvider(
                    LocalDensity provides Density(
                        density = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                            widthPixels / 360.0f
                        } else{
                            displayMetrics.heightPixels / 360.0f
                        },//设计稿是按照360dp的宽度设计的
                        fontScale = fontScale
                    ),
                    LocalCustomColors provides colors
                ) {
                    content()
                }
            }
        )
    }

}

@Composable
fun ScreenOrientationProvider(
    orientation: Int = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
    children: @Composable () -> Unit
) {
    val activity = (LocalContext.current as? ComponentActivity)
        ?: error("No access to activity. Use LocalActivity from androidx.activity:activity-compose")
    activity.apply {
        requestedOrientation = orientation
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O && orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }*/
    }

    children()
}