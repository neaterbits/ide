package com.neaterbits.ide.util.dependencyresolution.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.ide.util.dependencyresolution.executor.logger.TargetExecutorLogState;
import com.neaterbits.ide.util.dependencyresolution.executor.logger.TargetExecutorLogger;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisite;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisites;
import com.neaterbits.ide.util.dependencyresolution.model.Target;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.ActionParameters;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

final class ExecutorState<CONTEXT extends TaskContext> implements ActionParameters<Object>, TargetExecutorLogState {

	private final Map<Target<?>, TargetStateMachine<CONTEXT>> targets;
	private final Map<Object, Target<?>> targetsByTargetObject;

	private final List<TargetStateMachine<CONTEXT>> nonCompletedTargets;

	
	/*
	private final Map<Target<?>, Object> collected;
	private final Map<Class<?>, Object> collectedProducts;
	*/
	
	private final Map<Target<?>, CollectedTargetObjects> recursiveTargetCollected;

	// private final Map<Target<?>, CollectedTargetObject> collectedTargetObjects; 
	private final Map<Target<?>, List<CollectedProduct>> collectedProductObjects;
	
	static <CTX extends TaskContext> ExecutorState<CTX> createFromTargetTree(
			Target<?> rootTarget,
			TargetExecutor targetExecutor,
			TargetExecutorLogger logger) {

		final Set<Target<?>> toExecuteTargets = new HashSet<>();

		toExecuteTargets.add(rootTarget);
		
		getSubTargets(rootTarget, toExecuteTargets);
		
		final ExecutorState<CTX> state = new ExecutorState<>(toExecuteTargets, logger);
		
		return state;
	}
	
	private ExecutorState(Set<Target<?>> toExecuteTargets, TargetExecutorLogger logger) {

		this.targets = toExecuteTargets.stream()
				.collect(Collectors.toMap(Function.identity(), target -> new TargetStateMachine<CONTEXT>(target, logger)));
		
		this.targetsByTargetObject = new HashMap<>(toExecuteTargets.size());
		
		this.nonCompletedTargets = new ArrayList<>(targets.values());
		
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

	public boolean hasUnfinishedTargets() {
		return !nonCompletedTargets.isEmpty();
	}

	boolean hasTarget(Target<?> target) {
		Objects.requireNonNull(target);
		
		return targets.containsKey(target);
	}
	
	void addTargetToExecute(Target<?> target, TargetExecutorLogger logger) {

		Objects.requireNonNull(target);
		
		final Object targetObject = target.getTargetObject();
		
		if (targets.containsKey(target)) {
			throw new IllegalArgumentException();
		}

		if (targetsByTargetObject.containsKey(targetObject)) {
			throw new IllegalStateException();
		}

		final TargetStateMachine<CONTEXT> targetState = new TargetStateMachine<>(target, logger);
		
		targets.put(target, targetState);
		targetsByTargetObject.put(targetObject, target);
		
		nonCompletedTargets.add(targetState);
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

	public Collection<TargetStateMachine<CONTEXT>> getNonCompletedTargets() {
		return Collections.unmodifiableCollection(nonCompletedTargets);
	}

	@Override
	public Set<Target<?>> getCompletedTargets() {
		return targetsInState(Status.SUCCESS);
	}

	@Override
	public Map<Target<?>, Exception> getFailedTargets() {
		
		final Map<Target<?>, Exception> map = new HashMap<>();
		
		for (Map.Entry<Target<?>, TargetStateMachine<CONTEXT>> entry : targets.entrySet()) {
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

	@Override
	public Set<Target<?>> getActionPerformedCollectTargets() {
		return targetsInState(Status.ACTION_PERFORMED_COLLECTING_SUBTARGETS);
	}

	@Override
	public Status getTargetStatus(Target<?> target) {
		return targetState(target).getStatus();
	}
	
	PrerequisiteCompletion getTargetCompletion(Target<?> target) {

		final Status status = getTargetStatus(target);
		
		return status != Status.FAILED
				? new PrerequisiteCompletion(status)
				: new PrerequisiteCompletion(status, targets.get(target).getException());
		
	}
	
	private TargetStateMachine<CONTEXT> targetState(Target<?> target) {
		
		Objects.requireNonNull(target);
		
		return targets.get(target);
	}
	
	void addToRecursiveTargetCollected(Target<?> target, CollectedTargetObjects collected) {
		
		Objects.requireNonNull(target);
		Objects.requireNonNull(collected);
		
		if (!target.isTopOfRecursion()) {
			throw new IllegalArgumentException();
		}
		
		CollectedTargetObjects objects = recursiveTargetCollected.get(target);
		
		// System.out.println("## merge collected " + collected + " with " + objects);
		
		if (objects != null) {
			objects = objects.mergeWith(collected);
		}
		else {
			objects = collected;
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

	void onCompletedTarget(Target<?> target) {

		final TargetStateMachine<CONTEXT> targetState = targetState(target);
		
		if (targetState == null) {
			throw new IllegalStateException();
		}

		nonCompletedTargets.remove(targetState);
		
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

		// System.out.println("## collected products " + targetsByTargetObject + ", returns " + target + " for " + targetObject);
		
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
