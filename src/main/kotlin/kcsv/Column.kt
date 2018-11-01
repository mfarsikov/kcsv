package kcsv

import java.lang.Integer.max

interface Column {
    val name: String
    val data: List<String>
    val width: Int

    operator fun plus(other: Column): Column {
        if (this.name != other.name) {
            throw Exception("different column names: ${this.name} and ${other.name}")
        }
        return ColumnImmutable(name, this.data + other.data)
    }
}

data class ColumnImmutable(
        override val name: String,
        override val data: List<String>
) : Column {
    override val width: Int by lazy {
        val maxDataLen = data.maxBy { it.length }?.length ?: 0
        max(maxDataLen, name.length)
    }
}

data class ColumnMutable(
        val fromIdx: Int,
        override val name: String
) : Column {
    override val data: MutableList<String> = mutableListOf()
    override val width: Int by lazy {
        val maxDataLen = data.maxBy { it.length }?.length ?: 0
        max(maxDataLen, name.length)
    }
}