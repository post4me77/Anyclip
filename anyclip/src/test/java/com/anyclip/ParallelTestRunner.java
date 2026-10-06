package com.anyclip;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;

/** Runs test methods concurrently; JUnit creates a separate test instance for each method. */
public class ParallelTestRunner extends BlockJUnit4ClassRunner {
    public ParallelTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        final int threads;
        try {
            threads = Integer.parseInt(System.getProperty("test.threads", "3"));
            if (threads < 1) {
                throw new NumberFormatException("Must be positive");
            }
        } catch (NumberFormatException e) {
            throw new InitializationError("test.threads must be a positive integer");
        }
        setScheduler(new RunnerScheduler() {
            private final ExecutorService executor = Executors.newFixedThreadPool(threads);
            private final List<Future<?>> tasks = new ArrayList<>();

            @Override
            public void schedule(Runnable childStatement) {
                tasks.add(executor.submit(childStatement));
            }

            @Override
            public void finished() {
                executor.shutdown();
                try {
                    for (Future<?> task : tasks) {
                        task.get();
                    }
                } catch (InterruptedException e) {
                    // Let running tests finish their @After cleanup before returning.
                    executor.shutdownNow();
                    boolean interrupted = true;
                    while (!executor.isTerminated()) {
                        try {
                            executor.awaitTermination(1, TimeUnit.SECONDS);
                        } catch (InterruptedException again) {
                            interrupted = true;
                        }
                    }
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                    throw new RuntimeException("Parallel test run interrupted", e);
                } catch (ExecutionException e) {
                    throw new RuntimeException("Parallel test worker failed", e.getCause());
                }
            }
        });
    }
}
