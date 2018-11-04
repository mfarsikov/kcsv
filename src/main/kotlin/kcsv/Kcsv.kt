package kcsv


class Kcsv {

    companion object {
        operator fun invoke(config: IConfig.() -> Unit): Columns {
            val cfg = Config()
            cfg.config()
            return Parser(cfg).parse()

        }

        const val ROW_NUM = "#ROW_NUM"
        const val TABLE_NAME = "#TABLE_NAME"
        const val `*` = "*"
    }

}

enum class TableNameStrategy { FILE_NAME, FILE_NAME_WITH_EXTENSION, FULL_PATH }
