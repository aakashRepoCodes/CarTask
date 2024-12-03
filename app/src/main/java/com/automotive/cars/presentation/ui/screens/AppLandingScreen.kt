package com.automotive.cars.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automotive.cars.R

@Composable
fun DefaultLandingScreen(
    onAddClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color =  MaterialTheme.colorScheme.background)
    ) {
        val backgroundHeight = maxHeight * 0.60f

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(backgroundHeight)
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_landing_page_bg), // Replace with your resource
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background.copy(alpha =  0.6f),
                            MaterialTheme.colorScheme.surface.copy(alpha =  0.2f)
                        ),
                        startY = 0f,
                        endY = maxHeight.value
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(top = 24.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.add_car_btn),
                contentDescription = "Add new car Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(top = 24.dp)
                    .clickable { onAddClick.invoke() }
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultLandingScreenPreview() {
    DefaultLandingScreen(
        onAddClick = {}
    )
}
