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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author       : ChenYu
 * @date         : 2026/5/11 15:26
 * @version      : 1.0
 * @Description  : Banner轮播图
 * @param data:数据源，类型同Coil的AsyncImage
 * @param modifier:属性控制
 * @param round:圆角
 * @param isCardMode:卡片模式
 * @param autoScrollDelay:自动轮播，小于等于0的时候不轮播
 * @param onItemClick(index: Int):每个轮播项的点击事件
 */
@Composable
fun BannerUI(
    data: List<Any?>,
    modifier: Modifier = Modifier,
    round: Dp = 15.dp,
    isCardMode: Boolean = false,
    autoScrollDelay: Long = 3000L,
    onItemClick: ((index: Int) -> Unit)? = null
) {

    //页面管理
    val pagerState = rememberPagerState(0) { data.size }

    // 自动滚动的协程
    val scope = rememberCoroutineScope()
    var autoScrollJob by remember { mutableStateOf<Job?>(null) }

    // 标记当前是否正在执行自动触发的滚动动画（用于排除自身的干扰）
    var isProgrammaticScroll by remember { mutableStateOf(false) }

    // 重置自动滚动：取消旧的 Job，启动新的延时任务
    fun resetAutoScroll() {
        autoScrollJob?.cancel()
        if (autoScrollDelay <= 0) return
        autoScrollJob = scope.launch {
            delay(autoScrollDelay)
            // 延时结束后，如果用户没有正在滚动，也没有自动滚动正在进行，则触发翻页
            if (!pagerState.isScrollInProgress && !isProgrammaticScroll) {
                val nextPage = (pagerState.currentPage + 1) % data.size
                isProgrammaticScroll = true
                try {
                    pagerState.animateScrollToPage(nextPage)
                } finally {
                    isProgrammaticScroll = false
                    // 翻页动画结束后，重新开始倒计时（实现无限循环）
                    resetAutoScroll()
                }
            } else {
                // 如果正处于滚动状态（用户或自动），则等待100ms再重试
                // 这种场景很少发生，但为了健壮性
                resetAutoScroll()
            }
        }
    }

    // 监听用户交互的开始和结束
    LaunchedEffect(pagerState) {
        if (autoScrollDelay <= 0) return@LaunchedEffect
        snapshotFlow { pagerState.isScrollInProgress }
            .collect { isScrolling ->
                if (isScrolling && !isProgrammaticScroll) {
                    // 用户开始拖拽（非自动触发的滚动）→ 取消自动滚动计时
                    autoScrollJob?.cancel()
                } else if (!isScrolling && !isProgrammaticScroll) {
                    // 用户结束拖拽（手指离开且惯性滚动也结束了）→ 重置定时器，重新等待3秒
                    resetAutoScroll()
                }
            }
    }

    // 首次加载时启动自动滚动
    LaunchedEffect(Unit) {
        if (autoScrollDelay <= 0) return@LaunchedEffect
        resetAutoScroll()
    }

    // 组件销毁时清理协程
    DisposableEffect(Unit) {
        onDispose {
            autoScrollJob?.cancel()
        }
    }

    //正式开始构建界面
    Box {
        HorizontalPager(
            pagerState,
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(if (!isCardMode) round else 0.dp)),
            //左右露头效果
            contentPadding = PaddingValues(horizontal = if (isCardMode) 50.dp else 0.dp),
            pageSpacing = if (isCardMode) 20.dp else 0.dp,
        ) {
            //滑动时中间变大的效果
            val animatedScale by animateFloatAsState(
                targetValue = if (it == pagerState.currentPage  || !isCardMode) 1f else 0.85f,
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
            pagerState.currentPage,
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