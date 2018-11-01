package kcsv

data class Table(
    val columns: List<Column>
) {
    val rowsCount = columns.first().data.size
    val columnNameToIdx: Map<String, Int> = columns.mapIndexed { index, column -> column.name to index }.toMap()

    val rows: Rows
        get() {
            return Rows(this)
        }

    operator fun get(columnName: String): Column {
        return columns.first { it.name == columnName }
    }

    operator fun plus(other: Table): Table {

        val thisNames = this.columns.map { it.name }
        val otherNames = other.columns.map { it.name }
        if (thisNames.size != otherNames.size) {
            throw Exception("Tables have different columns count: ${thisNames.size} and ${otherNames.size}")
        }
        if (thisNames.toSet() != otherNames.toSet()) {
            throw Exception("Tables have different columns: ${thisNames.minus(otherNames)}  ${otherNames.minus(thisNames)}")

        }
        if (thisNames != otherNames) {
            throw Exception("Tables have different column order")
        }

        return Table(this.columns.mapIndexed { index, column -> column + other.columns[index] })
    }
}

class Rows(private val table: Table) : Iterable<Row> {
    override fun iterator(): Iterator<Row> {
        return object : Iterator<Row> {
            var nextIdx = 0
            override fun hasNext(): Boolean {
                return nextIdx < table.rowsCount
            }

            override fun next(): Row {
                val r = Row(nextIdx, table)
                nextIdx++
                return r
            }
        }
    }
}
