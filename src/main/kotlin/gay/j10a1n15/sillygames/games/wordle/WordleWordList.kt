package gay.j10a1n15.sillygames.games.wordle

import org.apache.commons.io.IOUtils

object WordleWordList {

    private val words: MutableList<String> = mutableListOf()
    private val allowed: MutableSet<String> = mutableSetOf()

    init {
        val wordsStream = this::class.java.getResourceAsStream("/wordle/words.txt")
        this.words.addAll(IOUtils.readLines(wordsStream, Charsets.UTF_8))
        val allowedStream = this::class.java.getResourceAsStream("/wordle/allowed.txt")
        this.allowed.addAll(IOUtils.readLines(allowedStream, Charsets.UTF_8))
        this.allowed.addAll(this.words)
    }

    fun isAllowed(word: String) = this.allowed.contains(word.lowercase())

    fun getWord() = this.words.random()
}
