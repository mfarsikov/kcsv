package kcsv

class Row(
        val rowNum: Int,
        private val columns: Columns
) {
    val data: List<String> by lazy {
        columns.map { it.data[rowNum] }
    }

    override fun toString() = data.joinToString()

    operator fun get(columnIndex: Int): String {
        return columns[columnIndex].data[rowNum]
    }

    operator fun get(columnName: String): String {
        return columns[columnName].data[rowNum]
    }

    fun toCsv(): String {
        return data.joinToString(",")
    }

    operator fun component1() = columns[0].data[rowNum]
    operator fun component2() = columns[1].data[rowNum]
    operator fun component3() = columns[2].data[rowNum]
    operator fun component4() = columns[3].data[rowNum]
    operator fun component5() = columns[4].data[rowNum]
}
