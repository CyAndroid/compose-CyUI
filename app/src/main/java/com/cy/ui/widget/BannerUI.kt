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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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

    val scope = rememberCoroutineScope()
    var autoScrollJob by remember { mutableStateOf<Job?>(null) }
    var isProgrammaticScroll by remember { mutableStateOf(false) }

    // 获取当前组件的生命周期
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle

    // 启动或停止自动滚动的方法
    fun startAutoScroll() {
        // 如果已经有Job，先取消再创建新的（保证每次重新开始计时）
        autoScrollJob?.cancel()
        // 如果 itemCount <= 1，则不应有滚动逻辑（此处简单禁用）
        if (autoScrollDelay <= 0 || data.size <= 1) return
        autoScrollJob = scope.launch {
            // 等待 delay 时间
            delay(autoScrollDelay)
            // 只有在页面可见（至少是 RESUMED）且没有用户滚动时，才执行翻页
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) &&
                !pagerState.isScrollInProgress &&
                !isProgrammaticScroll
            ) {
                val nextPage = (pagerState.currentPage + 1) % data.size
                isProgrammaticScroll = true
                try {
                    pagerState.animateScrollToPage(nextPage)
                } finally {
                    isProgrammaticScroll = false
                    // 动画结束后继续下一轮滚动（但需要先检查页面是否还在前台）
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        startAutoScroll()
                    }
                }
            } else {
                // 如果条件不满足（例如页面已不在前台），则重新等待一小段时间后重试
                // 实际上如果生命周期不在 RESUMED，我们会在生命周期的 PAUSE 中取消 Job，
                // 这里仅作为保护，避免死循环
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    startAutoScroll()
                }
            }
        }
    }

    fun stopAutoScroll() {
        autoScrollJob?.cancel()
        autoScrollJob = null
    }

    // 监听生命周期变化
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    // 页面可见，启动自动滚动（重置计时器）
                    startAutoScroll()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    // 页面不可见，停止自动滚动
                    stopAutoScroll()
                }

                else -> {}
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
            stopAutoScroll()
        }
    }

    // 监听用户滚动状态（重置倒计时）
    LaunchedEffect(pagerState) {
        if (autoScrollDelay <= 0 || data.size <= 1) return@LaunchedEffect
        snapshotFlow { pagerState.isScrollInProgress }
            .collect { isScrolling ->
                // 只处理用户主动滚动（非自动动画）
                if (isScrolling && !isProgrammaticScroll) {
                    // 用户开始拖拽 → 取消当前定时
                    autoScrollJob?.cancel()
                } else if (!isScrolling && !isProgrammaticScroll) {
                    // 用户结束拖拽（手指离开且惯性滚动停止）→ 重新开始倒计时（仅当页面可见时）
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        startAutoScroll()
                    }
                }
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
        if (data.size > 1){
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