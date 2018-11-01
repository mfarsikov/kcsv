package kcsv


class Kcsv {

    companion object {
        operator fun invoke(config: Config.() -> Unit): Table {
            val cfg = Config()
            cfg.config()
            return Parser(cfg).parse()

        }

        const val ROWNUM = "#ROW_NUM"
        const val TABLE_NAME = "#TABLE_NAME"
        const val `*` = "*"
    }

}

enum class Split { SIMPLE, QUOTED }
enum class TableNameStrategy { FILE_NAME, FILE_NAME_WITH_EXTENSION, FULL_PATH }
