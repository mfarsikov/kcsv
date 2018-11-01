package kcsv

import java.io.File
import java.time.Duration
import java.time.Instant
import java.util.logging.Level
import java.util.logging.Logger


class Kcsv {
    private var paths = mutableListOf<String>()
    private var select: List<String> = listOf()
    private var printHeader = false
    private var printData = false
    private var lineNumbers = setOf<Int>()
    private val log = Logger.getLogger("Kcsv")
    private var split = Split.QUOTED
    private var trim = false
    private var tableNameStrategy = TableNameStrategy.FILE_NAME
    private var timed = false
    private lateinit var duration: Duration

    private constructor()

    companion object {
        operator fun invoke(config: Kcsv.() -> Unit): Table {
            val csv = Kcsv()
            csv.config()
            return csv.doo()
        }

        const val ROWNUM = "#ROW_NUM"
        const val TABLE_NAME = "#TABLE_NAME"
        const val `*` = "*"
        private val REGEX = """,(?=([^"]*"[^"]*")*(?![^"]*"))""".toRegex()
    }

    fun path(path: String) {
        this.paths.add(path)
    }

    fun select(vararg fields: String) {
        select = fields.asList()
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

    fun lineNumber(vararg lineNums: Int) {
        lineNumbers = lineNums.toSet()
    }

    fun split(split: Split) {
        this.split = split
    }

    fun trim(trim: Boolean = true) {
        this.trim = trim
    }

    fun tableName(strategy: TableNameStrategy) {
        tableNameStrategy = strategy
    }

    fun logger(strategy: Level){
        log.level = strategy
    }

    fun timed(){
        timed = true
    }

    private fun doo(): Table {

        val start = Instant.now()
        val resultTable = paths.asSequence()
            .flatMap { File(it).walk() }
            .filter { it.isFile && it.extension.toLowerCase() == "csv" }
            .map { readTable(tableName(it), it.readLines(), select) }
            .reduce(Table::plus)


        duration = Duration.between(start, Instant.now())
        doPrint(resultTable)
        return resultTable
    }

    private fun tableName(file: File): String {
        return when (tableNameStrategy) {
            TableNameStrategy.FILE_NAME -> file.nameWithoutExtension
            TableNameStrategy.FILE_NAME_WITH_EXTENSION -> file.name
            TableNameStrategy.FULL_PATH -> file.path
        }
    }

    private fun readTable(tableName: String, textLines: List<String>, select: List<String>): Table {


        val headerColumnNames = split(textLines.first()).map { trim(it) }

        val columnNamesToSelect = if (select.isEmpty() || `*` in select) {
            val beforeAsterisk = select.dropLastWhile { it != `*` }.dropLast(1)
            val afterAsterisk = select.dropWhile { it != `*` }.drop(1)
            beforeAsterisk + headerColumnNames + afterAsterisk
        } else {
            select
        }

        val fieldNameToIdx = headerColumnNames
            .mapIndexed { i, s -> s to i }
            .toMap()

        val data = textLines.drop(1)

        val columns = columnNamesToSelect
            .map {
                if (it == ROWNUM) {
                    ColumnMutable(name = it, fromIdx = -1)
                } else if (it == TABLE_NAME) {
                    ColumnMutable(name = it, fromIdx = -1)
                } else {
                    val fromIdx = fieldNameToIdx[it] ?: throw Exception("No such field: $it")
                    ColumnMutable(name = it, fromIdx = fromIdx)
                }
            }

        data.forEachIndexed { idx, it ->

            val row = split(it)
            val currentRowNum = idx + 1

            if (lineNumbers.isNotEmpty() && currentRowNum !in lineNumbers) {
                return@forEachIndexed
            }
            if (row.size != headerColumnNames.size) {
                log.warning("Table $tableName line #$currentRowNum contains ${row.size}, but expected: ${headerColumnNames.size}")
                log.finest(it)
                return@forEachIndexed
            }

            for (column in columns) {
                if (column.name == ROWNUM) {
                    column.data += currentRowNum.toString()
                } else if (column.name == TABLE_NAME) {
                    column.data += tableName
                } else {
                    column.data += trim(row[column.fromIdx])
                }
            }
        }
        val table = Table(columns)
        return table
    }

    private fun split(line: String): List<String> {
        return when (split) {
            Split.SIMPLE -> line.split(",")
            Split.QUOTED -> line.split(REGEX)
        }
    }

    private fun trim(value: String): String {
        return if (trim) {
            value.trim()
        } else {
            value
        }
    }

    private fun doPrint(table: Table) {
        if (printHeader) {
            println(table.columnNameToIdx.keys.joinToString())
        }
        if (printData) {
            table.rows.forEach(::println)
        }
        if(timed){
            println("Took: ${duration.toMillis()} ms")
        }
    }
}

enum class Split { SIMPLE, QUOTED }
enum class TableNameStrategy { FILE_NAME, FILE_NAME_WITH_EXTENSION, FULL_PATH }
