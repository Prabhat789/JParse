package com.emittoz.jparse.parser

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.asTypeName

class FunctionProvider(
    private val kClass: KSClassDeclaration,
    private val packageName: String = kClass.packageName.asString(),
    private val fileName: String = kClass.simpleName.asString()
) {

    fun fromJsonWithStringArgs(): FunSpec {
        return FunSpec.builder(name = FUNC_FROM_JSON)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(ARG_JSON_STRING, String::class.asTypeName().copy(nullable = true))
            .returns(ClassName(packageName = packageName, fileName))
            .addStatement("")
            .build()
    }

    companion object {
        const val FUNC_FROM_JSON = "fromJson"
        const val ARG_JSON_STRING = "json"
    }
}