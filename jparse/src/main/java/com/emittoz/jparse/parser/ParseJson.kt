package com.emittoz.jparse.parser

import com.emittoz.jparse.parser.FunctionProvider.Companion.FUNC_FROM_JSON
import com.emittoz.jparse.parser.FunctionUtils.Companion.CLOSE_BRACE
import com.emittoz.jparse.parser.FunctionUtils.Companion.DOT
import com.emittoz.jparse.parser.FunctionUtils.Companion.JSON_OBJECT
import com.emittoz.jparse.parser.FunctionUtils.Companion.OPEN_BRACE
import com.emittoz.jparse.processor.JParseProcessor.Companion.NAME_PREFIX
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName

class ParseJson {

    fun getDataFromJson(
        jsonPropertyName: String,
        returnType: TypeName
    ): String {
        return when (returnType) {
            String::class.asTypeName() -> {
                getJsonElementValue(
                    OPT_STRING,
                    jsonPropertyName
                )
            }

            Int::class.asTypeName() -> {
                getJsonElementValue(
                    OPT_INT,
                    jsonPropertyName
                )
            }

            Boolean::class.asTypeName() -> {
                getJsonElementValue(
                    OPT_BOOLEAN,
                    jsonPropertyName
                )
            }

            Long::class.asTypeName() -> {
                getJsonElementValue(
                    OPT_LONG,
                    jsonPropertyName
                )
            }

            Double::class.asTypeName() -> {
                getJsonElementValue(
                    OPT_DOUBLE,
                    jsonPropertyName
                )
            }

            List::class.asTypeName() -> {
                "${getList(jsonPropertyName, returnType)}"
            }

            else -> {
                getObject(propertyName = jsonPropertyName, typeName = returnType)
            }
        }
    }

    private fun getJsonElementValue(elementName: String, propertyName: String): String {
        return "$JSON_OBJECT$DOT$elementName$OPEN_BRACE\"$propertyName\"$CLOSE_BRACE"
    }

    private fun getObject(propertyName: String, typeName: TypeName): String {
        return "${NAME_PREFIX}${getClassName(typeName)}()${DOT}${FUNC_FROM_JSON}(${OPT_JSON_OBJECT}(\"$propertyName\"))"
    }

    private fun getList(propertyName: String, typeName: TypeName): PropertySpec {
        //val className = typeName as ClassName
        val listType = MutableList::class.asClassName().parameterizedBy(typeName)
        return PropertySpec.builder(propertyName, typeName)
            .initializer("mutableListOf<%T>()", listType)
            .build()
    }

    private fun getClassName(typeName: TypeName): String {
        return when (typeName) {
            is ClassName -> typeName.simpleName
            is ParameterizedTypeName -> typeName.rawType.simpleName
            else -> throw IllegalArgumentException("Unsupported TypeName: $typeName")
        }
    }

    companion object {
        const val OPT_STRING = "optString"
        const val OPT_INT = "optInt"
        const val OPT_DOUBLE = "optDouble"
        const val OPT_LONG = "optLong"
        const val OPT_BOOLEAN = "optBoolean"
        const val OPT_JSON_OBJECT = "optJSONObject"
    }
}
