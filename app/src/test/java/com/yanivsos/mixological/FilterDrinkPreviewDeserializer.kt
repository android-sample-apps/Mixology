package com.yanivsos.mixological

import com.yanivsos.mixological.network.response.FilterDrinkPreviewResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test

class FilterDrinkPreviewDeserializer {

    @Test
    fun `test deserialization with results`() {
        val json =
            "{\"drinks\":[{\"strDrink\":\"Jelly Bean\",\"strDrinkThumb\":\"https:\\/\\/www.thecocktaildb.com\\/images\\/media\\/drink\\/bglc6y1504388797.jpg\",\"idDrink\":\"13775\"},{\"strDrink\":\"Turf Cocktail\",\"strDrinkThumb\":\"https:\\/\\/www.thecocktaildb.com\\/images\\/media\\/drink\\/utypqq1441554367.jpg\",\"idDrink\":\"12418\"}]}"
        val response = Json { }.decodeFromString<FilterDrinkPreviewResponse>(json)
        assert(response is FilterDrinkPreviewResponse.Results && response.drinks.size == 2)
    }

    @Test
    fun `test deserialization without results`() {
        val json = "{\"drinks\":\"None Found\"}"
        val response = Json { }.decodeFromString<FilterDrinkPreviewResponse>(json)
        assert(response is FilterDrinkPreviewResponse.NoResults && response.message == "None Found")
    }

}

