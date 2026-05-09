package com.cy.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.cy.ui.page.home.HomePageKey
import com.cy.ui.page.home.HomePageUI
import com.cy.ui.page.text.TextPageKey
import com.cy.ui.page.text.TextPageUI

/**
 * @author       : ChenYu
 * @date         : 2026/5/10/周日 0:22
 * @version      : 1.0
 * @Description  : 主导航页
 */

val backStack by lazy { NavBackStack<NavKey>(HomePageKey) }

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {

    NavDisplay(
        backStack,
        entryProvider = entryProvider {
            entry<HomePageKey> {
                HomePageUI()
            }
            entry<TextPageKey> {
                TextPageUI()
            }
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            // 入场动画，跳转，平移 + 淡入
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)) togetherWith slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popTransitionSpec = {
            // 出场动画，返回  平移
            // Slide in from left when navigating back
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        predictivePopTransitionSpec = {
            // 出场动画，返回  平移
            // Slide in from left when navigating back
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))  togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    )
}