package com.emittoz.jparse.processor

import com.emittoz.jparse.annotations.Serializable
import com.emittoz.jparse.parser.FunctionProvider
import com.emittoz.jparse.serialise.Serialise
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.writeTo

class JParseProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(
            Serializable::class.qualifiedName.toString()
        ).filterIsInstance<KSClassDeclaration>().forEach { symbol ->
            generateSource(symbol)
        }
        return emptyList()
    }

    private fun generateSource(kClass: KSClassDeclaration) {
        val className = kClass.simpleName.asString()
        val packageName = kClass.packageName.asString()
        val functionProvider = FunctionProvider(kClass)
        val fromJsonWithStringArgs = functionProvider.fromJsonWithStringArgs()
        val fromJsonWithJsonArgs = functionProvider.fromJsonWithJsonArgs()
        val toJsonStringFromModel = functionProvider.toJsonStringFromModel()
        val toJsonObjectFromModel = functionProvider.toJsonObjectFromModel()
        val fileSpec =
            FileSpec.builder(packageName = packageName, fileName = "$NAME_PREFIX$className")
                .addType(
                    TypeSpec.classBuilder("$NAME_PREFIX$className")
                        .addSuperinterface(
                            Serialise::class.asClassName()
                                .parameterizedBy(ClassName(packageName = packageName, className))
                        )
                        .addFunction(fromJsonWithStringArgs)
                        .addFunction(fromJsonWithJsonArgs)
                        .addFunction(toJsonStringFromModel)
                        .addFunction(toJsonObjectFromModel)
                        .build()
                ).build()
        fileSpec.writeTo(codeGenerator, true)
    }

    companion object {
        const val NAME_PREFIX = "Serialised"
    }
}
