package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

class Collector {
	
	private static final Boolean DEBUG = false;
	
	/*
	private static Prerequisites findOnlyPrerequisitesWithCollect(Target<?> target) {

		Prerequisites withCollect = null;
		
		for (Prerequisites prerequisites : target.getPrerequisites()) {
			
			if (prerequisites.getCollect() != null) {
				
				if (withCollect != null) {
					throw new IllegalStateException("Multiple prerequisites with collect for " + target + " " + target.getPrerequisites());
				}
				
				withCollect = prerequisites;
			}
		}


		return withCollect;
	}
	*/
	
	private static CollectedTargetObjects getCollectedTargetsFromSub(Prerequisites withCollect, ExecutorState targetState) {

		final Set<CollectedTargetObject> targetObjects = new HashSet<>(withCollect.getPrerequisites().size());
		
		for (Prerequisite<?> prerequisite : withCollect.getPrerequisites()) {

			final Target<?> subTarget = prerequisite.getSubTarget();

			if (subTarget != null) {
				if (subTarget.getTargetObject() != null) {
					targetObjects.add(new CollectedTargetObject(subTarget.getTargetObject()));
				}
			}
		}
		
		return new CollectedTargetObjects(targetObjects);
	}
	
	private static CollectedProduct productFromCollectedSubTargets(
			TargetExecutionContext<?> context,
			Target<?> target,
			Prerequisites withCollect,
			CollectedTargetObjects subTargetObjects) {
	
		if (!target.getPrerequisites().contains(withCollect)) {
			throw new IllegalArgumentException();
		}
		
		final CollectSubTargets<?> collect = withCollect.getSpec().getCollectSubTargets();
		final Object collectTargetObject;
		final CollectedProduct collected;
		
		if (withCollect.isRecursiveBuild()) {
			
			final Target<?> topOfRecursionTarget = Target.findRecursionTop(withCollect);
			
			collectTargetObject = topOfRecursionTarget.getTargetObject();
	
			context.state.addToRecursiveTargetCollected(topOfRecursionTarget, subTargetObjects);
	
			if (target.isTopOfRecursion()) {
				
				if (target != topOfRecursionTarget) {
					throw new IllegalStateException();
				}
				
				collected = collect.collect(collectTargetObject, subTargetObjects);
			}
			else {
				collected = null;
			}
		}
		else {
			collectTargetObject = withCollect.getFromTarget().getTargetObject();
			collected = collect.collect(collectTargetObject, subTargetObjects);
		}
		
		return collected;
	}
	
	private static <CONTEXT extends TaskContext> void collectProductsFromSubTargetsOf(TargetExecutionContext<CONTEXT> context, Target<?> target) {

		// final Prerequisites withCollect = findOnlyPrerequisitesWithCollect(target);

		// if (withCollect != null) {
		
		for (Prerequisites prerequisites : target.getPrerequisites()) {

			if (prerequisites.getSpec().getCollectSubTargets() != null) {

				if (DEBUG) {
					System.out.println("-- collect computed targets for " + target.getTargetObject());
				}

				final CollectedTargetObjects subTargetObjects = getCollectedTargetsFromSub(prerequisites, context.state);
				
				if (DEBUG) {
					System.out.println("-- collected targets "  + subTargetObjects);
				}
				
				final CollectedProduct collected = productFromCollectedSubTargets(context, target, prerequisites, subTargetObjects);
				
				if (collected != null) {
					
					if (context.logger != null) {
						context.logger.onCollectTargetObjects(target, subTargetObjects, collected, context.state);
					}
					
					if (DEBUG) {
						System.out.println("## add collected target product for " + target + " " + collected);
					}
					
					context.state.addCollectedProduct(target, collected);
				}
			}
		}
	}

	private static CollectedProducts getCollectedProductsFromSub(Target<?> target, Prerequisites withCollect, ExecutorState targetState) {

		final Set<CollectedProduct> subProducts = new HashSet<>(withCollect.getPrerequisites().size());
		
		for (Prerequisite<?> prerequisite : withCollect.getPrerequisites()) {

			final Target<?> subTarget = prerequisite.getSubTarget();

			final List<CollectedProduct> targetSubProducts = targetState.getCollectedProducts(subTarget);
			
			if (DEBUG) {
				System.out.println("## collected subproducts for " + target + " from " + subTarget + " " + targetSubProducts);
			}
			
			if (targetSubProducts != null) {
				subProducts.addAll(targetSubProducts);
			}
		}
		
		return new CollectedProducts(subProducts);
	}

	private static CollectedProduct productFromCollectedSubProducts(
			TargetExecutionContext<?> context,
			Target<?> target,
			Prerequisites withCollect,
			CollectedProducts subProducts) {
	
		if (!target.getPrerequisites().contains(withCollect)) {
			throw new IllegalArgumentException();
		}
		
		final CollectSubProducts<?> collect = withCollect.getSpec().getCollectSubProducts();
		final CollectedProduct collected;
		
		if (collect == null) {
			collected = null;
		}
		else {
			
			if (withCollect.isRecursiveBuild()) {
				throw new UnsupportedOperationException();
			}
			else {
				collected = collect.collect(target.getTargetObject(), subProducts);
			}
		}
		
		return collected;
	}

	private static <CONTEXT extends TaskContext> void collectProductsFromSubProductsOf(TargetExecutionContext<CONTEXT> context, Target<?> target) {

		// final Prerequisites withCollect = findOnlyPrerequisitesWithCollect(target);

		// if (withCollect != null) {
		
		for (Prerequisites prerequisites : target.getPrerequisites()) {

			if (prerequisites.getSpec().getCollectSubProducts() != null) {

				if (DEBUG) {
					System.out.println("-- collect computed products for " + " with prerequisites " + prerequisites);
				}

				final CollectedProducts subProducts = getCollectedProductsFromSub(target, prerequisites, context.state);
				
				if (DEBUG) {
					System.out.println("-- collected products for " + target + " " + subProducts);
				}
	
				final CollectedProduct collected = productFromCollectedSubProducts(context, target, prerequisites, subProducts);
				
				if (collected != null) {
					
					if (context.logger != null) {
						context.logger.onCollectProducts(target, subProducts, collected, context.state);
					}
					
					if (DEBUG) {
						System.out.println("## add collected product for subproducts of " + target + " " + collected);
					}
					
					context.state.addCollectedProduct(target, collected);
				}
			}
		}
	}

	static <CONTEXT extends TaskContext> void collectFromSubTargetsAndSubProducts(TargetExecutionContext<CONTEXT> context, Target<?> target) {
		
		collectProductsFromSubTargetsOf(context, target);
		collectProductsFromSubProductsOf(context, target);
	}
	
}
