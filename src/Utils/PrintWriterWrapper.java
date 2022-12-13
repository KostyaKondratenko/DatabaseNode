package Utils;

import java.io.OutputStream;
import java.io.PrintWriter;

public class PrintWriterWrapper {
    private PrintWriter wrapped;

    public PrintWriterWrapper(OutputStream out, boolean autoFlush) {
        wrapped = new PrintWriter(out, autoFlush);
    }

    public void println(String x) {
        wrapped.println(x);
    }
}
