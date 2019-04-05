package com.neaterbits.ide.util.dependencyresolution;

import java.io.File;
import java.util.List;
import java.util.Objects;

import com.neaterbits.structuredlog.binary.logging.LogContext;

final class FileTarget<TARGET> extends Target<TARGET> {

	private static String getLogIdentifier(File file) {
		return file.getPath();
	}
	
	private static String getLogLocalIdentifier(File file) {
		return file.getName();
	}
	
	private final File file;
	
	public FileTarget(
			LogContext logContext,
			Class<TARGET> type,
			File file,
			String description,
			TARGET targetObject,
			List<Prerequisites> prerequisites,
			Action<TARGET> action,
			ActionWithResult<TARGET> actionWithResult,
			FileTargetSpec<?, TARGET, ?> targetSpec) {
		
		super(
				logContext,
				getLogIdentifier(file),
				getLogLocalIdentifier(file),
				type,
				description,
				targetObject,
				prerequisites,
				action,
				actionWithResult,
				targetSpec);

		Objects.requireNonNull(file);
		
		this.file = file;
		
		if (targetSpec.getFile() == null) {
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public String getLogIdentifier() {
		return getLogIdentifier(file);
	}
	
	@Override
	public String getLogLocalIdentifier() {
		return getLogLocalIdentifier(file);
	}

	@Override
	String getDebugString() {
		return file.getName();
	}

	File getFile() {
		return file;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileTarget<?> other = (FileTarget<?>) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}

	@Override
	public String targetSimpleLogString() {
		return file.getName();
	}

	@Override
	public String targetToLogString() {
		return file.getPath();
	}

	@Override
	public String toString() {
		return "FileTarget [file=" + file + "]";
	}
}
