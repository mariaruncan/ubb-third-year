import java.io.File
import java.io.FileNotFoundException
import java.util.*


private val firstTable = ArrayList<String>()
private val FIP = ArrayList<ArrayList<Int>>()
private val TS_ID = SortedLinkedList()
private val TS_CONST = SortedLinkedList()

/**
 * Entry point of the program
 */
fun main() {
    firstTable.add("ID")
    firstTable.add("CONST")
    println("Enter file name: ")
    val `in` = Scanner(System.`in`)
    val filename = "src/" + `in`.nextLine()
    try {
        processFile(filename)
    } catch (ex: RuntimeException) {
        ex.printStackTrace()
    } finally {
        writeFirstTable()
        writeFIP()
        writeTS()
    }
}

/**
 * Reads from file line by line and processes word by word for lexical analysis
 * @param filename name of the file to read the program from
 * @throws RuntimeException if file is not found or if an atom is invalid
 */
private fun processFile(filename: String) {
    var lineNumber = 0
    try {
        Scanner(File(filename)).use { `in` ->
            while (`in`.hasNext()) {
                lineNumber++
                val line = `in`.nextLine()
                val words =
                    line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val finalLineNumber = lineNumber
                Arrays.stream(words)
                    .forEach { word: String ->
                        processWord(
                            word,
                            finalLineNumber
                        )
                    }
            }
        }
    } catch (e: FileNotFoundException) {
        throw RuntimeException(e)
    }
}

/**
 * Splits a word in prefix, word and suffix and processes it
 * @param word the word to be split
 * @param lineNumber line number of the word
 * @throws RuntimeException if a part of the word is invalid
 */
private fun processWord(word: String, lineNumber: Int) {
    var word = word
    var prefix: String? = null
    var suffix: String? = null

    // verify if atom starts or ends with a separator and splits it in prefix, word and suffix
    for (separator in separators) {
        if (word.startsWith(separator) && prefix == null) {
            word = word.replace(separator, "")
            prefix = separator
        }
        if (word.endsWith(separator) && suffix == null) {
            word = word.replace(separator, "")
            suffix = separator
        }
    }
    if (prefix != null) {
        processAtom(prefix, lineNumber)
    }
    word = word.trim { it <= ' ' }
    if (word.isNotEmpty()) {
        processAtom(word, lineNumber)
    }
    if (suffix != null) {
        processAtom(suffix, lineNumber)
    }
}

/**
 * Verifies if a string is keyword, separator, operator, id or constant
 * @param atom the string to be verified
 * @param lineNumber the line that contains the atom
 * @throws RuntimeException if the atom in not in a category
 */
private fun processAtom(atom: String, lineNumber: Int) {
    val indexInFirstTable: Int
    var indexInTS = -1
    if (firstTable.contains(atom)) {
        indexInFirstTable = firstTable.indexOf(atom)
    } else if (atom.isKeyword() || atom.isOperator() || atom.isSeparator()) {
        firstTable.add(atom)
        indexInFirstTable = firstTable.indexOf(atom)
    } else if (atom.isConstInt() || atom.isConstFloat()) {
        TS_CONST.sortedInsert(atom)
        indexInFirstTable = firstTable.indexOf("CONST")
        indexInTS = TS_CONST.findPosition(atom)
    } else if (atom.isId()) {
        TS_ID.sortedInsert(atom)
        indexInFirstTable = firstTable.indexOf("ID")
        indexInTS = TS_ID.findPosition(atom)
    } else {
        throw RuntimeException("Something went wrong on line: $lineNumber")
    }
    val lineInFip = ArrayList<Int>()
    lineInFip.add(indexInFirstTable)
    if (indexInTS != -1) lineInFip.add(indexInTS)
    FIP.add(lineInFip)
}

/**
 * Writes the first table to console
 */
private fun writeFirstTable() {
    println("\n")
    println("------- First Table -------")
    println("Cod \tAtom lexical")
    for (i in firstTable.indices) {
        println(i.toString() + "\t\t" + firstTable[i])
    }
    println("\n\n")
}

/**
 * Writes FIP to console
 */
private fun writeFIP() {
    println("------- FIP -------")
    println("Cod atom \tPoz TS")
    for (line in FIP) {
        if (line[0] == 0 || line[0] == 1) {
            println(line[0].toString() + "\t\t\t" + line[1])
        } else {
            println(line[0])
        }
    }
    println("\n\n")
}

/**
 * Writes TS_ID and TS_CONST to console
 */
private fun writeTS() {
    println("------- TS_ID -------")
    TS_ID.printList()
    println("------- TS_CONST -------")
    TS_CONST.printList()
}