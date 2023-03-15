/**
 * Class for defining type for a line from a file
 */
enum class LineType {
    Q, SIGMA, S, F, TRANSITION;

    companion object {
        /**
         * Converts a string to a LineType
         */
        fun fromString(str: String): LineType =
            when (str) {
                "Q" -> Q
                "SIGMA" -> SIGMA
                "S" -> S
                "F" -> F
                else -> TRANSITION
            }
    }
}