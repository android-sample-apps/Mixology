package com.yanivsos.mixological.network.response

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

object FilteredDrinkPreviewsSerializer :
    JsonContentPolymorphicSerializer<FilterDrinkPreviewResponse>(FilterDrinkPreviewResponse::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out FilterDrinkPreviewResponse> {
        return when {
            noResults(element) -> FilterDrinkPreviewResponse.NoResults.serializer()
            else -> FilterDrinkPreviewResponse.Results.serializer()
        }
    }

    private fun noResults(element: JsonElement): Boolean {
        return element.jsonObject[SERIALIZED_NAME_DRINKS] is JsonPrimitive
    }
}
