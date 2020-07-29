/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.samples.crane.ui

import androidx.compose.animation.asDisposableClock
import androidx.compose.animation.core.AnimationClockObservable
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.Box
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.DpConstraints
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.preferredSizeIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeableState
import androidx.compose.material.fractionalThresholds
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.state
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.onPositioned
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

// BackdropFrontLayer is missing a proper nested scrolling behavior as right now,
// instead of scrolling only the parts that are visible on the screen, it scrolls
// all its content. Waiting for a NestedScrollController that makes this easier:
// https://issuetracker.google.com/162408885
@Composable
fun BackdropFrontLayer(
    modifier: Modifier = Modifier,
    backdropState: SwipeableBackdropState = rememberSwipeableBackdropState(),
    staticChildren: @Composable (Modifier) -> Unit,
    backdropChildren: @Composable (Modifier) -> Unit
) {
    var backgroundChildrenSize by state(structuralEqualityPolicy()) { IntSize(0, 0) }

    Box(modifier.fillMaxSize()) {
        WithConstraints {
            val fullHeight = constraints.maxHeight.toFloat()
            val anchors = getAnchors(backgroundChildrenSize, fullHeight)

            Stack(
                Modifier.swipeable(
                    state = backdropState,
                    anchors = anchors,
                    thresholds = fractionalThresholds(0.5f),
                    orientation = Orientation.Vertical,
                    minValue = VerticalExplorePadding,
                    maxValue = fullHeight,
                    enabled = true
                )
            ) {
                staticChildren(
                    Modifier.onPositioned { coordinates ->
                        if (backgroundChildrenSize.height == 0) {
                            backdropState.value = FullScreenState.Collapsed
                        }
                        backgroundChildrenSize = coordinates.size
                    }
                )

                val shadowColor = MaterialTheme.colors.surface.copy(alpha = 0.8f)
                val revealValue = backgroundChildrenSize.height / 2
                if (backdropState.offset.value < revealValue) {
                    Canvas(Modifier.fillMaxSize()) {
                        drawRect(size = size, color = shadowColor)
                    }
                }

                val yOffset = with(DensityAmbient.current) {
                    backdropState.offset.value.toDp()
                }

                backdropChildren(
                    Modifier.offset(0.dp, yOffset)
                        .preferredSizeIn(currentConstraints(constraints))
                )
            }
        }
    }
}

enum class FullScreenState {
    Minimised,
    Collapsed,
    Expanded,
}

class SwipeableBackdropState(
    initialValue: FullScreenState = FullScreenState.Minimised,
    clock: AnimationClockObservable,
    confirmStateChange: (FullScreenState) -> Boolean = { true }
) : SwipeableState<FullScreenState>(initialValue, clock, confirmStateChange, AnimationSpec)

@Composable
fun rememberSwipeableBackdropState(
    initialValue: FullScreenState = FullScreenState.Minimised,
    confirmStateChange: (FullScreenState) -> Boolean = { true }
): SwipeableBackdropState {
    val clock = AnimationClockAmbient.current.asDisposableClock()
    return remember(clock, confirmStateChange) {
        SwipeableBackdropState(initialValue, clock, confirmStateChange)
    }
}

@Composable
private fun currentConstraints(pxConstraints: Constraints): DpConstraints {
    return with(DensityAmbient.current) {
        DpConstraints(pxConstraints)
    }
}

private fun getAnchors(
    searchChildrenSize: IntSize,
    fullHeight: Float
): Map<Float, FullScreenState> {
    val mediumValue = searchChildrenSize.height + 50.dp.value
    val maxValue = fullHeight - AnchorBottomOffset
    return mapOf(
        VerticalExplorePadding to FullScreenState.Expanded,
        mediumValue to FullScreenState.Collapsed,
        maxValue to FullScreenState.Minimised
    )
}

private const val AnchorBottomOffset = 130f
private const val VerticalExplorePadding = 0f
private const val ExploreStiffness = 1000f
private val AnimationSpec = SpringSpec<Float>(stiffness = ExploreStiffness)
