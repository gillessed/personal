package com.gillessed.gscript.ast;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.GObject.Type;

public class ASTBlockIfElse extends ASTStatement {

    private ASTBlockIf ifBlock;
    private List<ASTBlockElseIf> elseIfBlocks;
    private ASTBlockElse elseBlock;
    
    public ASTBlockIfElse(List<? extends AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        elseIfBlocks = new ArrayList<>();
        ifBlock = (ASTBlockIf)tokens.get(0);
        int index = 1;
        while(index < tokens.size() && tokens.get(index).getParseType().isSubtypeOf(ASTBlockElseIf.class)) {
            elseIfBlocks.add((ASTBlockElseIf)tokens.get(index));
            index++;
        }
        if(index < tokens.size()) {
            elseBlock = (ASTBlockElse)tokens.get(index);
        }
    }

    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        Environment current = env.push();
        GObject result = ifBlock.getCondition().run(current, function);
        if(result.getType() != Type.BOOL) {
            throw new GScriptException("Non-boolean type for if-condition", getLineNumber());
        }
        boolean ifResult = (Boolean)result.getValue();
        if(ifResult) {
            for(ASTStatement statement : ifBlock.getStatements()) {
                statement.run(env, function);
                if(function.isFinished()) {
                    return GObject.VOID;
                }
            }
        } else {
            ASTBlockElseIf elseIf = null;
            for(ASTBlockElseIf elseIfBlock : elseIfBlocks) {
                result = elseIfBlock.getCondition().run(current, function);
                if(result.getType() != Type.BOOL) {
                    throw new GScriptException("Non-boolean type for if-condition", getLineNumber());
                }
                ifResult = (Boolean)result.getValue();
                if(ifResult) {
                    elseIf = elseIfBlock;
                    break;
                }
            }
            if(elseIf != null) {
                for(ASTStatement statement : elseIf.getStatements()) {
                    statement.run(env, function);
                    if(function.isFinished()) {
                        return GObject.VOID;
                    }
                }
            } else if(elseBlock != null) {
                for(ASTStatement statement : elseBlock.getStatements()) {
                    statement.run(env, function);
                    if(function.isFinished()) {
                        return GObject.VOID;
                    }
                }
            }
        }
        return GObject.VOID;
    }
}
