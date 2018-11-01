package kcsv

import java.io.File
import java.util.logging.Level

fun main(args: Array<String>) {
    fun  Any.print() {
        println(this)
    }

    val lines = Kcsv {
        select("PROFILE ID","TITLE")
        path("/Users/maxfarsikov/Desktop/kemp-bookings.2018-11-01")
        tableName(TableNameStrategy.FILE_NAME)
        trim()
        logger(Level.OFF)
        timed()
    }.rows
        .map { (profileId, title) -> "('$profileId', '$title'),"}
        .distinct()
        .joinToString(separator = "\n")



    File("/Users/maxfarsikov/Desktop/kemp-bookings.2018-11-01/res.txt").writeText("""
create table title (
                       profileId varchar(255),
                       title varchar(255)
);
insert into title values
$lines
    """.trimIndent())

}
