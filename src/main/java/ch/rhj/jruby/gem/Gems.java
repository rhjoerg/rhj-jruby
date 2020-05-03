package ch.rhj.jruby.gem;

import java.nio.file.Path;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Gems {

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

	public synchronized Map<String, Path> install(Path directory) {

		return gems.entrySet().stream() //
				.map(e -> Map.entry(e.getKey(), e.getValue().last())) //
				.map(e -> Map.entry(e.getKey(), e.getValue().install(directory))) //
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}
}
