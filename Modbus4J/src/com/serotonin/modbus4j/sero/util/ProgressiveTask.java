package com.serotonin.modbus4j.sero.util;


/**
 * @author Matthew Lohbihler
 */
abstract public class ProgressiveTask implements Runnable {
    private boolean cancelled = false;
    protected boolean completed = false;
    private ProgressiveTaskListener listener;

    public ProgressiveTask() {
        // no op
    }

    public ProgressiveTask(ProgressiveTaskListener l) {
        listener = l;
    }

    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isCompleted() {
        return completed;
    }

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
