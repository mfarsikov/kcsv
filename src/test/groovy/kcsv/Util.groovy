package kcsv

class Util {
    static String path(String path){
        getClass().getResource(path).toURI().path
    }
}
