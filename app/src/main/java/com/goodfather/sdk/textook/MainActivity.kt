package com.goodfather.sdk.textook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.goodfather.sdk.textook.data.state.MainState
import com.goodfather.sdk.textook.navigation.NavGraph
import com.goodfather.sdk.textook.network.NetworkUtils
import com.goodfather.sdk.textook.ui.theme.ReadSdkTheme
import com.goodfather.sdk.textook.ui.viewmodel.MainViewModel
import com.goodfather.sdk.textook.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GpapaFacade.init(application)

        mainViewModel = ViewModelProvider(this, ViewModelFactory(NetworkUtils.sdkService))[MainViewModel::class.java]
        setContent {
            ReadSdkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val mainViewModel: MainViewModel = viewModel(factory = ViewModelFactory(NetworkUtils.sdkService))
                    val navController = rememberNavController()
                    NavGraph(navigationController = navController)
                }
            }
        }

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is MainState.BookDetailResp -> {

                    }
                    is MainState.Error -> {

                    }
                    MainState.Idle -> {

                    }
                    MainState.Loading -> {

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ReadSdkTheme {
        Greeting("Android")
    }
}