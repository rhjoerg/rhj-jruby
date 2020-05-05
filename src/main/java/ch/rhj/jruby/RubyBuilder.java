package ch.rhj.jruby;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;

import ch.rhj.jruby.gem.Gems;

public class RubyBuilder {

	private RubyInstanceConfig config = null;
	private TreeMap<String, String> requirements = new TreeMap<>();

	public RubyInstanceConfig config() {

		return config == null ? new RubyInstanceConfig() : config;
	}

	public RubyBuilder config(RubyInstanceConfig config) {

		this.config = config;
		return this;
	}

	public Map<String, String> requirements() {

		return new TreeMap<String, String>(requirements);
	}

	public RubyBuilder requirements(Map<String, String> requirements) {

		this.requirements = new TreeMap<String, String>(requirements);
		return this;
	}

	public RubyBuilder require(String name, String version) {

		requirements.put(name, version);
		return this;
	}

	public Ruby build() {

		RubyInstanceConfig config = config();
		Gems gems = Gems.instance();

		requirements.forEach(gems::resolve);
		gems.store(false);

		ArrayList<String> loadPaths = new ArrayList<>(config.getLoadPaths());

		gems.install(true).values().stream() //
				.forEach(s -> loadPaths.add(s));

		config.setLoadPaths(loadPaths);

		return Ruby.newInstance(config);
	}

	public static RubyBuilder rubyBuilder() {

		return new RubyBuilder();
	}
}
