package kcsv

import java.io.File
import java.time.Duration
import java.time.Instant

class Parser(
        private val config: Config
) {
    companion object {
        private val REGEX = """,(?=([^"]*"[^"]*")*(?![^"]*"))""".toRegex()
    }

    fun parse(): Table {

        val start = Instant.now()
        val resultTable = config.paths.asSequence()
                .flatMap { File(it).walk() }
                .filter { it.isFile && it.extension.toLowerCase() == "csv" }
                .map { readTable(tableName(it), it.readLines(), config.select) }
                .reduce(Table::plus)

        doPrint(resultTable, Duration.between(start, Instant.now()))
        return resultTable
    }

    private fun tableName(file: File): String {
        return when (config.tableNameStrategy) {
            TableNameStrategy.FILE_NAME -> file.nameWithoutExtension
            TableNameStrategy.FILE_NAME_WITH_EXTENSION -> file.name
            TableNameStrategy.FULL_PATH -> file.path
        }
    }

    private fun readTable(tableName: String, textLines: List<String>, select: List<String>): Table {


        val headerColumnNames = textLines.first().split(REGEX).map { trim(it) }

        val columnNamesToSelect = if (select.isEmpty() || Kcsv.`*` in select) {
            val beforeAsterisk = select.dropLastWhile { it != Kcsv.`*` }.dropLast(1)
            val afterAsterisk = select.dropWhile { it != Kcsv.`*` }.drop(1)
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
                    if (it == Kcsv.ROW_NUM) {
                        ColumnMutable(name = it, fromIdx = -1)
                    } else if (it == Kcsv.TABLE_NAME) {
                        ColumnMutable(name = it, fromIdx = -1)
                    } else {
                        val fromIdx = fieldNameToIdx[it] ?: throw Exception("No such field: $it")
                        ColumnMutable(name = it, fromIdx = fromIdx)
                    }
                }

        data.forEachIndexed { idx, it ->

            val row = it.split(REGEX)
            val currentRowNum = idx + 1

            if (config.lineNumbers.isNotEmpty() && currentRowNum !in config.lineNumbers) {
                return@forEachIndexed
            }
            if (row.size != headerColumnNames.size) {
                config.log.warning("Table $tableName line #$currentRowNum contains ${row.size}, but expected: ${headerColumnNames.size}")
                config.log.finest(it)
                return@forEachIndexed
            }

            for (column in columns) {
                if (column.name == Kcsv.ROW_NUM) {
                    column.data += currentRowNum.toString()
                } else if (column.name == Kcsv.TABLE_NAME) {
                    column.data += tableName
                } else {
                    column.data += trim(row[column.fromIdx])
                }
            }
        }
        val table = Table(columns)
        return table
    }


    private fun trim(value: String): String {
        return if (config.trim) {
            value.trim()
        } else {
            value
        }
    }

    private fun doPrint(table: Table, duration: Duration) {
            println("Took: ${duration.toMillis()} ms")
    }
}
