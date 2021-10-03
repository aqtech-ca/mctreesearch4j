package ca

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CoreLibraryTests {

    fun coreLibraryTest(i: Int) {
        // println("cool test")
        true
    }

    @Test fun coreLibraryTestGo() {
        this.coreLibraryTest(1)
        // false
        // assertTrue(4 == 7, "failed test")
    }
}