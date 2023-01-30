interface From<F, T> {
    fun from(from: F): T
}

class EvenNumber(private val number: Number) {
    override fun toString(): String {
        return "EvenNumber(number=$number)"
    }
}

object EvenNumberFactory : From<Int, EvenNumber> {
    override fun from(from: Int): EvenNumber {
        return EvenNumber(from);
    }
}

fun <F, T, FA : From<F, T>> F.into(factory: FA): T {
    return factory.from(this)
}

fun main() {
    val eventNumber: EvenNumber = 10.into(EvenNumberFactory)
    println(eventNumber)
}
