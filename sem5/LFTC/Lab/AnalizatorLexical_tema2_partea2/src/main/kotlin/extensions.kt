import Utils.Companion.readFromFile

val separators: List<String> = listOf(";", "(", ")", "{", "}", ",")
private val operators: List<String> = listOf("!=", ">>", "<<", ">", "<", "=", "-", "*", "+")
private val keywords: List<String> = listOf(
    "#include",
    "<iostream>",
    "using",
    "namespace",
    "std",
    "int",
    "main",
    "cin",
    "while",
    "cout",
    "float",
    "if",
    "else"
)

private val idFSM = readFromFile(Utils.ID_AF)
private val constIntFSM = readFromFile(Utils.CONST_INT_AF)
private val constFloatFSM = readFromFile(Utils.CONST_FLOAT_AF)

fun String.isOperator(): Boolean = operators.contains(this)

fun String.isSeparator(): Boolean = separators.contains(this)

fun String.isKeyword(): Boolean = keywords.contains(this)

fun String.isConstInt(): Boolean = constIntFSM.isAccepted(this)

fun String.isConstFloat(): Boolean = constFloatFSM.isAccepted(this)

fun String.isId(): Boolean = idFSM.isAccepted(this)