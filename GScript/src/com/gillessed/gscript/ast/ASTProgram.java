package com.gillessed.gscript.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GObjectConverter;
import com.gillessed.gscript.GScriptException;

public class ASTProgram extends AbstractSyntaxTree {
    private List<ASTImport> imports;
    private List<ASTInclude> includes;
    private List<ASTFunction> functions;
    private List<ASTStatement> statements;
    
    public ASTProgram(List<? extends AbstractSyntaxTree> tree) {
        super(0);
        imports = new ArrayList<>();
        includes = new ArrayList<>();
        functions = new ArrayList<>();
        statements = new ArrayList<>();
        int index = 2;
        while(index < tree.size()) {
            if(tree.get(index).getParseType().isSubtypeOf(ASTImport.class)) {
                imports.add((ASTImport)tree.get(index));
                index++;
            } else {
                break;
            }
        }
        while(index < tree.size()) {
            if(tree.get(index).getParseType().isSubtypeOf(ASTInclude.class)) {
                includes.add((ASTInclude)tree.get(index));
                index++;
            } else {
                break;
            }
        }
        while(index < tree.size()) {
            if(tree.get(index).getParseType().isSubtypeOf(ASTFunction.class)) {
                functions.add((ASTFunction)tree.get(index));
                index++;
            } else {
                break;
            }
        }
        while(index < tree.size()) {
            if(tree.get(index).getParseType().isSubtypeOf(ASTStatement.class)) {
                statements.add((ASTStatement)tree.get(index));
                index++;
            } else {
                break;
            }
        }
        index += 2;
        if(index != tree.size()) {
            throw new RuntimeException("Something is out of order in your script." +
                    "Imports > Includes > Functions > Statements");
        }
    }
    
    public GObject run(List<Object> arguments) throws GScriptException {
        Environment mainEnv = new Environment();
        
        for(ASTFunction function : functions) {
            mainEnv.setValue(function.getKey().getName(), new GObject(function, Type.FUNCTION), true);
        }
        
        GObject[] convertedArguments = new GObject[arguments.size()];
        for(int i = 0; i < arguments.size(); i++) {
            convertedArguments[i] = GObjectConverter.convertToGObject(arguments.get(i));
        }
        List<GObject> argsList = new ArrayList<GObject>();
        argsList.add(new GObject(convertedArguments, Type.LIST));
        List<String> args = new ArrayList<String>();
        args.add("args");
        
        ASTFunction mainFunction = new ASTFunction("gscript_main", args, statements);
        GObject result = mainFunction.run(mainEnv, argsList);
        return result;
    }

    public List<ASTImport> getImports() {
        return Collections.unmodifiableList(imports);
    }

    public List<ASTInclude> getIncludes() {
        return Collections.unmodifiableList(includes);
    }

    public List<ASTFunction> getFunctions() {
        return Collections.unmodifiableList(functions);
    }
}
