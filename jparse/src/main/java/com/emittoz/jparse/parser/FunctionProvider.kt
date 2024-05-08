package com.emittoz.jparse.parser

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.asTypeName

class FunctionProvider(
    private val kClass: KSClassDeclaration,
    private val functionUtils: FunctionUtils = FunctionUtils(),
    private val packageName: String = kClass.packageName.asString(),
    private val fileName: String = kClass.simpleName.asString()
) {

    fun fromJsonWithStringArgs(): FunSpec {
        return FunSpec.builder(name = FUNC_FROM_JSON)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(ARG_JSON_STRING, String::class.asTypeName().copy(nullable = true))
            .returns(ClassName(packageName = packageName, fileName))
            .addStatement(
                functionUtils.ifStatement(
                    input = ARG_JSON_STRING,
                    compareTo = NULL_VALUE
                ).prependIndent()
            )
            .addStatement(
                functionUtils.throwException(
                    exceptionName = IllegalArgumentException::class.simpleName,
                    msg = NULL_JSON_EXCEPTION
                ).prependIndent()
            )
            .addStatement(CLOSE_CURLY_BRACE.prependIndent())
            .addStatement(
                functionUtils.returnStatement(
                    returnFunction = true,
                    funcName = FUNC_FROM_JSON,
                    argument = ARG_JSON_STRING,
                    toString = false
                )
            )
            .build()
    }

    companion object {
        const val FUNC_FROM_JSON = "fromJson"
        const val ARG_JSON_STRING = "json"
        const val NULL_VALUE = "null"
        const val CLOSE_CURLY_BRACE = "}"
        const val NULL_JSON_EXCEPTION = "\"JSON content should not be null.\""
    }
}