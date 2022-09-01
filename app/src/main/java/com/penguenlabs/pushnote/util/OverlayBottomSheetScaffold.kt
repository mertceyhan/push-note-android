package com.penguenlabs.pushnote.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.min

@ExperimentalMaterialApi
@Composable
fun OverlayBottomSheetScaffold(
    sheetContent: @Composable ColumnScope.() -> Unit,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    sheetPeekHeight: Dp = 0.dp,
    sheetShapes: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    maxOverlayAlpha: Float = 0.7f,
    overlayColor: Color = Color.Black,
    cancelable: Boolean = true,
    content: @Composable (PaddingValues) -> Unit,
) {
    BottomSheetScaffold(
        sheetContent = sheetContent,
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetPeekHeight,
        sheetShape = sheetShapes
    ) {
        Box {
            content(it)

            val bottomSheetState = scaffoldState.bottomSheetState
            val bottomSheetProgress = bottomSheetState.progress
            val coroutineScope = rememberCoroutineScope()

            if ((bottomSheetProgress.to == BottomSheetValue.Expanded) or (bottomSheetState.currentValue == BottomSheetValue.Expanded)) {
                val fraction = bottomSheetProgress.fraction
                val alpha = min(fraction, maxOverlayAlpha)
                val alphaDifference = maxOverlayAlpha.minus(alpha)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (bottomSheetProgress.to == BottomSheetValue.Collapsed) alphaDifference else maxOverlayAlpha)
                        .background(color = overlayColor)
                        .clickable(
                            enabled = cancelable,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (bottomSheetState.isExpanded) {
                                coroutineScope.launch {
                                    scaffoldState.bottomSheetState.collapse()
                                }
                            }
                        }
                )
            }
        }
    }
}