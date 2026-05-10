package com.cy.ui.page.pagebreak

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

/**
 * @author       : ChenYu
 * @date         : 2026/5/10/周日 18:35
 * @version      : 1.0
 * @Description  : 描述
 */
@Serializable
data object PageBreakPageKey : NavKey

@Composable
fun PageBreakPageUI(modifier: Modifier = Modifier) {

    val datas = remember {
        mutableStateListOf("香蕉", "苹果", "芒果", "萝卜", "咖啡")
    }

    val state = rememberPagerState(
        pageCount = { datas.size },
        initialPage = 0,//初始页码
    )
    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFC5C5C5))
            .safeContentPadding()
    ) {
        PrimaryScrollableTabRow(
            selectedTabIndex = state.currentPage,//展示的页码，和Pager的保持一致
        ) {
            datas.forEachIndexed { index, data ->
                Box(
                    Modifier
                        .height(40.dp)
                        .width(100.dp)
                        .clickable {
                            scope.launch {
                                state.animateScrollToPage(index, 0f)//Tab被点击后让Pager中内容动画形式滑动到目标页
                            }
                        }, contentAlignment = Alignment.Center
                ) {
                    Text(text = data)
                }
            }
        }
        HorizontalPager(
            state = state,//Pager当前所在页数
            modifier = Modifier.height(300.dp)
        ) { pagePosition ->
            Log.e("tag", "加载页码$pagePosition")
            val color = (0..255)
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(color.random(), color.random(), color.random())),
                contentAlignment = Alignment.Center
            ) {
                Text(text = datas[pagePosition])
            }
        }
    }
}