package net.shadowfacts.markovandamir

import java.util.*

/**
 * @author shadowfacts
 */
class MarkovChain {

	private val chain = Hashtable<String, Vector<String>>()
	private val rand = Random()

	constructor() {
		chain.put("_start", Vector())
		chain.put("_end", Vector())
	}

	fun parseReference(line: String) {
		val words = line.split(" ")

		words.forEachIndexed { i, word ->
			if (i == words.size - 1) {
				val endWords = chain["_end"]!!
				endWords.add(word)
			} else if (i == 0) {
				val startWords = chain["_start"]!!
				startWords.add(word)
				var suffix = chain[word]
				if (suffix == null) {
					suffix = Vector()
					suffix.add(words[i + 1])
					chain[word] = suffix
				}
			} else {
				var suffix = chain[word]
				if (suffix == null) {
					suffix = Vector()
					suffix.add(words[i + 1])
					chain[word] = suffix
				} else {
					suffix.add(words[i + 1])
					chain[word] = suffix
				}
			}
		}
	}

	fun generateString(): String {
		val phrase = Vector<String>()

		val startWords = chain["_start"]!!

		var nextWord = startWords[rand.nextInt(startWords.size)]
		phrase.add(nextWord)

		while (phrase.size < 20) {
			val wordSelection = chain[nextWord]
			if (wordSelection != null) {
				nextWord = wordSelection[rand.nextInt(wordSelection.size)]
				phrase.addElement(nextWord)
//				if (chain["_end"]!!.contains(nextWord)) {
//					break
//				}
			} else {
				break
			}
		}

		return phrase.joinToString(separator = " ")
	}

}