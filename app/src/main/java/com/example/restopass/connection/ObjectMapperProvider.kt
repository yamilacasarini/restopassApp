package com.example.restopass.connection

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object ObjectMapperProvider {
    val mapper = jacksonObjectMapper()

    init {
        mapper.apply {
            registerModule(KotlinModule())
            enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
            propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE

            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }
}