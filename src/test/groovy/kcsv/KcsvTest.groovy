package kcsv

import spock.lang.Specification

import static kcsv.Util.path


class KcsvTest extends Specification {

    def "smoke test"() {
        def config = new Config()
        config.addPath(path('/single-line.csv'))

        def parser = new Parser(config)

        when:
        def table = parser.parse()

        then:
        table.rowsCount == 1
    }

    def "table toString"() {
        def config = new Config()
        config.addPath(path('/single-line.csv'))
        config.trim(true)

        def parser = new Parser(config)
        def table = parser.parse()

        expect:
        table.toString() ==
                """|FIELD_1           FIELD_2
                   |value 1 long data value 2""".stripMargin()


    }

    def "table toCsv"() {
        def config = new Config()
        config.addPath(path('/single-line.csv'))
        config.trim(true)

        def parser = new Parser(config)
        def table = parser.parse()

        expect:
        table.toCsv() ==
                """|FIELD_1,FIELD_2
                   |value 1 long data,value 2""".stripMargin()


    }
}
