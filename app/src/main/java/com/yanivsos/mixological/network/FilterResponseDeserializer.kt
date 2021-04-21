/*
package com.yanivsos.mixological.network

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.yanivsos.mixological.network.response.DrinkPreviewResponse
import com.yanivsos.mixological.network.response.DrinksWrapperResponse
import com.yanivsos.mixological.network.response.SERIALIZED_NAME_DRINKS
import com.yanivsos.mixological.network.response.SERIALIZED_NAME_NONE_FOUND
import java.lang.reflect.Type


TODO("replace this whole thing")
class FilterResponseDeserializer : JsonDeserializer<DrinksWrapperResponse<DrinkPreviewResponse>> {
    private val gson = Gson()
    private val type = object : TypeToken<DrinksWrapperResponse<DrinkPreviewResponse>>() {}.type

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): DrinksWrapperResponse<DrinkPreviewResponse> {
        val jsonObject = json.asJsonObject
        val drinks = jsonObject.get(SERIALIZED_NAME_DRINKS)

        runCatching<DrinksWrapperResponse<DrinkPreviewResponse>> { gson.fromJson(json, type) }
            .onSuccess { return it }

        drinks.asStringOrNull()?.let { stringValue ->
            if (stringValue == SERIALIZED_NAME_NONE_FOUND) {
                return DrinksWrapperResponse(emptyList())
            }
        }

        throw JsonSyntaxException("Unable to parse string value of $drinks")
    }

    private fun JsonElement.asStringOrNull(): String? {
        return try {
            asString
        } catch (e: Exception) {
            null
        }
    }
}
*/
