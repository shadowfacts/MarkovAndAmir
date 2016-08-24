package net.shadowfacts.markovandamir

import java.io.File
import java.util.*

/**
 * @author shadowfacts
 */

val chains: MutableMap<String, MarkovChain> = HashMap()
val allLines: MutableList<String> = ArrayList()

fun main(args: Array<String>) {
	println("loading scripts and creating chain")

	val scripts = File("scripts")
	scripts.listFiles().forEach {
		val text = it.readText()
		val lines = text.split("\n")
		lines.forEach {
			val bits = it.split(":")
			if (!it.startsWith("[") && !it.startsWith("(") && bits.size >= 2) {
				val line = bits.range(1, bits.size).joinToString(separator = ":").trim()
				allLines.add(line)
				val chain = getChain(bits[0])
				chain.parseReference(line)
			}
		}
	}

	val linesFile = File("alllines.txt")
	if (linesFile.exists()) linesFile.delete()
	linesFile.writeText(allLines.joinToString(separator = "\n"))

	val scanner = Scanner(System.`in`)
	forever@ while (true) {
		print("Enter command: ")
		val line = scanner.nextLine()
		val bits = line.split(" ")
		val cmd = bits[0].toLowerCase()
		when (cmd) {
			"exit", "stop" -> break@forever
			"list", "listchains" -> {
				println(chains.keys.joinToString(separator = ", "))
			}
			"gen", "generate" -> {
				if (bits.size != 2) {
					println("Invalid number of arguments")
					continue@forever
				}
				println(getChain(bits[1]).generateString())
			}
			"exists" -> {
				val line = bits.range(1, bits.size).joinToString(separator = " ")
				println(if (allLines.contains(line)) "Line exists" else "Line does not exist")
			}
		}
	}
}

fun getChain(name: String): MarkovChain {
	val cleanedName = name.toLowerCase().trim()

	if (!chains.containsKey(cleanedName)) {
		chains[cleanedName] = MarkovChain()
	}
	return chains[cleanedName]!!
}

fun <T> List<T>.range(from: Int, to: Int): List<T> {
	val list: MutableList<T> = ArrayList()

	for (i in from.until(to)) {
		list.add(this[i])
	}

	return list
}