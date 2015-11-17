package jaxb.directdebit;

import java.util.Calendar;

/**
 *
 * @author paweldudek
 * 
 * !!! this can't be used in multithreaded environment
 */
public class UniqueId {

    /**
     * place to store count
     */
    private volatile long theCount;
    
    private static UniqueId me;
    
    public static UniqueId instance() {
        if (me == null) {
            me = new UniqueId();
        } 
        return me;
    }

    private UniqueId() {
        Calendar now = Calendar.getInstance();
        theCount = now.getTimeInMillis();
    }

    /**
     * gets the next value of the counter. If you call it frequently, it will use the incremented count, but if time
     * elapses, it uses the current time
     *
     * @return the next value of the timer
     */
    private synchronized Long next() {
        return next(0);
    }

    /**
     * gets the next value of the counter. If you call it frequently, it will use the incremented count, but if time
     * elapses, it uses the current time
     *
     * @param delay millisecond increment
     * @return the next value of the timer
     */
    private synchronized Long next(int delay) {
        Calendar now = Calendar.getInstance();
        long tempTime = now.getTimeInMillis() + delay;
        theCount++;
        if (theCount < tempTime) {
            theCount = tempTime;
        }
        return theCount;
    }

    public String nextId() {
        return Long.toString(next());
    }
}