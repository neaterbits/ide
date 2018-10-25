package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.io.File;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface SubTargetBuilder<CONTEXT extends TaskContext, TARGET, PREREQUISITES_BUILDER> {

	PREREQUISITES_BUILDER target(String name, Function<TARGET, String> description);

	PREREQUISITES_BUILDER target(Class<TARGET> type, Function<TARGET, String> description);

	PREREQUISITES_BUILDER target(Class<TARGET> type, Function<TARGET, File> file, Function<TARGET, String> description);

	PREREQUISITES_BUILDER target(Class<TARGET> type, String qualifiername, Function<TARGET, String> description);

	<FILE_TARGET> PREREQUISITES_BUILDER target(Class<TARGET> type, Class<FILE_TARGET> fileTargetType, BiFunction<CONTEXT, TARGET, FILE_TARGET> getFileTarget, Function<FILE_TARGET, File> file, Function<TARGET, String> description);

}
