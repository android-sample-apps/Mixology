package com.yanivsos.mixological.ui.utils

import com.google.android.material.tabs.TabLayout

class SimpleOnTabSelectedListener(
    private val onReSelected: ((TabLayout.Tab) -> Unit)? = null,
    private val onSelected: ((TabLayout.Tab) -> Unit)? = null,
    private val onUnSelected: ((TabLayout.Tab) -> Unit)? = null
): TabLayout.OnTabSelectedListener {

    override fun onTabReselected(tab: TabLayout.Tab) {
        onReSelected?.invoke(tab)
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        onUnSelected?.invoke(tab)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        onSelected?.invoke(tab)
    }
}