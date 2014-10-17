package com.gillessed.gscript.ast;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GScriptException;


public abstract class ASTStatement extends AbstractSyntaxTree {
    public ASTStatement(int lineNumber) {
        super(lineNumber);
    }
    public abstract GObject run(Environment env, ASTFunction function) throws GScriptException;
}