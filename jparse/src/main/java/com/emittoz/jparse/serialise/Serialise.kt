package com.emittoz.jparse.serialise

import org.json.JSONObject

interface Serialise<T> {

    fun fromJson(json: String?): T

    fun fromJson(jsonObject: JSONObject?): T

    fun toJson(model: T?): JSONObject

    fun toJsonString(model: T?): String
}