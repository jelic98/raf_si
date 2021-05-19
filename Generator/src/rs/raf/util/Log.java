package rs.raf.util;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    public static void print(String message) {
        log(System.out, message);
    }

    public static void error(String message) {
        log(System.err, message);
    }

    private static void log(PrintStream stream, String message) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();

        stream.println(df.format(now) + " - " + message);
    }
}
