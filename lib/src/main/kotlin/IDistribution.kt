import kotlin.random.Random

interface IDistribution<TElement> {
    fun RandomElement(random: Random): TElement
}