package com.gillessed.gscript.ast;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.gscript.Token;
import com.gillessed.gscript.TokenType;

public class ASTFunctionHeader extends AbstractSyntaxTree {

    private String name;
    private List<String> arguments;
    
    public ASTFunctionHeader(String name, List<String> arguments) {
        super(0);
        this.name = name;
        this.arguments = arguments;
    }
    
    public ASTFunctionHeader(List<? extends AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        name = ((Token)tokens.get(1)).getValue();
        
        arguments = new ArrayList<>();
        
        int index = 3;
        while(index < tokens.size() && tokens.get(index).getParseType().isSubtypeOf(TokenType.IDENTIFIER)) {
            arguments.add(((Token)tokens.get(index)).getValue());
            index += 2;
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
