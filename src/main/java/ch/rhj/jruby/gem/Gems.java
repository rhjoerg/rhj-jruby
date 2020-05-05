package ch.rhj.jruby.gem;

import static ch.rhj.util.Singleton.singleton;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.logging.Logger;

import ch.rhj.jruby.gem.provider.GemProviders;
import ch.rhj.util.Singleton;

public class Gems {

	private static final Singleton<Gems> instance = singleton(Gems::new);

	private final Map<String, SortedSet<Gem>> gems = new TreeMap<>();

	public synchronized void add(Gem gem) {

		String name = gem.specification().name();
		SortedSet<Gem> set = gems.get(name);

		if (set == null) {

			set = new TreeSet<Gem>();
			gems.put(name, set);
		}

		set.add(gem);
	}

	public synchronized boolean contains(String name, String version) {

		if (!gems.containsKey(name))
			return false;

		return gems.get(name).last().specification().version().satisfies(version);
	}

	public synchronized Gem get(String name, String version) {

		if (!contains(name, version))
			return null;

		return gems.get(name).last();
	}

	public synchronized boolean resolve(String name, String version) {

		GemProviders providers = GemProviders.instance();
		Gem gem = get(name, version);

		if (gem == null) {

			gem = providers.gem(name, version);

			if (gem == null) {

				Logger.getLogger("ch.rhj.jruby").info(String.format("cannot resolve '%1$s' version '%2$s'", name, version));
				return false;
			}

			add(gem);
		}

		List<Boolean> results = gem.specification().dependencies() //
				.filter(d -> ":runtime".equals(d.type())) //
				.map(d -> resolve(d.name(), d.minVersion().version())) //
				.collect(toList());

		return !results.contains(false);
	}

	private Map<String, Path> installOrStore(Function<Gem, Path> installOrStore) {

		return gems.entrySet().stream() //
				.map(e -> entry(e.getKey(), e.getValue().last())) //
				.map(e -> entry(e.getKey(), installOrStore.apply(e.getValue()))) //
				.collect(toMap(e -> e.getKey(), e -> e.getValue()));
	}

	public synchronized Map<String, Path> install(Path directory, boolean replace) {

		return installOrStore(g -> g.install(directory, replace));
	}

	public synchronized Map<String, Path> install(boolean replace) {

		return install(GemsConfig.instance().libDirectory(), replace);
	}

	public synchronized Map<String, Path> store(Path directory, boolean replace) {

		return installOrStore(g -> g.store(directory, replace));
	}

	public synchronized Map<String, Path> store(boolean replace) {

		return store(GemsConfig.instance().gemDirectory(), replace);
	}

	public static Gems instance() {

		return instance.get();
	}
}
