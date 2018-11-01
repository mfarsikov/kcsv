package kcsv

import spock.lang.Specification

import static kcsv.Util.path


class KcsvTest extends Specification {

    def "smoke test"() {
        def config = new Config()
        config.path(path('/single-line.csv'))

        def parser = new Parser(config)

        when:
        def table = parser.parse()

        then:
        table.rowsCount == 1
    }
}
