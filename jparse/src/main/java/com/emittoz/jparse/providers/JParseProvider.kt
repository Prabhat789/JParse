package com.emittoz.jparse.providers

import com.emittoz.jparse.processor.JParseProcessor
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class JParseProvider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return JParseProcessor(environment.codeGenerator, environment.logger)
    }
}
