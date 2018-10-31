package kcsv

data class Column(
    val fromIdx: Int,
    val name: String,
    val data: MutableList<String> = mutableListOf()
)