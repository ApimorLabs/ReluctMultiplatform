package com.apimorlabs.reluct.compose.ui.components.animations

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntOffset

@Stable
fun slideInVerticallyReversed(
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )
): EnterTransition = slideInVertically(
    animationSpec = animationSpec,
    initialOffsetY = { fullHeight -> fullHeight }
)

@Stable
fun slideOutVerticallyReversed(
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )
): ExitTransition = slideOutVertically(
    animationSpec = animationSpec,
    targetOffsetY = { fullHeight -> fullHeight }
)

@Stable
fun slideInVerticallyFadeReversed(
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    ),
    initialScale: Float = 1f
): EnterTransition = slideInVertically(
    animationSpec = animationSpec,
    initialOffsetY = { fullHeight -> (initialScale * fullHeight).toInt() }
) + fadeIn()

@Stable
fun slideInHorizontallyFadeReversed(
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    ),
    initialScale: Float = 1f
): EnterTransition = slideInHorizontally(
    animationSpec = animationSpec,
    initialOffsetX = { fullHeight -> -(initialScale * fullHeight).toInt() }
) + fadeIn()

@Stable
fun slideOutVerticallyFadeReversed(
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    ),
    targetScale: Float = 1f
): ExitTransition = slideOutVertically(
    animationSpec = animationSpec,
    targetOffsetY = { fullHeight -> (targetScale * fullHeight).toInt() }
) + fadeOut()

@Stable
fun slideOutHorizontallyFadeReversed(
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    ),
    targetScale: Float = 1f
): ExitTransition = slideOutHorizontally(
    animationSpec = animationSpec,
    targetOffsetX = { fullHeight -> -(targetScale * fullHeight).toInt() }
) + fadeOut()
