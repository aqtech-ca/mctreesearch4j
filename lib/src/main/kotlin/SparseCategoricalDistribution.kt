import kotlin.random.Random

class SparseCategoricalDistribution<TElement>(val probabilisticElements: List<ProbabilisticElement<TElement>>) : IDistribution<TElement>
{
    override fun RandomElement(random: Random): TElement {
        var totalProbabilities = 0.0
        probabilisticElements.forEach { e -> totalProbabilities += e.probability } // simplify?

        val randomValue = random.nextDouble()*totalProbabilities;

        var total = 0.0
        for (probabilisticElement in probabilisticElements) // order guaranteed?
        {
            total += probabilisticElement.probability
            if (total > randomValue)
            {
                return probabilisticElement.element
            }
        }

        // shouldn't happen
        throw Exception()
    }
}