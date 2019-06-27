package com.serotonin.modbus4j.sero.util;


/**
 * <p>Abstract ProgressiveTask class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
abstract public class ProgressiveTask implements Runnable {
    private boolean cancelled = false;
    protected boolean completed = false;
    private ProgressiveTaskListener listener;

    /**
     * <p>Constructor for ProgressiveTask.</p>
     */
    public ProgressiveTask() {
        // no op
    }

    /**
     * <p>Constructor for ProgressiveTask.</p>
     *
     * @param l a {@link com.serotonin.modbus4j.sero.util.ProgressiveTaskListener} object.
     */
    public ProgressiveTask(ProgressiveTaskListener l) {
        listener = l;
    }

    /**
     * <p>cancel.</p>
     */
    public void cancel() {
        cancelled = true;
    }

    /**
     * <p>isCancelled.</p>
     *
     * @return a boolean.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * <p>isCompleted.</p>
     *
     * @return a boolean.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * <p>run.</p>
     */
    public final void run() {
        while (true) {
            if (isCancelled()) {
                declareFinished(true);
                break;
            }

            runImpl();

            if (isCompleted()) {
                declareFinished(false);
                break;
            }
        }
        completed = true;
    }

    /**
     * <p>declareProgress.</p>
     *
     * @param progress a float.
     */
    protected void declareProgress(float progress) {
        ProgressiveTaskListener l = listener;
        if (l != null)
            l.progressUpdate(progress);
    }

    private void declareFinished(boolean cancelled) {
        ProgressiveTaskListener l = listener;
        if (l != null) {
            if (cancelled)
                l.taskCancelled();
            else
                l.taskCompleted();
        }
    }

    /**
     * Implementers of this method MUST return from it occasionally so that the cancelled status can be checked. Each
     * return must leave the class and thread state with the expectation that runImpl will not be called again, while
     * acknowledging the possibility that it will.
     *
     * Implementations SHOULD call the declareProgress method with each runImpl execution such that the listener can be
     * notified.
     *
     * Implementations MUST set the completed field to true when the task is finished.
     */
    abstract protected void runImpl();
}
