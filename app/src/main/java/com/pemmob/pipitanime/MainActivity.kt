package com.pemmob.pipitanime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pemmob.pipitanime.navigation.AppNavigation
import com.pemmob.pipitanime.ui.theme.PipitAnimeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PipitAnimeTheme {
                AppNavigation()
            }
        }
    }
}
