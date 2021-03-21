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
        if (_currentWordCount.value!! < MAX_WORD_COUNT) {
            nextWord()
            return true
        }
        return false
    }

    private fun nextWord() {
        currentPlainWord = getUnusedWord()
        _currentScrambledWord.value = scrambleCurrentWord()
        _currentWordCount.value = _currentWordCount.value?.inc()
    }

    private fun getUnusedWord(): String {
        var word = allWordsList.random()
        while (usedWords.contains(word)) {
            word = allWordsList.random()
        }
        Log.i("GameFragment", "word='${word}'")
        updateUsedWords(word)
        return word
    }

    private fun updateUsedWords(word: String) {
        usedWords.add(word)
        if (allWordsList.size == usedWords.size) {
            usedWords.clear()
        }
    }

    private fun scrambleCurrentWord(): String {
        val scrambled = currentPlainWord.toCharArray()
        scrambled.shuffle()
        while (String(scrambled) == currentPlainWord) {
            scrambled.shuffle()
        }
        return String(scrambled)
    }

    fun restart() {
        Log.i("GameFragment", "restarting")
        _score.value = 0
        _currentWordCount.value = 0
    }

}