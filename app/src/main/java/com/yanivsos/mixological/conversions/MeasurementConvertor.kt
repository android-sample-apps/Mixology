package com.yanivsos.mixological.conversions

fun Double.ozToMl(): Double {
    return this.times(30)
}

fun Double.mlToOz(): Double {
    return this.div(30)
}

fun Double.clToMl(): Double {
    return this.times(10)
}

fun Double.clToOz(): Double {
    return clToMl().mlToOz()
}
