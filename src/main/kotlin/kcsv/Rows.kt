package kcsv

class Rows(

        private val columns: Columns

) : Iterable<Row> {

    override fun iterator(): Iterator<Row> {
        return object : Iterator<Row> {

            private var nextIdx = 0

            override fun hasNext(): Boolean {
                return nextIdx < columns.rowsCount
            }

            override fun next(): Row {
                val nextRow = Row(nextIdx, columns)
                nextIdx++
                return nextRow
            }
        }
    }

    operator fun get(rowNum: Int): Row {
        return Row(rowNum, columns)
    }
}