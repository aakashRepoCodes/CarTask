package com.automotive.cars.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automotive.cars.R

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MyCarHeader(
    logoRes: Int,
    carName: String,
    carDetails: String,
    carImageRes: Int,
    onManageCarClicked: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.55f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_landing_page_bg),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 148.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = carName,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = carDetails,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onManageCarClicked.invoke() },
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.manage_car),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp)
                )
            }

        }

        Image(
            painter = painterResource(id = carImageRes),
            contentDescription = "Car Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(width = 340.dp, height = 220.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF23262B)
@Composable
fun MyCarHeaderPreview() {
    MyCarHeader(
        logoRes = R.drawable.app_logo,
        carName = "Audi A4",
        carDetails = "2007 - Gasoline",
        carImageRes = R.drawable.car_logo,
        onManageCarClicked = {}
    )
}

