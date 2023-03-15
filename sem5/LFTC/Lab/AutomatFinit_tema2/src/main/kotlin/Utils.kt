import java.io.File
import kotlin.RuntimeException

class Utils {
    companion object {
        private const val FILE_NAME = "src/main/input2.txt"

        /**
         * Reads a finite state machine from file
         * The file must respect a template
         */
        fun readFromFile(): FiniteStateMachine {
            var startState: State? = null
            val states: MutableList<State> = mutableListOf()
            val finalStates: MutableList<State> = mutableListOf()
            val alphabet: MutableList<String> = mutableListOf()
            val transitions: MutableMap<State, MutableList<Pair<String, State>>> = mutableMapOf()

            File(FILE_NAME).forEachLine { line ->
                val words: List<String> = line.split(" ")

                when (LineType.fromString(words[0])) {
                    LineType.Q -> {
                        for (i in 1 until words.size) {
                            states.add(State(words[i]))
                        }
                    }

                    LineType.SIGMA -> {
                        for (i in 1 until words.size) {
                            alphabet.add(words[i])
                        }
                    }

                    LineType.S -> {
                        startState = State(words[1])
                    }

                    LineType.F -> {
                        for (i in 1 until words.size) {
                            val finaleState = states.firstOrNull { it.id == words[i] }
                                ?: throw RuntimeException("Invalid final state!")
                            finalStates.add(finaleState)
                        }
                    }

                    LineType.TRANSITION -> {
                        val firstState = states.firstOrNull { it.id == words[0] }
                            ?: throw RuntimeException("Invalid start state!")
                        val transitionsAux = mutableListOf<String>()
                        words[1].split("").forEach { if(it != "") transitionsAux.add(it)}
                        val secondState = states.firstOrNull { it.id == words[2] }
                            ?: throw RuntimeException("Invalid end state!")

                        transitionsAux.forEach {input ->
                            alphabet.firstOrNull { it == input } ?: throw RuntimeException("Invalid input state!")
                            if (transitions.containsKey(firstState)) {
                                val list: MutableList<Pair<String, State>> = transitions.getValue(firstState)
                                list.add(Pair(input, secondState))
                            } else {
                                val list: MutableList<Pair<String, State>> = mutableListOf(Pair(input, secondState))
                                transitions[firstState] = list
                            }
                        }
                    }
                }
            }

            return FiniteStateMachine(
                startSate = startState ?: throw RuntimeException("Invalid start state!"),
                states = states,
                alphabet = alphabet,
                finalStates = finalStates,
                transitions = transitions
            )
        }

        /**
         * Reads a finite state machine components from console
         * Inputs must be written in a given format
         */
        fun readFromConsole(): FiniteStateMachine {
            var startState: State? = null
            val states: MutableList<State> = mutableListOf()
            val finalStates: MutableList<State> = mutableListOf()
            val alphabet: MutableList<String> = mutableListOf()
            val transitions: MutableMap<State, MutableList<Pair<String, State>>> = mutableMapOf()

            println("Enter states (x to end): ")
            while (true) {
                val id = readLine()!!.toString()
                if (id == "x") break
                if (id.trim() != "") {
                    states.add(State(id))
                }
            }

            println("Enter inputs (x to end): ")
            while (true) {
                val input = readLine()!!.toString()
                if (input == "x") break
                if (input.trim() != "") {
                    alphabet.add(input)
                }
            }

            println("Enter start state: ")
            while (startState == null) {
                val startId = readLine()!!.toString()
                startState = states.firstOrNull { it.id == startId }
                if (startState == null) println("Invalid start state!")
            }

            println("Enter final states (x to end): ")
            while (true) {
                val id = readLine()!!.toString()
                if (id == "x") break
                val state = states.firstOrNull { it.id == id }
                if (state == null) {
                    println("Invalid final state!")
                } else {
                    finalStates.add(state)
                }
            }

            println("Enter transitions separated by space (x to end): ")
            while (true) {
                val line = readLine()!!.toString()
                if (line == "x") break
                val words = line.split(" ").onEach { it.trim() }

                try {
                    val firstState =
                        states.firstOrNull { it.id == words[0] } ?: throw RuntimeException("Invalid first state!")
                    val input = alphabet.firstOrNull { it == words[1] } ?: throw RuntimeException("Invalid input!")
                    val secondState =
                        states.firstOrNull { it.id == words[2] } ?: throw RuntimeException("Invalid second state!")

                    if (transitions.containsKey(firstState)) {
                        val list: MutableList<Pair<String, State>> = transitions.getValue(firstState)
                        list.add(Pair(input, secondState))
                    } else {
                        val list: MutableList<Pair<String, State>> = mutableListOf(Pair(input, secondState))
                        transitions[firstState] = list
                    }
                } catch (ex: RuntimeException) {
                    ex.printStackTrace()
                    continue
                }
            }

            return FiniteStateMachine(
                startSate = startState,
                states = states,
                alphabet = alphabet,
                finalStates = finalStates,
                transitions = transitions
            )
        }
    }
}