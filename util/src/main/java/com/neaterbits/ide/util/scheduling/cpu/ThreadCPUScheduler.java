package com.neaterbits.ide.util.scheduling.cpu;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.neaterbits.ide.util.scheduling.ForwardToCaller;

public final class ThreadCPUScheduler implements CPUScheduler {

	private final ForwardToCaller forwardToCaller;
	
	private final ThreadPoolExecutor executor;
	
	public ThreadCPUScheduler(ForwardToCaller forwardToCaller) {
		this.forwardToCaller = forwardToCaller;
		this.executor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
	}
	
	@Override
	public <T, R> void schedule(T parameter, Function<T, R> callable, BiConsumer<T, R> completion) {

		executor.execute(() -> {
			R result;
			try {
				result = callable.apply(parameter);

				forwardToCaller.forward(() -> completion.accept(parameter, result));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
	}
}
