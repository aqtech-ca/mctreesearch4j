package Twenty48

class Game2048Controller(){

    fun isGridSolved(grid: Array<Array<Int>>): Boolean = grid.any { row -> row.contains(2048) }
    fun isGridFull(grid: Array<Array<Int>>): Boolean = grid.all { row -> !row.contains(0) }

    fun spawnNumber(grid: Array<Array<Int>>): Array<Array<Int>> {

        val newGrid = grid.copyOf()
        val coordinates = locateSpawnCoordinates(newGrid)
        val number = generateNumber()
        // val number = 2
        return updateGrid(newGrid, coordinates, number)
    }

    fun locateSpawnCoordinates(grid: Array<Array<Int>>): Pair<Int, Int> {
        val emptyCells = arrayListOf<Pair<Int, Int>>()
        grid.forEachIndexed { x, row ->
            row.forEachIndexed { y, cell ->
                if (cell == 0) emptyCells.add(Pair(x, y))
            }
        }
        return emptyCells[(Math.random() * (emptyCells.size - 1)).toInt()]
    }

    fun generateNumber(): Int = if (Math.random() > 0.10) 2 else 4

    fun updateGrid(grid: Array<Array<Int>>, at: Pair<Int, Int>, value: Int): Array<Array<Int>> {
        // val updatedGrid = grid.copyOf()
        var updatedGrid = arrayOf(
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0)
        )
        for (r in 0 until updatedGrid.size) {
            for (c in 0 until updatedGrid[r].size){
                updatedGrid[r][c] = grid[r][c]
            }
        }
        updatedGrid[at.first][at.second] = value
        return updatedGrid.copyOf()
    }

    fun isValidInput(input: String): Boolean = arrayOf("up", "left", "right", "down").contains(input)

    fun manipulateGrid(grid: Array<Array<Int>>, input: String): Array<Array<Int>> = when (input) {
        "left" -> shiftCellsLeft(grid)
        "down" -> shiftCellsDown(grid)
        "right" -> shiftCellsRight(grid)
        "up" -> shiftCellsUp(grid)
        else -> throw IllegalArgumentException("Expected one of [up, left, right, down]")
    }

    fun shiftCellsLeft(grid: Array<Array<Int>>): Array<Array<Int>> {
        return grid.map(::mergeAndOrganizeCells).toTypedArray().copyOf()
    }


    fun shiftCellsRight(grid: Array<Array<Int>>): Array<Array<Int>> {
        return grid.map { row -> mergeAndOrganizeCells(row.reversed().toTypedArray()).reversed().toTypedArray() }.toTypedArray().copyOf()
    }

    fun shiftCellsUp(grid: Array<Array<Int>>): Array<Array<Int>> {
        val rows: Array<Array<Int>> = arrayOf(
            arrayOf(grid[0][0], grid[1][0], grid[2][0], grid[3][0]),
            arrayOf(grid[0][1], grid[1][1], grid[2][1], grid[3][1]),
            arrayOf(grid[0][2], grid[1][2], grid[2][2], grid[3][2]),
            arrayOf(grid[0][3], grid[1][3], grid[2][3], grid[3][3])
        )

        val updatedGrid = grid.copyOf()

        rows.map(::mergeAndOrganizeCells).forEachIndexed { rowIdx, row ->
            updatedGrid[0][rowIdx] = row[0]
            updatedGrid[1][rowIdx] = row[1]
            updatedGrid[2][rowIdx] = row[2]
            updatedGrid[3][rowIdx] = row[3]
        }

        return updatedGrid
    }

    fun shiftCellsDown(grid: Array<Array<Int>>): Array<Array<Int>> {
        val rows: Array<Array<Int>> = arrayOf(
            arrayOf(grid[3][0], grid[2][0], grid[1][0], grid[0][0]),
            arrayOf(grid[3][1], grid[2][1], grid[1][1], grid[0][1]),
            arrayOf(grid[3][2], grid[2][2], grid[1][2], grid[0][2]),
            arrayOf(grid[3][3], grid[2][3], grid[1][3], grid[0][3])
        )

        val updatedGrid = grid.copyOf()

        rows.map(::mergeAndOrganizeCells).forEachIndexed { rowIdx, row ->
            updatedGrid[3][rowIdx] = row[0]
            updatedGrid[2][rowIdx] = row[1]
            updatedGrid[1][rowIdx] = row[2]
            updatedGrid[0][rowIdx] = row[3]
        }

        return updatedGrid
    }

    fun mergeAndOrganizeCells(row: Array<Int>): Array<Int> = organize(merge(row.copyOf())).copyOf()

    fun merge(row: Array<Int>, idxToMatch: Int = 0, idxToCompare: Int = 1): Array<Int> {
        if (idxToMatch >= row.size) return row
        if (idxToCompare >= row.size) return merge(row, idxToMatch + 1, idxToMatch + 2)
        if (row[idxToMatch] == 0) return merge(row, idxToMatch + 1, idxToMatch + 2)

        return if (row[idxToMatch] == row[idxToCompare]) {
            row[idxToMatch] *= 2
            row[idxToCompare] = 0
            merge(row, idxToMatch + 1, idxToMatch + 2)
        } else {
            if (row[idxToCompare] != 0) merge(row, idxToMatch + 1, idxToMatch + 2)
            else merge(row, idxToMatch, idxToCompare + 1)
        }
    }

    fun organize(row: Array<Int>, idxToMatch: Int = 0, idxToCompare: Int = 1): Array<Int> {
        if (idxToMatch >= row.size) return row
        if (idxToCompare >= row.size) return organize(row, idxToMatch + 1, idxToMatch + 2)
        if (row[idxToMatch] != 0) return organize(row, idxToMatch + 1, idxToMatch + 2)

        return if (row[idxToCompare] != 0) {
            row[idxToMatch] = row[idxToCompare]
            row[idxToCompare] = 0
            organize(row, idxToMatch + 1, idxToMatch + 2)
        } else {
            organize(row, idxToMatch, idxToCompare + 1)
        }
    }

}

