package com.coffeebliss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.coffeebliss.ui.CoffeeBlissNavGraph
import com.coffeebliss.ui.theme.CoffeeBlissTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeBlissTheme {
                CoffeeBlissNavGraph()
            }
        }
    }
}
