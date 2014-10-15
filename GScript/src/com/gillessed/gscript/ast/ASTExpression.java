package com.gillessed.gscript.ast;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GScriptException;

public abstract class ASTExpression extends AbstractSyntaxTree {
    public abstract GObject run(Environment env, ASTFunction funcion) throws GScriptException;
}
