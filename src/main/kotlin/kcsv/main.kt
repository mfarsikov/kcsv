package kcsv

import java.io.File
import java.util.logging.Level

fun main(args: Array<String>) {

    val table = Kcsv {
        select( Kcsv.ROW_NUM, Kcsv.TABLE_NAME, "FIRST NAME")
        addPath("/Users/maxfarsikov/Downloads/kemp.csv")
        tableNameStrategy(TableNameStrategy.FILE_NAME)
        trim()
        //logger(Level.OFF)
    }

    File("/Users/maxfarsikov/Downloads/kemp2.csv").writeText(table.toCsv())

     Kcsv {
        addPath("/Users/maxfarsikov/Downloads/kemp2.csv")
        tableNameStrategy(TableNameStrategy.FILE_NAME)
        trim()
        //logger(Level.OFF)
    }.print()

/*    val lines = table.rows
            .map { (profileId, title) -> "('$profileId', '$title')," }
            .distinct()
            .joinToString(separator = "\n")



    File("/Users/maxfarsikov/Desktop/kemp-bookings.2018-11-01/res.txt").writeText("""
create table title (
                       profileId varchar(255),
                       title varchar(255)
);
insert into title values
$lines
    """.trimIndent())*/

}
