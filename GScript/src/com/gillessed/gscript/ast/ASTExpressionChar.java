package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.StringEscape;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.Token;

public class ASTExpressionChar extends ASTExpression {

    private Token token;
    private char ch;
    
    public ASTExpressionChar(List<AbstractSyntaxTree> tokens) {
        token = (Token)tokens.get(0);
        String value = token.getValue().substring(1, token.getValue().length() - 1);
        String escape = StringEscape.stringEscape(value);
        ch = escape.charAt(0);
    }
    
    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        return new GObject(ch, Type.CHAR);
    }
}
