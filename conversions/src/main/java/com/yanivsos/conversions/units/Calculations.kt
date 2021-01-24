package com.yanivsos.conversions.units

fun Double.ozToMl(): Double {
    return this.times(30)
}

fun Double.ozToGallon(): Double {
    return this.times(0.0078125)
}

fun Double.qtToOz(): Double {
    return this.times(32)
}

fun Double.ozToCl(): Double {
    return this.ozToMl().mlToCl()
}

fun Double.ozToPint(): Double {
    return this.times(0.0625)
}

fun Double.mlToOz(): Double {
    return this.div(30)
}

fun Double.mlToLiter(): Double {
    return this.div(1000)
}

fun Double.literToMl(): Double {
    return this.times(1000)
}

fun Double.clToMl(): Double {
    return this.times(10)
}

fun Double.mlToCl(): Double {
    return this.div(10)
}

fun Double.clToOz(): Double {
    return clToMl().mlToOz()
}

fun Double.ozToQt(): Double {
    return this.times(0.03125)
}

fun Double.galToLiter(): Double {
    return this.times(3.78543)
}

fun Double.galToOz(): Double {
    return this.times(128)
}

fun Double.pintToMl(): Double {
    return this.times(16).ozToMl()
}
