package com.codek.pensamentos.presentation.ui.composables

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codek.pensamentos.R

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun SkeletonCard(modifier: Modifier = Modifier) {

    val infiniteTransition = rememberInfiniteTransition()

    val animationValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.8f),
            Color.LightGray.copy(alpha = 0.1f),
            Color.LightGray.copy(alpha = 0.1f),
            Color.LightGray.copy(alpha = 0.8f),
        ),
        start = Offset.Zero,
        end = Offset(animationValue, animationValue)
    )

    Box(
        Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clip(CutCornerShape(0.dp, 0.dp, 30.dp, 0.dp))
            .background(gradientBrush)
    ) {
        Row(
            Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                Modifier
                    .weight(1f)
                    .background(Color.Transparent)
            ) {
                Column {
                    Box(
                        Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_pensamentos),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 10.dp),
                            colorFilter = ColorFilter.tint(Color.Gray)
                        )
                    }
                }
            }
        }
        Box(
            Modifier
                .size(30.dp)
                .clip(CutCornerShape(0.dp, 0.dp, 30.dp, 0.dp))
                .background(Color.Gray)
                .align(Alignment.BottomEnd)
        )
    }
}

@Preview
@Composable
fun SkeletonCardPreview() {
    SkeletonCard()
}