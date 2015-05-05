package com.aeells;

import org.force66.correlate.RequestCorrelationContext;

import java.util.concurrent.Callable;

public final class ThreadStateWrapper<T> implements Callable<T>
{
    private final Callable<T> actual;

    private final RequestCorrelationContext parentThreadState;

    public ThreadStateWrapper(final Callable<T> actual)
    {
        this.actual = actual;
        // copy whatever state such as MDC
        this.parentThreadState = RequestCorrelationContext.getCurrent();
    }

    @Override
    public T call() throws Exception
    {
        RequestCorrelationContext existingState = RequestCorrelationContext.getCurrent();

        try
        {
            // set the state of this thread to that of its parent
            // this is where the MDC state and other ThreadLocal values can be set
            RequestCorrelationContext.setCurrent(parentThreadState);
            // execute actual Callable with the state of the parent
            return actual.call();
        }
        finally
        {
            // restore this thread back to its original state
            RequestCorrelationContext.setCurrent(existingState);
        }
    }
}
