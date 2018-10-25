package com.neaterbits.ide.util.scheduling.dependencies;

import java.io.File;
import java.util.List;
import java.util.Objects;

final class FileTarget<TARGET> extends Target<TARGET> {

	private final File file;
	
	public FileTarget(
			Class<TARGET> type,
			File file,
			String description,
			TARGET targetObject,
			List<Prerequisites> prerequisites,
			Action<TARGET> action,
			ActionWithResult<TARGET> actionWithResult) {
		super(type, description, targetObject, prerequisites, action, actionWithResult);

		Objects.requireNonNull(file);
		
		this.file = file;
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
	public String toString() {
		return "FileTarget [file=" + file + "]";
	}
}
