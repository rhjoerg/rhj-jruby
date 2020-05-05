package ch.rhj.jruby;

import org.jruby.Ruby;

public class RubyBuilder {

	public Ruby build() {

		return Ruby.newInstance();
	}

	public static RubyBuilder rubyBuilder() {

		return new RubyBuilder();
	}
}
