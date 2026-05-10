package com.cy.ui.page.edittext

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImage
import com.cy.ui.R
import kotlinx.serialization.Serializable

/**
 * @author       : ChenYu
 * @date         : 2026/5/10/周日 17:24
 * @version      : 1.0
 * @Description  : 输入框
 */
@Serializable
data object EditTextPageKey : NavKey

@Composable
fun EditTextPageUI(modifier: Modifier = Modifier) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .safeContentPadding()
            .wrapContentSize(Alignment.Center)
    ) {
        var inputValue by remember { mutableStateOf("") }

        BasicTextField(
            inputValue, {
                inputValue = it
            }, Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.White)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(R.drawable.ic_launcher_background,"")
                if (inputValue.isEmpty()) Text("请输入内容（等于hint）")
                //实际的输入框实现，必须调用一次
                it()
            }
        }
    }
}