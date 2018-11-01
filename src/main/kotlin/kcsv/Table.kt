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

    override fun toString(): String {

        val header = columns.map { it.name.padEnd(it.width) }.joinToString(" ")

        val body = (0 until rowsCount).asSequence()
                .map { i -> columns.map { it.data[i].padEnd(it.width) }.joinToString(" ") }
                .joinToString("\n")

        return StringBuilder(header).append("\n").append(body).toString()
    }

    fun print() {
        println(this)
    }

    fun toCsv(): String {
        val header = columns.map { it.name }.joinToString(",")
        val body = rows.map { it.toCsv() }.joinToString("\n")
        return StringBuilder(header).append("\n").append(body).toString()
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
