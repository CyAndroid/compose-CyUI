package com.cy.ui.page.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.cy.ui.backStack
import kotlinx.serialization.Serializable

/**
 * @author       : ChenYu
 * @date         : 2026/5/10/周日 0:37
 * @version      : 1.0
 * @Description  : 描述
 */

@Serializable
data object HomePageKey : NavKey

@Composable
fun HomePageUI(modifier: Modifier = Modifier) {

    val viewModel: HomeViewModel = viewModel()

    LazyVerticalGrid(
        GridCells.Fixed(3),
        modifier
            .safeContentPadding()
            .fillMaxSize()
    ) {
        items(viewModel.items) {
            Button({
                backStack.add(it.second)
            }) {
                Text(it.first)
            }
        }
    }
}