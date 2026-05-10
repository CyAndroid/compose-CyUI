package com.cy.ui.page.home

import androidx.lifecycle.ViewModel
import com.cy.ui.page.edittext.EditTextPageKey
import com.cy.ui.page.pagebreak.PageBreakPageKey
import com.cy.ui.page.text.TextPageKey

/**
 * @author       : ChenYu
 * @date         : 2026/5/10/周日 0:48
 * @version      : 1.0
 * @Description  : 描述
 */
class HomeViewModel: ViewModel() {
    val items = listOf(
        "文本" to TextPageKey,
        "按钮" to TextPageKey,
        "输入框" to EditTextPageKey,
        "分页和TabView" to PageBreakPageKey,
        "触摸冲突和Banner" to TextPageKey,
        "下拉刷新和上滑加载" to TextPageKey,
        "列表" to TextPageKey,
        "流式和网格" to TextPageKey,
    )
}