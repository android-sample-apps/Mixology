package com.yanivsos.conversions

import com.yanivsos.conversions.units.clToMl
import com.yanivsos.conversions.units.clToOz
import com.yanivsos.conversions.units.mlToOz
import com.yanivsos.conversions.units.ozToMl
import org.junit.Test

class Conversions {

    @Test
    fun mlToOz() {
        assert(15.0.mlToOz() == 0.5)
        assert(30.0.mlToOz() == 1.0)
        assert(45.0.mlToOz() == 1.5)
        assert(60.0.mlToOz() == 2.0)
        assert(75.0.mlToOz() == 2.5)
    }

    @Test
    fun ozToMl() {
        assert(2.0.ozToMl() == 60.0)
        assert(1.5.ozToMl() == 45.0)
        assert(1.0.ozToMl() == 30.0)
        assert(0.5.ozToMl() == 15.0)
        assert((5.0 / 3.0).ozToMl() == 50.0)
    }

    @Test
    fun clToMl() {
        assert(1.0.clToMl() == 10.0)
        assert(2.0.clToMl() == 20.0)
        assert(1.5.clToMl() == 15.0)
        assert(4.5.clToMl() == 45.0)
    }

    @Test
    fun clToOz() {
        assert(1.0.clToOz() == 1.0.div(3))
        assert(3.0.clToOz() == 1.0)
        assert(1.5.clToOz() == 0.5)
        assert(4.5.clToOz() == 1.5)
    }
}
