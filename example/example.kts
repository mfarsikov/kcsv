
Kcsv {
    select("TRAVEL AGENT NAME ID", Kcsv.ROWNUM)
    path("/Users/maxfarsikov/Downloads/Berlin and Venice YTD 24 June1.csv")
    lineNumber(3,33,333)
    //    skip(5)
    //    limit(10)
    //    filter ("FIELD NAME", { it.isNotEmpty() })
    print()
    trim()
}