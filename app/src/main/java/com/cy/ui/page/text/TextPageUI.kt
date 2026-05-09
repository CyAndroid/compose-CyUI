package com.cy.ui.page.text

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

/**
 * @author       : ChenYu
 * @date         : 2026/5/10/周日 0:59
 * @version      : 1.0
 * @Description  : 文本控件 展示页面
 */
@Serializable
data object TextPageKey: NavKey

@Preview
@Composable
fun TextPageUI(modifier: Modifier = Modifier) {
    Column(Modifier
        .background(Color.Gray)
        //等价于match_parent ，默认是wrap_content
        .fillMaxSize()
        //留出安全距离，包括系统的状态栏和导航栏
        .safeContentPadding()) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        Text(
            //内容
            text = "这是一段文字",
            //字体大小
            fontSize = 18.sp,
            //字体颜色
            color = Color.White,
            //字体权重（加粗）
            fontWeight = FontWeight.Bold,
            //最大行数
            maxLines = 1,
            //溢出选项（显示，截断，前中后省略号）
//            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                //剪切 圆角RoundedCornerShape，自动满圆CircleShape，剪角CutCornerShape，自定义剪切继承Shape
                .clip(RoundedCornerShape(10.dp))
                //点击事件
                .clickable {
                    coroutineScope.launch {
                        Toast.makeText(context, "弹个土司！！！", Toast.LENGTH_SHORT).show()
                    }
                }
                //背景
                .background(Color.Cyan)
                //尺寸，size height width
                .width(70.dp)
                //跑马灯，溢出选项必须是默认的
                .basicMarquee(Int.MAX_VALUE)
                //等价于 gravity 内部位置
                .wrapContentSize(Alignment.Center)
                //因为链式操作，所以没有margin,都能用padding实现
                .padding(8.dp, 5.dp, 8.dp, 5.dp)
        )
        Spacer(Modifier.background(Color.Yellow).size(100.dp,50.dp))
    }
}

