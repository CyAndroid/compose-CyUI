package com.cy.ui.widget

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay

/**
 * @author       : ChenYu
 * @date         : 2026/5/11 15:26
 * @version      : 1.0
 * @Description  : Banner
 */
@Composable
fun BannerUI(
    data: List<Any?>,
    modifier: Modifier = Modifier,
    round: Dp = 20.dp,
    isCardMode: Boolean = true,
    onItemClick: ((index: Int) -> Unit)? = null
) {

    //页面管理
    val pageStatus = rememberPagerState(0) { data.size }

    // 自动滚动的协程
    LaunchedEffect(Unit) {
        while (true) {
            // 如果没有正在进行的滚动，且当前是空闲状态，则执行自动滚动
            if (!pageStatus.isScrollInProgress) {
                // 等待设定的延时
                delay(3000)
                // 再次检查，防止用户在 delay 期间开始拖拽
                if (!pageStatus.isScrollInProgress) {
                    val nextPage = (pageStatus.currentPage + 1) % data.size
                    pageStatus.animateScrollToPage(nextPage)
                }
            } else {
                // 用户正在拖拽，等待100ms再检查，避免忙循环
                delay(100)
            }
        }
    }
    Box {

        HorizontalPager(
            pageStatus,
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(if (!isCardMode) round else 0.dp)),
            //左右露头效果
            contentPadding = PaddingValues(horizontal = if (isCardMode) 50.dp else 0.dp),
            pageSpacing = if (isCardMode) 20.dp else 0.dp,
        ) {
            //滑动时中间变大的效果
            val animatedScale by animateFloatAsState(
                targetValue = if (it == pageStatus.currentPage  || !isCardMode) 1f else 0.85f,
                label = ""
            )
            //加载网络图片
            AsyncImage(
                data[it],
                "banner $it",
                modifier
                    .scale(animatedScale)
                    .clip(RoundedCornerShape(if (isCardMode) round else 0.dp))
                    .clickable(onItemClick != null) {
                        onItemClick!!(it)
                    },
                contentScale = ContentScale.Crop,
            )
        }
        //指示器
        BannerIndicator(
            data.size,
            pageStatus.currentPage,
            Modifier
                //针对父组件是Box时，可以设置自己的位置权重
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        )
    }
}


@Composable
fun BannerIndicator(count: Int, current: Int, modifier: Modifier = Modifier) {
    Row(
        modifier
            .width((count * 16).dp)
            .height(3.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White.copy(0.2f)),
    ) {
        val offsetX by animateDpAsState(
            targetValue = (current * 16).dp,
            label = "OffsetAnimation"
        )
        Box(
            Modifier
                .width(16.dp)
                .height(3.dp)
                .offset(x = offsetX)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White)
        )
    }
}