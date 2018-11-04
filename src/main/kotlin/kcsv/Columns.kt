package kcsv

data class Columns(

        private val columns: List<Column>

) : List<Column> by columns {

    val rowsCount = columns.first().data.size

    val columnNameToIdx: Map<String, Int> = columns.mapIndexed { index, column -> column.name to index }
            .toMap()

    val header: List<String> by lazy {
        columns.map { it.name }
    }

    fun toRows(): Rows {
        return Rows(this)
    }

    operator fun get(columnName: String): Column {
        return get(columnNameToIdx[columnName] ?: throw Exception("No such column: $columnName"))
    }

    operator fun plus(other: Columns): Columns {

        if (header.size != other.header.size) {
            throw Exception("Tables have different columns count: ${header.size} and ${other.header.size}")
        }
        if (header.toSet() != other.header.toSet()) {
            throw Exception("Tables have different columns: ${header.minus(other.header)}  ${other.header.minus(header)}")
        }
        if (header != other.header) {
            throw Exception("Tables have different column order")
        }

        return Columns(this.columns.mapIndexed { index, column -> column + other.columns[index] })
    }

    override fun toString(): String {

        val header = columns.map { it.name.padEnd(it.maxWidth) }.joinToString(" ")

        val body = (0 until rowsCount).asSequence()
                .map { i -> columns.map { it.data[i].padEnd(it.maxWidth) }.joinToString(" ") }
                .joinToString("\n")

        return StringBuilder(header).append("\n").append(body).toString()
    }

    fun print() : Columns{
        println(this)
        return this
    }

    fun toCsv(): String {
        val header = columns.map { it.name }.joinToString(",")
        val body = toRows().map { it.toCsv() }.joinToString("\n")
        return StringBuilder(header).append("\n").append(body).toString()
    }
}

