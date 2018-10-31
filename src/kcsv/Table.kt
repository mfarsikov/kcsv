package kcsv

data class Table(
    private val columns: List<Column>,
    private val rowsCount: Int
) : Iterable<Row> {
    override fun iterator(): Iterator<Row> {
        return object : Iterator<Row> {
            var nextIdx = 0
            override fun hasNext(): Boolean {
                return nextIdx < rowsCount
            }

            override fun next(): Row {
                val r = Row(nextIdx, columns)
                nextIdx++
                return r
            }
        }
    }
}