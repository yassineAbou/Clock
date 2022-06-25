package com.example.clock.alarm

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> AnimatedSwipeDismiss(
modifier: Modifier = Modifier,
item: T,
background: @Composable (isDismissed: Boolean) -> Unit,
content: @Composable (isDismissed: Boolean) -> Unit,
directions: Set<DismissDirection> = setOf(DismissDirection.EndToStart),
enter: EnterTransition = expandVertically(),
exit: ExitTransition = shrinkVertically(
animationSpec = tween(
durationMillis = 500,
)
),
onDismiss: (T) -> Unit
) {
    val dismissState = rememberDismissState()
    val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)

    SideEffect {
        if (dismissState.isDismissed(DismissDirection.EndToStart)) {
            onDismiss(item)
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = !isDismissed,
        enter = enter,
        exit = exit
    ) {
        SwipeToDismiss(
            modifier = modifier,
            state = dismissState,
            directions = directions,
            background = { background(isDismissed) },
            dismissContent = { content(isDismissed) }
        )
    }
}