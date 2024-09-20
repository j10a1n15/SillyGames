package gay.j10a1n15.sillygames.games.wordle

import gay.j10a1n15.sillygames.utils.SillyUtils.getKeyCodeName
import gay.j10a1n15.sillygames.utils.SillyUtils.replaceAt
import gg.essential.elementa.utils.invisible
import gg.essential.universal.UKeyboard
import java.awt.Color

class WordleState(private val wordIndexInput: Int? = null) {

    private val letterKeys = setOf(
        UKeyboard.KEY_A, UKeyboard.KEY_B, UKeyboard.KEY_C, UKeyboard.KEY_D, UKeyboard.KEY_E, UKeyboard.KEY_F,
        UKeyboard.KEY_G, UKeyboard.KEY_H, UKeyboard.KEY_I, UKeyboard.KEY_J, UKeyboard.KEY_K, UKeyboard.KEY_L,
        UKeyboard.KEY_M, UKeyboard.KEY_N, UKeyboard.KEY_O, UKeyboard.KEY_P, UKeyboard.KEY_Q, UKeyboard.KEY_R,
        UKeyboard.KEY_S, UKeyboard.KEY_T, UKeyboard.KEY_U, UKeyboard.KEY_V, UKeyboard.KEY_W, UKeyboard.KEY_X,
        UKeyboard.KEY_Y, UKeyboard.KEY_Z,
    )

    var word = wordIndexInput?.let { WordleWordList.getWord(it) } ?: WordleWordList.getWord()
    var wordIndex = wordIndexInput ?: WordleWordList.getIndex(word)
    val guesses = Array(6) { "" }
    val colors = Array(6) { Array(5) { Color.WHITE.invisible() } }
    val letters = mutableMapOf<Char, Boolean?>()
    var tries = 0
    var reset = false

    fun reset(wordIndex: Int? = null) {
        word = wordIndex?.let { WordleWordList.getWord(it) } ?: WordleWordList.getWord()
        this.wordIndex = wordIndex ?: WordleWordList.getIndex(word)
        guesses.fill("")
        colors.forEach { it.fill(Color.WHITE.invisible()) }
        letters.clear()
        tries = 0
        reset = false
    }

    fun enterChar(char: Char) {
        if (char in 'a'..'z' && guesses[tries].length < 6) {
            guesses[tries] = guesses[tries] + char
        }
    }

    fun keyPress(key: Int) {
        if (tries >= 6 || reset) return
        when {
            key == UKeyboard.KEY_BACKSPACE -> {
                guesses[tries] = guesses[tries].dropLast(1)
            }

            key in letterKeys && guesses[tries].length < 5 -> {
                enterChar(key.getKeyCodeName()?.lowercase()?.first() ?: return)
            }

            key == UKeyboard.KEY_BACKSLASH -> {
                println("The word is $word")
            }
        }
    }

    fun guess(): String? {
        if (tries >= 6 || reset) return null
        if (!isAllowed()) return "Invalid word"

        val guess = guesses[tries]
        var answer = word
        var input = guess.lowercase()

        // Check for correct letters in correct positions
        for (i in 0 until 5) {
            if (answer[i] == input[i]) {
                colors[tries][i] = WordlePalette.GREEN
                answer = answer.replaceAt(i, ' ')
                input = input.replaceAt(i, ' ')
                letters[guess[i]] = true
            }
        }

        // Check for correct letters in wrong positions
        for (i in 0 until 5) {
            if (input[i] != guess[i]) continue
            if (answer.contains(input[i])) {
                colors[tries][i] = WordlePalette.YELLOW
                answer = answer.replaceFirst(input[i], ' ')
                input = input.replaceAt(i, ' ')
                if (letters[guess[i]] == null) {
                    letters[guess[i]] = false
                }
            } else {
                letters.putIfAbsent(guess[i], null)
                colors[tries][i] = WordlePalette.GRAY
            }
        }

        tries++
        if (guess.lowercase() == word) {
            reset = true
            return "You won!\nWord Index: $wordIndex"
        } else if (tries >= 6) {
            reset = true
            return "You lost! The word was '$word'\nWord Index: $wordIndex"
        }
        return null
    }

    private fun isAllowed() = WordleWordList.isAllowed(guesses[tries])

    fun getKeyboardColor(letter: Char): Color {
        if (!letters.containsKey(letter.lowercaseChar())) return WordlePalette.LIGHT_GRAY
        return when (letters[letter.lowercaseChar()]) {
            true -> WordlePalette.GREEN
            false -> WordlePalette.YELLOW
            else -> WordlePalette.GRAY
        }
    }
}
