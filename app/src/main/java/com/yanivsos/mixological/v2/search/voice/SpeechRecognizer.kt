package com.yanivsos.mixological.v2.search.voice

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

class SpeechRecognizer {
    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    private val spokenTextMutableSharedFlow = MutableSharedFlow<String>(replay = 1)
    val spokenTextSharedFlow: SharedFlow<String> = spokenTextMutableSharedFlow

    fun onAttach(fragment: Fragment) {
        activityResultLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
            onActivityResult(activityResult.resultCode, activityResult.data)
        }
    }

    fun launch() {
        val launcher = activityResultLauncher
            ?: throw IllegalStateException("activityResultLauncher is null. did you remember to call onAttach()?")
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }.let { intent ->
            Timber.d("launching speech recognition activity")
            launcher.launch(intent)
        }

    }

    private fun onActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()?.let { spokenText ->
                    notifySpokenText(spokenText)
                } ?: run {
                Timber.e("unable to get spoken text")
            }
        } else {
            Timber.d("spoken text cancelled")
        }
    }

    private fun notifySpokenText(spokenText: String) {
        Timber.d("notifySpokenText: spoken text detected: $spokenText")
        spokenTextMutableSharedFlow.tryEmit(spokenText)
    }
}
