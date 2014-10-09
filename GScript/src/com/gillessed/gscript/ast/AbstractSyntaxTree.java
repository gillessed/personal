package com.gillessed.gscript.ast;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;

public abstract class AbstractSyntaxTree {
	public abstract GObject run(Environment env);
	public ParseType getParseType() {
		return new ParseType(getClass());
	}
}