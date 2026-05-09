package com.cy.ui.page.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * @author       : ChenYu
 * @date         : 2026/5/10/周日 0:59
 * @version      : 1.0
 * @Description  : 描述
 */
@Serializable
data object TextPageKey: NavKey

@Composable
fun TextPageUI(modifier: Modifier = Modifier) {
    Box(Modifier.background(Color.Gray)) {

    }
}

