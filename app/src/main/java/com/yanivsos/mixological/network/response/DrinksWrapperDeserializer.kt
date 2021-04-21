package com.yanivsos.mixological.network.response

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

class DrinksWrapperDeserializer<T> : KSerializer<DrinksWrapperResponse<T>> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(SERIALIZED_NAME_DRINKS, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DrinksWrapperResponse<T>) {
        encoder.encodeString(Json.encodeToString(value))
    }

    override fun deserialize(decoder: Decoder): DrinksWrapperResponse<T> {
        val json = decoder.decodeString()
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            DrinksWrapperResponse(emptyList())
        }
    }
}
