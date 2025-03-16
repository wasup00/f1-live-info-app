package com.example.f1liveinfo.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

/**
 * Custom serializer that can handle flexible value types (string, float, or null)
 */
object FlexibleValueSerializer : KSerializer<String?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FlexibleValue", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String?) {
        encoder.encodeString(value ?: "")
    }

    override fun deserialize(decoder: Decoder): String? {
        val jsonDecoder =
            decoder as? JsonDecoder ?: throw IllegalStateException("Expected JSON decoder")
        val element = jsonDecoder.decodeJsonElement()

        return when {
            element is JsonNull -> null
            element is JsonPrimitive && element.isString -> element.content
            element is JsonPrimitive -> element.toString()
            else -> element.toString()
        }
    }
}