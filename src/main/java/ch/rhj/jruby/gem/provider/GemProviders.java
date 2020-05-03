package ch.rhj.jruby.gem.provider;

import static ch.rhj.util.Singleton.singleton;

import java.util.Arrays;
import java.util.ServiceLoader;

import ch.rhj.jruby.gem.Gem;
import ch.rhj.util.Singleton;

public class GemProviders implements GemProvider {

	private final static Singleton<GemProviders> instance = singleton(GemProviders::new);

	private final GemProvider[] providers;

	private GemProviders() {

		providers = ServiceLoader.load(GemProvider.class).stream().map(p -> p.get()).toArray(GemProvider[]::new);
		Arrays.sort(providers, (a, b) -> Integer.compare(a.order(), b.order()));
	}

	@Override
	public int order() {

		return 0;
	}

	@Override
	public Gem gem(String name, String version) {

		for (GemProvider provider : providers) {

			Gem gem = provider.gem(name, version);

			if (gem != null)
				return gem;
		}

		return null;
	}

	public static GemProviders instance() {

		return instance.get();
	}
}
