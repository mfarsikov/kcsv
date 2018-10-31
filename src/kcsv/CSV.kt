package kcsv

import java.io.File


class CSV {
    private lateinit var path: String
    private var columnNamesToSelect: List<String> = listOf()
    private var printHeader = false
    private var printData = false
    private var skip: Int = 0
    private var limit: Int = Int.MAX_VALUE

    private constructor()

    companion object {
        operator fun invoke(config: CSV.() -> Unit): Table {
            val csv = CSV()
            csv.config()
            return csv.doo()
        }
        const val ROWNUM = "#"
    }

    fun path(path: String) {
        this.path = path
    }

    fun select(vararg fields: String) {
        columnNamesToSelect = fields.asList()
    }

    fun printHeader() {
        printHeader = true
    }

    fun printData() {
        printData = true
    }

    fun print() {
        printHeader = true
        printData = true
    }

    private fun doo(): Table {
        val lines = File(path)
            .readLines()

        val headerColumnNames = lines.first()
            .split(",")

        if (columnNamesToSelect.isEmpty()) {
            columnNamesToSelect = headerColumnNames
        }

        val fieldNameToIdx = headerColumnNames
            .mapIndexed { i, s -> s to i }
            .toMap()

        val data = lines.drop(1)

        val columns = columnNamesToSelect
            .map {
                if(it == ROWNUM){
                    Column(name = it, fromIdx = -1)
                }else{
                    val fromIdx = fieldNameToIdx[it] ?: throw Exception("No such field: $it")
                    Column(name = it, fromIdx = fromIdx)
                }
        }

        var currentRowNum = 0
        data.forEach {
            val row = it.split(",")
            currentRowNum++
            for (column in columns) {
                if(column.name == ROWNUM){
                    column.data += currentRowNum.toString()
                }else {
                    column.data += row[column.fromIdx]
                }
            }
        }
        val table = Table(columns, data.size)
        doPrint(table)
        return table
    }

    private fun doPrint(table: Table) {
        if (printHeader) { println(columnNamesToSelect.joinToString()) }
        if (printData) { table.forEach(::println) }
    }
}
