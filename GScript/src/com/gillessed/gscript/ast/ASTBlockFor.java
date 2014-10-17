package com.gillessed.gscript.ast;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.TokenType;

public class ASTBlockFor extends ASTStatement {

    private ASTBlockForHeader header;
    private List<ASTStatement> statements;
    public ASTBlockFor(List<AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        AbstractSyntaxTree token;
        header = (ASTBlockForHeader)tokens.get(0);
        int index = 2;
        token = tokens.get(index);
        statements = new ArrayList<>();
        while(!token.getParseType().isSubtypeOf(TokenType.RSQUIG)) {
            statements.add((ASTStatement)tokens.get(index));
            index++;
            token = tokens.get(index);
        }
    }
    
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        Environment current = env.push();
        header.getStart().run(env, function);
        GObject conditionTest = header.getCondition().run(current, function);
        boolean value = (boolean)(conditionTest.getValue());
        while(value) {
            for(ASTStatement statement : statements) {
                statement.run(current, function);
                if(function.isFinished()) {
                    return new GObject(null, Type.VOID);
                }
            }
            header.getLoopFunction().run(current, function);
            conditionTest = header.getCondition().run(current, function);
            value = (boolean)(conditionTest.getValue());
        }
        return new GObject(null, Type.VOID);
    }
    
}
