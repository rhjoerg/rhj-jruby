package ch.rhj.jruby;

import java.util.Properties;

import ch.rhj.io.Props;

public abstract class AbstractConfig {

	protected final Properties properties;

	protected AbstractConfig(Properties properties) {

		this.properties = properties;
	}

	public Properties properties(String prefix) {

		return Props.subProperties(properties, prefix);
	}
}
