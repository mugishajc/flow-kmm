package com.mugishajc.flowkmm.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mugishajc.flowkmm.android.ui.navigation.AppNavigation
import com.mugishajc.flowkmm.android.ui.theme.FlowKmmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowKmmTheme {
                AppNavigation()
            }
        }
    }
}
