import java.io.File
import kotlin.RuntimeException

class Utils {
    companion object {
        const val ID_AF = "src/id_af.txt"
        const val CONST_INT_AF = "src/const_int_af.txt"
        const val CONST_FLOAT_AF = "src/const_float_af.txt"

        /**
         * Reads a finite state machine from file
         * The file must respect a template
         */
        fun readFromFile(filename: String): FiniteStateMachine {
            var startState: State? = null
            val states: MutableList<State> = mutableListOf()
            val finalStates: MutableList<State> = mutableListOf()
            val alphabet: MutableList<String> = mutableListOf()
            val transitions: MutableMap<State, MutableList<Pair<String, State>>> = mutableMapOf()

            File(filename).forEachLine { line ->
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
    }
}