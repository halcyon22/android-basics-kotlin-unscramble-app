package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val _score: MutableLiveData<Int> = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount: MutableLiveData<Int> = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord: MutableLiveData<String> = MutableLiveData("")
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    private lateinit var currentPlainWord: String
    private val usedWords: MutableSet<String> = mutableSetOf()

    fun isSubmissionCorrect(submission: String): Boolean {
        return submission == currentPlainWord
    }

    fun countCorrectSubmission() {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }

    fun tryNextWord(): Boolean {
        if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            nextWord()
            return true
        }
        return false
    }

    private fun nextWord() {
        currentPlainWord = allWordsList.random()
        Log.i("GameFragment", "word='${currentPlainWord}'")

        val tempWord = currentPlainWord.toCharArray()
        tempWord.shuffle()
        _currentScrambledWord.value = String(tempWord)
        if (_currentScrambledWord.value == currentPlainWord || usedWords.contains(currentPlainWord)) {
            Log.i("GameFragment", "skipping duplicate")
            return nextWord()
        }

        usedWords.add(currentPlainWord)
        if (allWordsList.size == usedWords.size) {
            usedWords.clear()
        }
        _currentWordCount.value = _currentWordCount.value?.inc()
    }

    fun restart() {
        Log.i("GameFragment", "restarting")
        _score.value = 0
        _currentWordCount.value = 0
        usedWords.clear()
    }

}