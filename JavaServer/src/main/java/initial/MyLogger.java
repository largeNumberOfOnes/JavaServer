package initial;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class MyLogger extends Logger implements Closeable {

    private static MyLogger instance = null;
    private FileHandler fh;

    private static class LogFormatter extends Formatter {
        @Override
        public String format(LogRecord logRecord) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            String date = formatter.format( new Date() );

            String sss = String.format("%s %7s --> %-60s : %s\n",
                    date,
                    logRecord.getLevel().toString(),
                    logRecord.getSourceClassName() + "." + logRecord.getSourceMethodName(),
                    logRecord.getMessage()
            );
            return sss;
        }
    }

    public MyLogger() throws Exception {
        super("MyLog", null);

        if (instance != null) {
            throw new Exception("Error: Instance already exists");
        }

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            fh = new FileHandler("logs/" + formatter.format( new Date() ) + ".log", false);
            addHandler(fh);
            fh.setFormatter(new LogFormatter());
            setUseParentHandlers(false);
            info("Logging start");
            instance = this;
        } catch (SecurityException | IOException e) {
            log(Level.SEVERE, "Error while creating logger", e);
        }
    }

    public static MyLogger getInstance() throws NullPointerException {
        if (instance == null) {
            throw new NullPointerException();
        }
        return instance;
    }

    public void sever(String msg, Exception e) {
//        System.out.println(msg + stackTraceLoggerString(e));
        this.severe(msg + stackTraceLoggerString(e));
    }

    public void warning(String msg, Exception e) {
        this.warning(msg + stackTraceLoggerString(e));
    }

    private static String stackTraceLoggerString(Exception e) {
        String err = "\n\t| %s: %s\n".formatted(e.getClass().getName(), e.getMessage());
        for (var q : e.getStackTrace()) {
            err += "\t| \tat %s\n".formatted(q.toString());
        }
        return err;
    }

    @Override
    public void close() throws IOException {
        fh.close();
    }
}
