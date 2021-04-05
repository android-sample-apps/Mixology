package com.yanivsos.mixological.domain

import com.yanivsos.conversions.units.FluidUnitsToSystemConverter
import com.yanivsos.conversions.units.system.MeasurementSystem
import com.yanivsos.mixological.conversions.MeasurementPreference
import com.yanivsos.mixological.conversions.MeasurementSystemParser
import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.ui.mappers.FluidMeasurementUnitMapper
import com.yanivsos.mixological.ui.mappers.KeepOriginalMeasurementUnitMapper
import com.yanivsos.mixological.ui.mappers.MeasurementUnitMapper
import com.yanivsos.mixological.ui.models.AppSettings
import timber.log.Timber

// TODO: 05/04/2021 refactor this thing
@Suppress("unused")
class ConvertMeasurementUseCase {

    fun convert(drinkModel: DrinkModel): DrinkModel {
        val measurementPreference = MeasurementSystemParser.parse(AppSettings.measurementSystem)
        Timber.d("converting to $measurementPreference")
        val fluidParser = getFluidMeasurementParser(measurementPreference)
        val ingredients = drinkModel.ingredients.mapValues {
            fluidParser.mapTo(it.value)
        }
        return drinkModel.copy(
            ingredients = ingredients
        )
    }

    private fun getFluidMeasurementParser(measurementSystem: MeasurementPreference): MeasurementUnitMapper {
        return when (measurementSystem) {
            MeasurementPreference.Original -> KeepOriginalMeasurementUnitMapper()
            is MeasurementPreference.System -> getFluidFromSystem(measurementSystem.measurementSystem)
        }
    }

    private fun getFluidFromSystem(measurementSystem: MeasurementSystem): MeasurementUnitMapper {
        return FluidMeasurementUnitMapper(FluidUnitsToSystemConverter(measurementSystem))
    }
}
