package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.ide.util.scheduling.dependencies.builder.ActionParameters;

final class ExecutorState implements ActionParameters<Object>, TargetExecutorLogState {

	private final Map<Target<?>, TargetState> targets;
	private final Map<Object, Target<?>> targetsByTargetObject;
	
	/*
	private final Map<Target<?>, Object> collected;
	private final Map<Class<?>, Object> collectedProducts;
	*/
	
	private final Map<Target<?>, CollectedTargetObjects> recursiveTargetCollected;

	// private final Map<Target<?>, CollectedTargetObject> collectedTargetObjects; 
	private final Map<Target<?>, List<CollectedProduct>> collectedProductObjects;
	
	static ExecutorState createFromTargetTree(Target<?> rootTarget) {

		final Set<Target<?>> toExecuteTargets = new HashSet<>();

		toExecuteTargets.add(rootTarget);
		
		getSubTargets(rootTarget, toExecuteTargets);
		
		final ExecutorState state = new ExecutorState(toExecuteTargets);
		
		return state;
	}
	
	private ExecutorState(Set<Target<?>> toExecuteTargets) {

		this.targets = toExecuteTargets.stream()
				.collect(Collectors.toMap(Function.identity(), target -> new TargetState()));
		
		this.targetsByTargetObject = new HashMap<>(toExecuteTargets.size());
		
		for (Target<?> target : toExecuteTargets) {
			
			final Object targetObject = target.getTargetObject();
			
			if (targetObject == null && !target.isRoot()) {
				throw new IllegalArgumentException("No target object for " + target);
			}
			
			if (targetsByTargetObject.put(targetObject, target) != null) {
				throw new IllegalArgumentException();
			}
		}
		
		/*
		this.collected = new HashMap<>(toExecuteTargets.size());
		this.collectedProducts = new HashMap<>();
		*/
		
		
		this.recursiveTargetCollected = new HashMap<>();
		
		// this.collectedTargetObjects = new HashMap<>();
		this.collectedProductObjects = new HashMap<>();
	}

	private boolean hasTargets(Status status) {
		
		Objects.requireNonNull(status);
	
		return targets.values().stream().anyMatch(state -> state.getStatus() == status);
	}
	
	private int numTargets(Status status) {
		
		Objects.requireNonNull(status);

		return (int)targets.values().stream()
				.filter(state -> state.getStatus() == status)
				.count();
	}

	private int numTargets(Status ... status) {

		if (status.length == 0) {
			throw new IllegalArgumentException();
		}
		
		return (int)targets.values().stream()
				.filter(state -> {
					for (Status st : status) {
						if (state.getStatus() == st) {
							return true;
						}
					}
					
					return false;
				})
				.count();
	}

	boolean hasTarget(Target<?> target) {
		Objects.requireNonNull(target);
		
		return targets.containsKey(target);
	}
	
	boolean hasExecuteTargets() {
		return hasTargets(Status.TO_EXECUTE);
	}
	
	int getNumExecuteTargets() {
		return numTargets(Status.TO_EXECUTE);
	}

	int getNumExecuteOrScheduledTargets() {
		return numTargets(Status.TO_EXECUTE, Status.SCHEDULED);
	}

	boolean hasScheduledTargets() {
		return hasTargets(Status.SCHEDULED);
	}
	
	boolean isCompletedSuccessfully(Target<?> target) {
		
		Objects.requireNonNull(target);
		
		return targets.get(target).getStatus() == Status.SUCCESS;
	}
	
	void addTargetToExecute(Target<?> target) {

		Objects.requireNonNull(target);
		
		final Object targetObject = target.getTargetObject();
		
		if (targets.containsKey(target)) {
			throw new IllegalArgumentException();
		}

		if (targetsByTargetObject.containsKey(targetObject)) {
			throw new IllegalStateException();
		}

		targets.put(target, new TargetState());
		targetsByTargetObject.put(targetObject, target);
	}
	
	private Set<Target<?>> targetsInState(Status status) {

		Objects.requireNonNull(status);
	
		return Collections.unmodifiableSet(targets.entrySet().stream()
				.filter(entry -> entry.getValue().getStatus() == status)
				.map(entry -> entry.getKey())
				.collect(Collectors.toSet()));
	}
	
	@Override
	public Set<Target<?>> getToExecuteTargets() {
		return targetsInState(Status.TO_EXECUTE);
	}
	
	@Override
	public Set<Target<?>> getCompletedTargets() {
		return targetsInState(Status.SUCCESS);
	}

	@Override
	public Map<Target<?>, Exception> getFailedTargets() {
		
		final Map<Target<?>, Exception> map = new HashMap<>();
		
		for (Map.Entry<Target<?>, TargetState> entry : targets.entrySet()) {
			if (entry.getValue().getStatus() == Status.FAILED) {
				map.put(entry.getKey(), entry.getValue().getException());
			}
		}
		
		return map;
	}

	@Override
	public Set<Target<?>> getScheduledTargets() {
		return targetsInState(Status.SCHEDULED);
	}

	Status getTargetCompletionStatus(Target<?> target) {
		return targetState(target).getStatus();
	}
	
	PrerequisiteCompletion getTargetCompletion(Target<?> target) {

		final Status status = getTargetCompletionStatus(target);
		
		return status != Status.FAILED
				? new PrerequisiteCompletion(status)
				: new PrerequisiteCompletion(status, targets.get(target).getException());
		
	}
	
	private TargetState targetState(Target<?> target) {
		
		Objects.requireNonNull(target);
		
		return targets.get(target);
	}
	
	void moveTargetFromToExecuteToScheduled(Target<?> target) {
		
		Objects.requireNonNull(target);
		
		targetState(target).moveTargetFromToExecuteToScheduled();
	}

	void moveTargetFromToExecuteToFailed(Target<?> target) {
		
		Objects.requireNonNull(target);

		targetState(target).moveTargetFromToExecuteToFailed();
	}
	
	void addToRecursiveTargetCollected(Target<?> target, CollectedTargetObjects collected) {
		
		Objects.requireNonNull(target);
		Objects.requireNonNull(collected);
		
		if (!target.isTopOfRecursion()) {
			throw new IllegalArgumentException();
		}
		
		CollectedTargetObjects objects = recursiveTargetCollected.get(target);
		
		if (objects != null) {
			objects = objects.mergeWith(collected);
		}

		recursiveTargetCollected.put(target, objects);
	}
	
	CollectedTargetObjects getRecursiveTargetCollected(Target<?> target) {
		
		Objects.requireNonNull(target);
		
		if (!target.isTopOfRecursion()) {
			throw new IllegalArgumentException();
		}
		
		return recursiveTargetCollected.get(target);
	}

	void onCompletedTarget(Target<?> target, Exception exception) {

		final TargetState targetState = targetState(target);
		
		if (exception == null) {
			targetState.moveTargetFromScheduledToSuccess();
		}
		else {
			targetState.moveTargetFromScheduledToFailed(exception);
		}
	}

	/*
	void addCollectedTargetObject(Target<?> target, CollectedTargetObject collected) {
		
		Objects.requireNonNull(target);
		Objects.requireNonNull(collected);
		
		if (collectedTargetObjects.containsKey(target)) {
			throw new IllegalStateException();
		}
		
		collectedTargetObjects.put(target, collected);
	}
	*/

	void addCollectedProduct(Target<?> target, CollectedProduct collected) {
		
		Objects.requireNonNull(target);
		Objects.requireNonNull(collected);
		
		List<CollectedProduct> list = collectedProductObjects.get(target);
		
		if (list == null) {
			list = new ArrayList<>();
			collectedProductObjects.put(target, list);
		}
		
		list.add(collected);
	}

	/*
	CollectedTargetObject getCollectedTargetObject(Target<?> target) {
		
		Objects.requireNonNull(target);
		
		return collectedTargetObjects.get(target);
	}
	*/

	List<CollectedProduct> getCollectedProducts(Target<?> target) {

		Objects.requireNonNull(target);
		
		final List<CollectedProduct> collectedProducts = collectedProductObjects.get(target);
		
		return collectedProducts;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCollectedProduct(Object targetObject, Class<T> type) {

		Objects.requireNonNull(targetObject);
		Objects.requireNonNull(type);
		
		final Target<?> target = targetsByTargetObject.get(targetObject);
		
		if (target == null) {
			throw new IllegalStateException();
		}
		
		final List<CollectedProduct> collectedProducts = collectedProductObjects.get(target);
		
		return collectedProducts != null
					? (T)collectedProducts.stream()
							.filter(product -> product.getProductType().equals(type))
							.map(CollectedProduct::getProductObject)
							.findFirst()
							.orElse(null)
					: null;
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
