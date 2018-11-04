//DEPS  mfarsikov:kcsv:0.0.3
import com.sun.org.apache.bcel.internal.generic.Select
import kcsv.Columns
import kcsv.Kcsv
import kcsv.TableNameStrategy
import java.util.logging.Level

fun case(description: String, code: () -> Unit) {
    println(description + ":")
    println()
    code()
    println()

    println("======================================")
}

case("Read single CSV file") {
    val columns: Columns = Kcsv {
        addPath("orders/January/orders.csv")
    }
    columns.print()
}

case("Read some spicific fields") {
    Kcsv {
        select("FIRST_NAME", "COMMENT")
        addPath("orders/January/orders.csv")
    }.print()
}

case("Attach file name and row number (from file) to each  record") {
    Kcsv {
        select(Kcsv.ROW_NUM, Kcsv.TABLE_NAME, "FIRST_NAME", "COMMENT")
        addPath("orders/January/orders.csv")
    }.print()
}

case("File name can be configured to include extension and full path") {
    Kcsv {
        select(Kcsv.ROW_NUM, Kcsv.TABLE_NAME, "FIRST_NAME", "COMMENT")
        addPath("orders/January/orders.csv")
        tableNameStrategy(TableNameStrategy.FULL_PATH)
    }.print()
}

case("Read all CSV files in specified and all nested directories") {
    Kcsv {
        select(Kcsv.ROW_NUM, Kcsv.TABLE_NAME, "FIRST_NAME", "COMMENT")
        addPath("orders")
        tableNameStrategy(TableNameStrategy.FULL_PATH)
    }.print()
}

case("`addPath()` can be called several times") {
    Kcsv {
        addPath("orders/January")
        addPath("orders/February")
    }.print()
}

case("If data or field name contains leading or trailing spaces, they can be trimmed") {
    Kcsv {
        select("SPACED_COLUMN")
        addPath("orders/January")
        trim(true)
    }.print()
}

case("Switch log level: OFF, WARNING (default), FINE (prints row caused problem)") {
    Kcsv {
        addPath("orders/January")
        logger(Level.OFF)
    }.print()
}

case("Collumn based") {
    val columns: Columns = Kcsv {
        select("LAST_NAME", "COMMENT")
        addPath("orders/January")
    }
    val commentColumn = columns["COMMENT"]
    println()
    println("Comment column is:")
    println(commentColumn)

    val firstColumn = columns[0] // Index depends on field ordering in select(), in this example it is LAST_NAME
    println()
    println("First column is:")
    println(firstColumn)

    val rows = columns.toRows() // switch to Row-based representation

    val firstRow = rows[0]
    println()
    println("First row is: ")
    println(firstRow)

    val lastNameToComment = rows.map { (lastName, comment) ->  lastName to comment} //row can be decomposed (destructed) to its columns. Ordering depends on select()

}