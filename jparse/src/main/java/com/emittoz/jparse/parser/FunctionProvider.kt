package com.emittoz.jparse.parser

import com.emittoz.jparse.annotations.Ignore
import com.emittoz.jparse.parser.FunctionUtils.Companion.CLOSE_BRACE
import com.emittoz.jparse.parser.FunctionUtils.Companion.DEFAULT_INDENTATION
import com.emittoz.jparse.parser.FunctionUtils.Companion.EMPTY
import com.emittoz.jparse.parser.FunctionUtils.Companion.INDENTATION_12
import com.emittoz.jparse.parser.FunctionUtils.Companion.INDENTATION_8
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.asTypeName
import org.json.JSONObject

class FunctionProvider(
    private val kClass: KSClassDeclaration,
    private val functionUtils: FunctionUtils = FunctionUtils(),
    private val packageName: String = kClass.packageName.asString(),
    private val fileName: String = kClass.simpleName.asString(),
    private val parseJson: ParseJson = ParseJson(),
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

    fun fromJsonWithJsonArgs(): FunSpec {
        val properties: List<KSPropertyDeclaration> = kClass.getDeclaredProperties().toList()
        return FunSpec.builder(name = FUNC_FROM_JSON)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(ARG_JSON_OBJECT, JSONObject::class.asTypeName().copy(nullable = true))
            .returns(ClassName(packageName = packageName, fileName))
            .addStatement(
                functionUtils.ifStatement(
                    input = ARG_JSON_OBJECT,
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
            .addStatement("${DEFAULT_INDENTATION}with(${ARG_JSON_OBJECT}) {").apply {
                properties.forEachIndexed { _, property ->
                    val propertyName = property.simpleName.asString()
                    val returnType = property.type.resolve().declaration.toTypeName()
                    if (!hasAnnotation(property)) {
                        addStatement(
                            "${INDENTATION_8}val $property = " +
                                    parseJson.getDataFromJson(
                                        jsonPropertyName = propertyName,
                                        returnType = returnType
                                    )
                        )
                    }
                }
            }
            .addStatement("${INDENTATION_8}return %T(", ClassName(
                packageName = packageName, fileName)
            ).apply {
                properties.forEachIndexed { index, property ->
                    if (!hasAnnotation(property)) {
                        addStatement(
                            "${INDENTATION_12}$property = $property${
                                when {
                                    index < properties.size - 1 -> ","
                                    else -> EMPTY
                                }
                            }"
                        )
                    }
                }
            }
            .addStatement(CLOSE_BRACE.prependIndent(INDENTATION_8))
            .addStatement(CLOSE_CURLY_BRACE.prependIndent(DEFAULT_INDENTATION))
            .build()
    }

    fun toJsonStringFromModel(): FunSpec {
        return FunSpec.builder(name = FUNC_TO_JSON_STRING)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(ARG_OBJECT_MODEL, ClassName(packageName = packageName, fileName).copy(nullable = true))
            .returns(String::class.asTypeName())
            .addStatement(
                functionUtils.ifStatement(
                    input = ARG_OBJECT_MODEL,
                    compareTo = NULL_VALUE
                ).prependIndent()
            )
            .addStatement(
                functionUtils.throwException(
                    exceptionName = IllegalArgumentException::class.simpleName,
                    msg = NULL_MODEL_EXCEPTION
                ).prependIndent()
            )
            .addStatement(CLOSE_CURLY_BRACE.prependIndent())
            .addStatement(
                functionUtils.returnStatement(
                    returnFunction = true,
                    funcName = FUNC_TO_JSON,
                    argument = ARG_OBJECT_MODEL,
                    toString = true
                )
            ).build()
    }

    private fun hasAnnotation(property: KSPropertyDeclaration): Boolean {
        return property.annotations.any {
            it.annotationType.resolve().declaration.qualifiedName?.asString() ==
                    Ignore::class.qualifiedName
        }
    }

    companion object {
        const val FUNC_FROM_JSON = "fromJson"
        const val FUNC_TO_JSON_STRING = "toJsonString"
        const val FUNC_TO_JSON = "toJson"
        const val ARG_OBJECT_MODEL = "model"
        const val ARG_JSON_STRING = "json"
        const val ARG_JSON_OBJECT = "jsonObject"
        const val NULL_VALUE = "null"
        const val CLOSE_CURLY_BRACE = "}"
        const val NULL_JSON_EXCEPTION = "\"JSON content should not be null.\""
        const val NULL_MODEL_EXCEPTION = "\"Model content should not be null.\""
    }
}