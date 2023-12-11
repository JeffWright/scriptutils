package dev.jtbw.scriptutils

infix fun Any?.shouldBe(other: Any?) {
    require(this == other) { "should be $other (${other?.javaClass?.simpleName}) but was $this (${this?.javaClass?.simpleName})" }
}

infix fun Any?.shouldNotBe(other: Any?) {
    require(this != other) { "should NOT be $other but was $this" }
    if(this is Number && other is Number) {
        require(this.toDouble() != other.toDouble()){ "should NOT be $other (${other.javaClass.simpleName}) but was $this (${this.javaClass.simpleName})" }
    }
}
