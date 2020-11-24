import kotlin.random.Random

interface IDistribution<TElement> {
    fun randomElement(random: Random): TElement
}