package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    var score = 0
        private set
    var currentWordCount = 0
        private set
    var currentScrambledWord = ""
        private set
    private lateinit var currentPlainWord: String

    private val usedWords: MutableSet<String> = mutableSetOf()

    fun isSubmissionCorrect(submission: String): Boolean {
        return submission == currentPlainWord
    }

    fun countCorrectSubmission() {
        score += SCORE_INCREASE
    }

    fun tryNextWord(): Boolean {
        if (currentWordCount < MAX_NO_OF_WORDS) {
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
        currentScrambledWord = String(tempWord)
        if (currentScrambledWord == currentPlainWord || usedWords.contains(currentPlainWord)) {
           return nextWord()
        }

        usedWords.add(currentPlainWord)
        if (allWordsList.size == usedWords.size) {
            usedWords.clear()
        }
        currentWordCount++
    }

    fun restart() {
        Log.i("GameFragment", "restarting")
        score = 0
        currentWordCount = 0
        usedWords.clear()
    }

}