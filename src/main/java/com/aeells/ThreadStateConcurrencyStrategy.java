package com.aeells;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

import java.util.concurrent.Callable;

public final class ThreadStateConcurrencyStrategy extends HystrixConcurrencyStrategy
{
    @Override
    public <T> Callable<T> wrapCallable(final Callable<T> callable)
    {
        return new ThreadStateWrapper<T>(callable);
    }
}
