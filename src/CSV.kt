import java.io.File


fun Any.print() {
    println(this)
}

fun main(args: Array<String>) {
    CSV.from(path = "/Users/maxfarsikov/Downloads/Berlin and Venice YTD 24 June1.csv",
        select = listOf("PROFILE ID", "TITLE")
    ).map { (profileId, title) -> profileId to title}
        .toMap()
        .print()

}





object CSV {
    fun from(path: String, select: List<String>): Table {
        val lines = File(path)
            .readLines()

        val fieldNameToIdx = lines.first().split(",")
            .mapIndexed { i, s -> s to i }
            .toMap()

        val data = lines.drop(1)

        val columns = select.map { Column(name = it, fromIdx = fieldNameToIdx[it]!!) }

        data.map {
            val row = it.split(",")
            for (column in columns) {
                column.data += row[column.fromIdx]
            }
        }
        return Table(columns, data.size)
    }
}

data class Column(
    val fromIdx: Int,
    val name: String,
    val data: MutableList<String> = mutableListOf()
)

class Row(
    val rowNum: Int,
    val columns: List<Column>
)

operator fun Row.component1(): String {
    return columns[0].data[rowNum]
}

operator fun Row.component2(): String {
    return columns[1].data[rowNum]
}

data class Table(
    val columns: List<Column>,
    val rowsCount: Int
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