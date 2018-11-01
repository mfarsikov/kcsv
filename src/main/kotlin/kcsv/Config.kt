package kcsv

import java.util.logging.Level
import java.util.logging.Logger

class Config {
    var paths = mutableListOf<String>()
    var select: List<String> = listOf()
    var printHeader = false
    var printData = false
    var lineNumbers = setOf<Int>()
    val log = Logger.getLogger("Kcsv")
    var split = Split.QUOTED
    var trim = false
    var tableNameStrategy = TableNameStrategy.FILE_NAME
    var timed = false

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

    fun logger(strategy: Level) {
        log.level = strategy
    }

    fun timed() {
        timed = true
    }
}