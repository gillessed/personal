package com.gillessed.gscript.ast;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.FunctionKey;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;

public class ASTFunction extends AbstractSyntaxTree {

    private boolean finished;
    private GObject result;
    
    private List<ASTStatement> statements;
    private ASTFunctionHeader header;
    
    public ASTFunction(String name, List<String> arguments, List<ASTStatement> statements) {
        super(0);
        header = new ASTFunctionHeader(name, arguments);
        this.statements = new ArrayList<>();
        this.statements.addAll(statements);
        
        finished = false;
        result = null;
    }
    
    public ASTFunction(List<? extends AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        header = (ASTFunctionHeader)tokens.get(0);
        statements = new ArrayList<>();
        
        int index = 2;
        while(index < tokens.size() && tokens.get(index).getParseType().isSubtypeOf(ASTStatement.class)) {
            statements.add((ASTStatement)tokens.get(index));
            index++;
        }
        
        finished = false;
        result = null;
    }
    
    public GObject run(Environment env, List<GObject> arguments) throws GScriptException {
        Environment newEnv = env.push();
        List<String> argumentNames = header.getArguments();
        if(argumentNames.size() != arguments.size()) {
            throw new GScriptException("Calling function " + header.getName() + " with a bad number of arguments", getLineNumber());
        }
        for(int i = 0; i < argumentNames.size(); i++) {
            newEnv.setValue(argumentNames.get(i), arguments.get(i), false);
        }
        for(ASTStatement statement : statements) {
            statement.run(newEnv, this);
            if(finished) {
                GObject tempResult = result;
                result = null;
                finished = false;
                return tempResult;
            }
        }
        return new GObject(null, Type.VOID);
    }
    
    public boolean isFinished() {
        return finished;
    }

    public void finished(GObject result) {
        this.result = result;
        finished = true;
    }

    public FunctionKey getKey() {
        return new FunctionKey(header.getName(), header.getArguments().size());
    }
    
}
