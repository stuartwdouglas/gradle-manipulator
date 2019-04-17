package org.jboss.gm.analyzer.alignment;

import java.beans.Transient;

import org.commonjava.maven.atlas.ident.ref.ProjectVersionRef;
import org.commonjava.maven.atlas.ident.ref.SimpleProjectVersionRef;

public interface GAV {

	String getGroup();

	String getName();

	String getVersion();

	@Transient
	default String getIdentifier() {
		return String.format("%s:%s", getGroup(), getName());
	}

	default ProjectVersionRef toProjectVersionRef() {
		return new SimpleProjectVersionRef(getGroup(), getName(), getVersion());
	}

	class Simple implements GAV {
		private final String group;
		private final String name;
		private final String version;

		public Simple(String group, String name, String version) {
			this.group = group;
			this.name = name;
			this.version = version;
		}

		@Override
		public String getGroup() {
			return group;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getVersion() {
			return version;
		}
	}
}
