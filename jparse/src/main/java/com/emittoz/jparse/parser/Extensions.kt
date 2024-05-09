package com.emittoz.jparse.parser

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

fun KSDeclaration.toTypeName(): TypeName {
    val qualifiedName = (this as? KSClassDeclaration)?.qualifiedName?.asString()
    return if (qualifiedName != null) {
        ClassName.bestGuess(qualifiedName)
    } else {
        throw IllegalArgumentException("Cannot determine type name for $this")
    }
}
