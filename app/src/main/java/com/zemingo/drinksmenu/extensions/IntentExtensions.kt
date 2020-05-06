package com.zemingo.drinksmenu.extensions

import android.app.SearchManager
import android.content.Intent

fun webSearchIntent(query: String): Intent {
    return Intent(Intent.ACTION_WEB_SEARCH).apply {
        putExtra(SearchManager.QUERY, query)
    }
}