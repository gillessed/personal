package com.gillessed.gscript.parser;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;

public abstract class ParseNode {
	protected List<ParseNode> children;
	public ParseNode() {
		children = new ArrayList<>();
	}
	
	public void addChild(ParseNode child) {
		children.add(child);
	}
	
	public abstract ParseResultType parse(List<AbstractSyntaxTree> abstractSyntaxTree, int index);
}
