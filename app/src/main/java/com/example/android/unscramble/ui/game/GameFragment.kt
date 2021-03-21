/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding
    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.gameViewModel = viewModel
        binding.maxWordCount = MAX_WORD_COUNT

        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }

        nextWord()
    }

    private fun onSubmitWord() {
        val submission = binding.submissionEditText.text.toString()
        val isCorrect = viewModel.isSubmissionCorrect(submission)

        Log.i("GameFragment", "'$submission' is correct? $isCorrect")

        if (isCorrect) {
            viewModel.countCorrectSubmission()
            setErrorTextField(false)
            nextWord()
        } else {
            setErrorTextField(true)
        }
    }

    private fun onSkipWord() {
        setErrorTextField(false)
        nextWord()
    }

    private fun nextWord() {
        if (viewModel.tryNextWord()) {
            binding.submissionEditText.text?.clear()
        } else {
            showFinalScoreDialog()
        }
    }

    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(this.requireContext())
            .setTitle(R.string.congratulations)
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(R.string.exit) { _, _ -> exitGame() }
            .setPositiveButton(R.string.play_again) { _, _ -> restartGame() }
            .show()
    }

    private fun restartGame() {
        setErrorTextField(false)
        viewModel.restart()
        nextWord()
    }

    private fun exitGame() {
        activity?.finish()
    }

    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.submissionInputLayout.isErrorEnabled = true
            binding.submissionInputLayout.error = getString(R.string.try_again)
        } else {
            binding.submissionInputLayout.isErrorEnabled = false
            binding.submissionEditText.text = null
        }
    }

}
