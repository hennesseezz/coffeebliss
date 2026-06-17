package com.coffeebliss.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Coffee Bliss Brand Colors
val CoffeeBrown = Color(0xFF3E2723)         // Deep espresso brown
val CoffeeMedium = Color(0xFF6D4C41)        // Medium roast
val CoffeeLight = Color(0xFF8D6E63)         // Light brown
val CoffeeAccent = Color(0xFFD4A853)        // Golden caramel
val CoffeeAccentLight = Color(0xFFF5E6C8)   // Cream
val CoffeeSurface = Color(0xFFFDF6EE)       // Off-white cream
val CoffeeGreen = Color(0xFF2E7D32)         // Coffee shop green
val CoffeeGreenLight = Color(0xFF66BB6A)
val CoffeeRed = Color(0xFFC62828)
val TextPrimary = Color(0xFF1A1A1A)
val TextSecondary = Color(0xFF5A5A5A)
val CardBackground = Color(0xFFFFFFFF)

private val CoffeeColorScheme = lightColorScheme(
    primary = CoffeeBrown,
    onPrimary = Color.White,
    primaryContainer = CoffeeAccentLight,
    onPrimaryContainer = CoffeeBrown,
    secondary = CoffeeAccent,
    onSecondary = CoffeeBrown,
    secondaryContainer = Color(0xFFFFF3E0),
    onSecondaryContainer = Color(0xFF3E2000),
    tertiary = CoffeeGreen,
    onTertiary = Color.White,
    background = CoffeeSurface,
    onBackground = TextPrimary,
    surface = CardBackground,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF3EAE1),
    onSurfaceVariant = TextSecondary,
    error = CoffeeRed,
    outline = Color(0xFFBCAAA4)
)

@Composable
fun CoffeeBlissTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CoffeeColorScheme,
        typography = Typography(),
        content = content
    )
}
