package com.yanivsos.mixological.ui.mappers

import com.yanivsos.mixological.R
import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.domain.models.Result
import com.yanivsos.mixological.ui.models.DrinkErrorUiModel
import com.yanivsos.mixological.ui.models.DrinkUiModel
import com.yanivsos.mixological.ui.models.ResultUiModel
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.function.Function

class ResultDrinkMapperUi(
    private val drinkMapper: Function<DrinkModel, DrinkUiModel>,
    private val drinkId: String
) : Function<Result<DrinkModel>, ResultUiModel<DrinkUiModel>> {

    override fun apply(t: Result<DrinkModel>): ResultUiModel<DrinkUiModel> {
        return when (t) {
            is Result.Success -> parseSuccess(t.data)
            is Result.Error -> parseError(t.tr)
            is Result.Loading -> parseLoading(t.id)
        }
    }

    private fun parseSuccess(data: DrinkModel): ResultUiModel<DrinkUiModel> {
        return ResultUiModel.Success(
            drinkMapper.apply(data)
        )
    }

    private fun parseLoading(id: String): ResultUiModel<DrinkUiModel> {
        return ResultUiModel.Loading(id)
    }

    private fun parseError(tr: Throwable): ResultUiModel<DrinkUiModel> {
        val errorUiModel: DrinkErrorUiModel = when (tr) {
            is UnknownHostException -> connectivityError()
            is SocketTimeoutException -> timeoutError()
            else -> defaultError()
        }

        return ResultUiModel.Error(errorUiModel)
    }

    private fun connectivityError(): DrinkErrorUiModel {
        return DrinkErrorUiModel(
            drinkId = drinkId,
            title = R.string.error_title,
            description = R.string.error_description_connectivity,
            lottieAnimation = R.raw.no_connection
        )
    }

    private fun timeoutError(): DrinkErrorUiModel {
        return DrinkErrorUiModel(
            drinkId = drinkId,
            title = R.string.error_title,
            description = R.string.error_description_timeout,
            lottieAnimation = R.raw.no_connection
        )
    }

    private fun defaultError(): DrinkErrorUiModel {
        return DrinkErrorUiModel(
            drinkId = drinkId,
            title = R.string.error_title,
            description = R.string.error_description_default,
            lottieAnimation = R.raw.something_went_wrong
        )
    }
}