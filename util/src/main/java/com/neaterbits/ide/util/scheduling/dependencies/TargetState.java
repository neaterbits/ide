package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.ide.util.scheduling.dependencies.builder.ActionParameters;

final class TargetState implements ActionParameters, TargetExecutorLogState {

	private final Set<Target<?>> toExecuteTargets;

	private final Set<Target<?>> scheduledTargets;
	
	private final Set<Target<?>> completedTargets;
	private final Map<Target<?>, Exception> failedTargets;
	
	private final Map<Target<?>, Object> collected;
	private final Map<Class<?>, Object> prerequisites;
	
	static TargetState createFromTargetTree(Target<?> rootTarget) {

		final Set<Target<?>> toExecuteTargets = new HashSet<>();

		toExecuteTargets.add(rootTarget);
		
		getSubTargets(rootTarget, toExecuteTargets);
		
		final TargetState state = new TargetState(toExecuteTargets);
		
		return state;
	}
	
	private TargetState(Set<Target<?>> toExecuteTargets) {
		this.toExecuteTargets = toExecuteTargets;
		
		this.scheduledTargets = new HashSet<>(toExecuteTargets.size());
		
		this.completedTargets = new HashSet<>(toExecuteTargets.size());
		this.failedTargets = new HashMap<>(toExecuteTargets.size());

		this.collected = new HashMap<>(toExecuteTargets.size());
		
		this.prerequisites = new HashMap<>();
	}

	boolean hasExecuteTargets() {
		return !toExecuteTargets.isEmpty();
	}
	
	int getNumExecuteTargets() {
		return toExecuteTargets.size();
	}

	int getNumExecuteOrScheduledTargets() {
		return toExecuteTargets.size() + scheduledTargets.size();
	}

	boolean hasScheduledTargets() {
		return !scheduledTargets.isEmpty();
	}
	
	@Override
	public Set<Target<?>> getToExecuteTargets() {
		return Collections.unmodifiableSet(toExecuteTargets);
	}
	
	@Override
	public Set<Target<?>> getCompletedTargets() {
		return Collections.unmodifiableSet(completedTargets);
	}

	@Override
	public Map<Target<?>, Exception> getFailedTargets() {
		return Collections.unmodifiableMap(failedTargets);
	}

	@Override
	public Set<Target<?>> getScheduledTargets() {
		return Collections.unmodifiableSet(scheduledTargets);
	}

	@Override
	public Map<Target<?>, Object> getCollected() {
		return Collections.unmodifiableMap(collected);
	}

	@Override
	public Map<Class<?>, Object> getPrerequisites() {
		return Collections.unmodifiableMap(prerequisites);
	}

	void moveTargetFromToExecuteToScheduled(Target<?> target) {
		
		Objects.requireNonNull(target);
		
		if (!toExecuteTargets.remove(target)) {
			throw new IllegalStateException();
		}
		
		if (!scheduledTargets.add(target)) {
			throw new IllegalStateException();
		}
	}

	void moveTargetFromToExecuteToFailed(Target<?> target) {
		
		Objects.requireNonNull(target);
		
		if (!toExecuteTargets.remove(target)) {
			throw new IllegalStateException();
		}
		
		if (scheduledTargets.contains(target)) {
			throw new IllegalStateException();
		}
		
		if (completedTargets.contains(target)) {
			throw new IllegalStateException();
		}
		
		if (failedTargets.put(target, null) != null) {
			throw new IllegalStateException();
		}
	}

	void onCompletedTarget(Target<?> target, Exception exception) {
		if (toExecuteTargets.contains(target)) {
			throw new IllegalStateException();
		}

		if (!scheduledTargets.remove(target)) {
			throw new IllegalStateException();
		}
		
		if (exception == null) {
			if (!completedTargets.add(target)) {
				throw new IllegalStateException();
			}
		}
		else {
			if (failedTargets.put(target, exception) != null) {
				throw new IllegalStateException();
			}
		}
	}
	
	void addCollected(Target<?> target, Object collected) {
		
		Objects.requireNonNull(target);
		Objects.requireNonNull(collected);
		
		this.collected.put(target, collected);
		
		prerequisites.put(collected.getClass(), collected);
	}
	
	Object getCollected(Target<?> target) {

		Objects.requireNonNull(target);
		
		return collected.get(target);		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCollected(Class<T> type) {
		
		Objects.requireNonNull(type);
		
		return (T)prerequisites.get(type);
	}

	private static <TARGET> void getSubTargets(Target<TARGET> target, Set<Target<?>> toExecuteTargets) {
		
		for (Prerequisites prerequisites : target.getPrerequisites()) {
			
			for (Prerequisite<?> prerequisite : prerequisites.getPrerequisites()) {

				if (prerequisite.getSubTarget() != null) {

					if (toExecuteTargets.contains(prerequisite.getSubTarget())) {
						// eg external modules are prerequisites via multiple paths
						// throw new IllegalStateException("Already contains " + prerequisite.getSubTarget());
					}
					else {
					
						toExecuteTargets.add(prerequisite.getSubTarget());
						
						getSubTargets(prerequisite.getSubTarget(), toExecuteTargets);
					}
				}
			}
		}
	}
}
