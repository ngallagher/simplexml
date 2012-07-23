package com.yieldbroker.common.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/** 
 * Provides a simple means of using {@link ThreadMXBean} to dump the stack 
 * frames on each of the threads within the service. 
 * 
 * @author Niall Gallagher
 */
@ManagedResource(description="A JMX tool for building a thread dump")
public class ThreadDumper {

	@ManagedOperation(description="Provides a dump of the threads")
	public String dumpThreads() {
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
		long[] threadIds = threadBean.getAllThreadIds();
		ThreadInfo[] threadInfos = threadBean.getThreadInfo(threadIds, Integer.MAX_VALUE);

		return generateDump(threadInfos);
	}

	private String generateDump(ThreadInfo[] threadInfos) {
		StringBuilder builder = new StringBuilder();

		builder.append("<PRE>");
		builder.append("<B>Full Java thread dump</B>");
		builder.append("\n");

		for (ThreadInfo threadInfo : threadInfos) {
			generateDescription(threadInfo, builder);
			generateLockDetails(threadInfo, builder);
			generateStackFrames(threadInfo, builder);
		}
		builder.append("</PRE>");
		return builder.toString();
	}

	private void generateStackFrames(ThreadInfo threadInfo, StringBuilder builder) {
		StackTraceElement[] stackTrace = threadInfo.getStackTrace();
		
		for (StackTraceElement stackTraceElement : stackTrace) {
			builder.append("    at ");
			builder.append(stackTraceElement);
			builder.append("\n");
		}
	}

	private void generateLockDetails(ThreadInfo threadInfo, StringBuilder builder) {
		String lockOwnerName = threadInfo.getLockOwnerName();
		long lockOwnerId = threadInfo.getLockOwnerId();

		if (lockOwnerName != null) {
			builder.append("    owned by ");
			builder.append(lockOwnerName);
			builder.append(" Id=");
			builder.append(lockOwnerId);
			builder.append("\n");
		}
	}

	private void generateDescription(ThreadInfo threadInfo, StringBuilder builder) {
		Thread.State threadState = threadInfo.getThreadState();
		String threadName = threadInfo.getThreadName();
		String lockName = threadInfo.getLockName();
		long threadId = threadInfo.getThreadId();

		builder.append("\n");
		builder.append("<B>");
		builder.append(threadName);
		builder.append("</B> Id=");
		builder.append(threadId);
		builder.append(" in ");
		builder.append(threadState);

		if (lockName != null) {
			builder.append(" on lock=");
			builder.append(lockName);
		}
		if (threadInfo.isSuspended()) {
			builder.append(" (suspended)");
		}
		if (threadInfo.isInNative()) {
			builder.append(" (running in native)");
		}
		builder.append("\n");
	}
}
