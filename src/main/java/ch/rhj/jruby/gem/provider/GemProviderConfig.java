package ch.rhj.jruby.gem.provider;

import static ch.rhj.util.Singleton.singleton;

import ch.rhj.jruby.AbstractConfig;
import ch.rhj.jruby.Config;
import ch.rhj.util.Singleton;

public class GemProviderConfig extends AbstractConfig {

	public final static String PREFIX = "gem.provider";

	private final static Singleton<GemProviderConfig> instance = singleton(GemProviderConfig::new);

	private GemProviderConfig() {

		super(Config.instance().properties(PREFIX));
	}

	public static GemProviderConfig instance() {

		return instance.get();
	}
}
