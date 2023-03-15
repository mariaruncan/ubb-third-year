class UI() {
    /**
     * Print function for first menu
     */
    private fun printFirstMenu() {
        println("1. Read from file")
        println("2. Read from console")
        println("Press x to exit")
        println("--------------------")
        print(">> ")
    }

    /**
     * Print function for second menu
     */
    private fun printSecondMenu() {
        println("1. States")
        println("2. Alphabet")
        println("3. Transitions")
        println("4. Final states")
        println("5. Is sequence accepted?")
        println("6. Longest prefix for sequence")
        println("Press x to exit")
        println("--------------------")
        print(">> ")
    }

    /**
     * Entry point of the second menu
     * fsm - FinalStateMachine
     */
    private fun enterFirstMenu() {
        var cmd = ""
        while (true) {
            printFirstMenu()
            cmd = readLine()!!.toString()
            if (cmd == "x" || cmd == "X") {
                println("Byee :(")
                break
            }
            when (cmd) {
                "1" -> enterSecondMenu(Utils.readFromFile())
                "2" -> enterSecondMenu(Utils.readFromConsole())
                else -> println("Invalid command!\n")
            }
        }
    }

    /**
     * Entry point for first menu
     */
    private fun enterSecondMenu(fsm: FiniteStateMachine) {
        var cmd = ""
        while (true) {
            printSecondMenu()
            cmd = readLine()!!.toString()
            if (cmd == "x" || cmd == "X") {

                break
            }
            when (cmd) {
                "1" -> {
                    print("States: ")
                    println(
                        fsm.states
                            .map { it.id }
                            .reduce { acc, s -> "$acc, $s" } + "\n"
                    )
                }

                "2" -> {
                    print("Alphabet: ")
                    println(
                        fsm.alphabet
                            .reduce { acc, s -> "$acc, $s" } + "\n"
                    )
                }

                "3" -> {
                    println("Transitions:")
                    fsm.transitions.forEach { (key, value) ->
                        print(key.id + ":")
                        value.forEach { pair ->
                            println("\t${pair.first} ${pair.second.id}")
                        }
                    }
                }

                "4" -> {
                    print("Final states: ")
                    println(
                        fsm.finalStates
                            .map { it.id }
                            .reduce { acc, s -> "$acc, $s" } + "\n"
                    )
                }

                "5" -> {
                    if (!fsm.isDeterminist()) {
                        print("Nedeterminist\n")
                    } else {
                        print("Enter sequence: ")
                        val sequence = readLine()!!.toString()
                        if (fsm.isAccepted(sequence)) {
                            println("Sequence is accepted!")
                        } else {
                            println("Sequence is declined!")
                        }
                    }
                }

                "6" -> {
                    if (!fsm.isDeterminist()) {
                        print("Nedeterminist\n")
                    } else {
                        print("Enter sequence: ")
                        val sequence = readLine()!!.toString()
                        val prefix = fsm.longestPrefix(sequence)
                        println("The longest prefix is: $prefix.")
                    }
                }

                else -> println("Invalid command!\n")
            }
        }
    }

    /**
     * Entry point of the program
     */
    fun run() {
        enterFirstMenu()
    }
}

fun main(args: Array<String>) {
    val ui: UI = UI()
    ui.run()
}
