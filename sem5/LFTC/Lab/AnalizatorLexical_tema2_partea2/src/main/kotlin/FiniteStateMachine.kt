/**
 * Class for a Finite State Machine, containing states, alphabet and transitions
 */
class FiniteStateMachine(
    val startSate: State,
    val states: List<State>,
    val alphabet: List<String>,
    val finalStates: List<State>,
    val transitions: Map<State, List<Pair<String, State>>>
) {
    /**
     * Checks if a string is accepted or not by this state machine
     */
    fun isAccepted(seq: String): Boolean {
        var currentState: State = startSate
        seq.forEach { ch ->
            val trans = transitions[currentState] ?: return false
            val pair = trans.firstOrNull { it.first == ch.toString() } ?: return false
            currentState = pair.second
        }
        return currentState in finalStates
    }

    /**
     * Calculates the longest prefix accepted by this state machine
     */
    fun longestPrefix(seq: String): String {
        var prefix = ""
        var crtPrefix = ""
        var currentState: State = startSate
        seq.forEach { ch ->
            val trans = transitions[currentState] ?: return prefix
            val pair = trans.firstOrNull { it.first == ch.toString() } ?: return prefix
            currentState = pair.second
            crtPrefix += ch.toString()
            if(currentState in finalStates)
                prefix = crtPrefix
        }
        return prefix
    }

    fun isDeterminist(): Boolean {
        transitions.values.forEach { listOfPairs ->
            listOfPairs.forEach { pair ->
                val count = listOfPairs.count { it.first == pair.first }
                if(count > 1) {
                    return false
                }
            }
        }
        return true
    }
}