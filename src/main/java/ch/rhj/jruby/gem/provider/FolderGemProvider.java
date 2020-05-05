package ch.rhj.jruby.gem.provider;

import java.nio.file.Path;
import java.util.Optional;

import ch.rhj.io.IO;
import ch.rhj.jruby.gem.Gem;
import ch.rhj.util.Versions;

public class FolderGemProvider implements GemProvider {

	public final static int ORDER = ClassPathGemProvider.ORDER - 1000;

	@Override
	public int order() {

		return ORDER;
	}

	private boolean filter(Path path, String name, String version) {

		String fileName = path.getFileName().toString();

		if (!fileName.startsWith(name + "-"))
			return false;

		if (!fileName.endsWith(".gem"))
			return false;

		String fileVersion = fileName.substring(0, fileName.length() - 4).substring(name.length() + 1);

		if (Versions.compare(fileVersion, version) < 0)
			return false;

		return true;
	}

	@Override
	public Gem gem(String name, String version) {

		Optional<Path> latest = FolderGemProviderConfig //
				.instance().locations().stream() //
				.flatMap(IO::list) //
				.filter(p -> filter(p, name, version)) //
				.findFirst();

		if (latest.isEmpty())
			return null;

		return new Gem(IO.read(latest.get()));
	}
}
