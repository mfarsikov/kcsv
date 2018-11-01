package kcsv

import java.util.logging.Level
import java.util.logging.Logger

interface IConfig {
    /**
     * Path to CSV file, or folder with CSV files. Folder will be deeply scanned searching *.csv flies.
     */
    fun addPath(path: String)

    /**
     * Accepts field names, which should be selected from CSV file.
     * Also accepts Kcsv.ROW_NUM (line number from parsed file),
     * Kcsv.TABLE_NAME (usually file name),
     * and "*" symbol (means all the columns)
     * Columns in result table will be in same order as they are specified in select
     */
    fun select(vararg fields: String)

    /**
     * Forces to remove leading and trailing spaces for all values and column names
     */
    fun trim(trim: Boolean = true)

    /**
     * Table name could be one of: file name, file name without extension, full file name
     */
    fun tableNameStrategy(strategy: TableNameStrategy)

    /**
     * Default logger level logs warnings. Level.OFF mutes log, Level.FINE shows lines caused errors
     */
    fun logger(strategy: Level)

    /**
     * File line numbers for select
     */
    fun lineNumber(vararg lineNums: Int)

}

class Config : IConfig {
    var paths = mutableListOf<String>()
    var select: List<String> = listOf()
    var lineNumbers = setOf<Int>()
    val log = Logger.getLogger("Kcsv")
    var trim = false
    var tableNameStrategy = TableNameStrategy.FILE_NAME

    override fun addPath(path: String) {
        this.paths.add(path)
    }

    override fun select(vararg fields: String) {
        select = fields.asList()
    }

    override fun lineNumber(vararg lineNums: Int) {
        lineNumbers = lineNums.toSet()
    }

    override fun trim(trim: Boolean) {
        this.trim = trim
    }

    override fun tableNameStrategy(strategy: TableNameStrategy) {
        tableNameStrategy = strategy
    }

    override fun logger(strategy: Level) {
        log.level = strategy
    }
}

