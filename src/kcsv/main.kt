package kcsv

fun main(args: Array<String>) {
    CSV {
        select(CSV.ROWNUM, "CONFIRMATION NUMBER")
        path("/Users/maxfarsikov/Downloads/Berlin and Venice YTD 24 June1.csv")
/*        skip(5)
        limit(10)*/
        print()
    }
}
