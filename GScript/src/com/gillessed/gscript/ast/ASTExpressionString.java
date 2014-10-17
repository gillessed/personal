package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.StringEscape;
import com.gillessed.gscript.Token;

public class ASTExpressionString extends ASTExpression {

    private Token token;
    private String string;
    
    public ASTExpressionString(List<AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        token = (Token)tokens.get(0);
        String value = token.getValue().substring(1, token.getValue().length() - 1);
        string = StringEscape.stringEscape(value);
    }
    
    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        return new GObject(string, Type.STRING);
    }
}
