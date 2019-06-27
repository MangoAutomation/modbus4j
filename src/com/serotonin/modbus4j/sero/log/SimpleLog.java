package com.serotonin.modbus4j.sero.log;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>SimpleLog class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class SimpleLog {
    private final PrintWriter out;
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss.SSS");
    private final StringBuilder sb = new StringBuilder();
    private final Date date = new Date();

    /**
     * <p>Constructor for SimpleLog.</p>
     */
    public SimpleLog() {
        this(new PrintWriter(System.out));
    }

    /**
     * <p>Constructor for SimpleLog.</p>
     *
     * @param out a {@link java.io.PrintWriter} object.
     */
    public SimpleLog(PrintWriter out) {
        this.out = out;
    }

    /**
     * <p>out.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public void out(String message) {
        out(message, null);
    }

    /**
     * <p>out.</p>
     *
     * @param t a {@link java.lang.Throwable} object.
     */
    public void out(Throwable t) {
        out(null, t);
    }

    /**
     * <p>out.</p>
     *
     * @param o a {@link java.lang.Object} object.
     */
    public void out(Object o) {
        if (o instanceof Throwable)
            out(null, (Throwable) o);
        else if (o == null)
            out(null, null);
        else
            out(o.toString(), null);
    }

    /**
     * <p>close.</p>
     */
    public void close() {
        out.close();
    }

    /**
     * <p>out.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param t a {@link java.lang.Throwable} object.
     */
    public synchronized void out(String message, Throwable t) {
        sb.delete(0, sb.length());
        date.setTime(System.currentTimeMillis());
        sb.append(sdf.format(date)).append(" ");
        if (message != null)
            sb.append(message);
        if (t != null) {
            if (t.getMessage() != null)
                sb.append(" - ").append(t.getMessage());
            out.println(sb.toString());
            t.printStackTrace(out);
        }
        else
            out.println(sb.toString());
        out.flush();
    }
}
