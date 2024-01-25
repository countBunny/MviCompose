package com.goodfather.sdk.textook.ui.pages

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.goodfather.sdk.textook.data.intent.MainIntent
import com.goodfather.sdk.textook.data.state.MainState
import com.goodfather.sdk.textook.network.NetworkUtils
import com.goodfather.sdk.textook.ui.theme.ReadSdkTheme
import com.goodfather.sdk.textook.ui.viewmodel.MainViewModel
import com.goodfather.sdk.textook.ui.viewmodel.ViewModelFactory
import com.goodfather.sdk.textook.utils.L
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SplashPage(
    modifier: Modifier = Modifier,
    onBack:()->Unit
    ) {
    val mainViewModel: MainViewModel = viewModel(factory = ViewModelFactory(NetworkUtils.sdkService))
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            L.i("SplashPage OpenBook invoked")
            mainViewModel.mainIntentChannel.send(MainIntent.OpenBook(24, "666"))
        }
    }
    val uiState = mainViewModel.state.collectAsState()
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .background(color = ReadSdkTheme.colors.background)
    ) {
        val (progressRef) = createRefs()
        if (uiState.value == MainState.Loading) {
            CircularProgressIndicator(
                strokeCap = StrokeCap.Round,
                modifier = Modifier
                    .constrainAs(progressRef) {
                        centerTo(parent)
                    }
            )
        }

    }
    SplashTopAppBar {
        onBack()
    }

}

@Composable
private fun SplashTopAppBar(
    backIcon: ImageVector = Icons.Default.ArrowBack,
    onBack:()-> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)
    ) {
        Button(onClick = {
            onBack()
        }) {
            Image(imageVector = backIcon, contentDescription = "go back",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}