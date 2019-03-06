package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

final class CollectedProducts {

	private final Set<CollectedProduct> collected;

	CollectedProducts(Set<CollectedProduct> collected) {
		
		Objects.requireNonNull(collected);
		
		this.collected = collected;
	}

	public List<Object> getCollectedObjects() {
		return collected.stream()
				.map(CollectedProduct::getProductObject)
				.collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return collected.toString();
	}

}
