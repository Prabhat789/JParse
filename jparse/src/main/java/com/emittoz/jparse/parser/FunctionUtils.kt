package com.emittoz.jparse.parser

class FunctionUtils {

    fun ifStatement(input: String, condition: String = DOUBLE_EQUAL, compareTo: String): String {
        return with(StringBuilder()) {
            append(IF_CONDITION)
            append(OPEN_BRACE)
            append(input)
            append(SPACE)
            append(condition)
            append(SPACE)
            append(compareTo)
            append(CLOSE_BRACE)
            append(SPACE)
            append(OPEN_CURLY_BRACE)
        }.toString()
    }

    fun throwException(exceptionName: String?, msg:String): String {
        return "$DEFAULT_INDENTATION$THROW $exceptionName$OPEN_BRACE$msg$CLOSE_BRACE"
    }

    fun returnStatement(
        returnFunction:Boolean,
        funcName: String = EMPTY,
        argument: String = EMPTY,
        toString: Boolean = false
    ): String {
        val returnData = "$DEFAULT_INDENTATION$RETURN ${
            when(returnFunction) {
                true -> "$funcName$OPEN_BRACE$argument$CLOSE_BRACE"
                false -> EMPTY
            }
        }"
        return if (toString) "$returnData$DOT$TO_STRING" else returnData
    }

    companion object {
        const val INDENTATION_8 = "        "
        const val INDENTATION_12 = "            "
        const val DEFAULT_INDENTATION = "    "
        const val DOT = "."
        const val OPEN_BRACE = "("
        const val CLOSE_BRACE = ")"
        const val OPEN_CURLY_BRACE = "{"
        const val CLOSE_CURLY_BRACE = "}"
        const val SPACE = " "
        const val DOUBLE_EQUAL = "=="
        const val IF_CONDITION = "if"
        const val NULL_VALUE = "null"
        const val THROW = "throw"
        const val RETURN = "return"
        const val JSON_OBJECT = "jsonObject"
        const val EMPTY = ""
        const val TO_STRING = "toString()"
    }
}