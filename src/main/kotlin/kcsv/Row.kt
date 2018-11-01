package kcsv

class Row(
    val rowNum: Int,
    private val table: Table
) {
    val data: List<String> by lazy {
        table.columns.map { it.data[rowNum] }
    }

    override fun toString() = data.joinToString()
    operator fun get(columnName: String): String {
        val idx = table.columnNameToIdx[columnName] ?: throw Exception("No such column: $columnName")
        return data[idx]
    }

    fun toCsv():String{
       return data.joinToString(",")
    }
}

operator fun Row.component1() = data[0]
operator fun Row.component2() = data[1]
operator fun Row.component3() = data[2]
operator fun Row.component4() = data[3]
operator fun Row.component5() = data[4]
operator fun Row.component6() = data[5]
operator fun Row.component7() = data[6]
operator fun Row.component8() = data[7]
operator fun Row.component9() = data[8]
operator fun Row.component10() = data[9]
operator fun Row.component11() = data[10]
operator fun Row.component12() = data[11]
operator fun Row.component13() = data[12]
operator fun Row.component14() = data[13]
operator fun Row.component15() = data[14]
operator fun Row.component16() = data[15]
operator fun Row.component17() = data[16]
operator fun Row.component18() = data[17]
operator fun Row.component19() = data[18]
operator fun Row.component20() = data[19]
operator fun Row.component21() = data[20]
operator fun Row.component22() = data[21]