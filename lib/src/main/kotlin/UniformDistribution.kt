import kotlin.random.Random

class UniformDistribution<TElement>(val elements: List<TElement>) : IDistribution<TElement>
{
    override fun RandomElement(random: Random): TElement {
        val index = random.nextInt(0, elements.size)
        return elements.get(index)
    }
}