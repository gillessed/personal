package com.gillessed.gscript;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.gillessed.gscript.ast.ASTFunction;

public class GObject {
    
    public static GObject VOID = new GObject(null, Type.VOID);
    
    public enum Type {
        VOID,
        BOOL,
        INT,
        FLOAT,
        CHAR,
        STRING,
        LIST,
        FUNCTION,
        CLASS,
        NULL
    }
	private final Object value;
	private final Type type;

	public GObject(Object value, Type type) {
		this.value = value;
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public Type getType() {
		return type;
	}
	
	@Override
	public int hashCode() {
	    int result = 17;
	    result += type.hashCode() * 31;
	    result += value.hashCode() * 31;
	    return result;
	}
	
	@Override
	public boolean equals(Object jobj) {
	    if(!(jobj instanceof GObject)) {
	        return false;
	    }
	    GObject obj = (GObject)jobj;
	    return compare(this, obj);
	}
	
	@Override
	public String toString() {
	    if(value == null) {
	        return null;
	    } else {
	        return value.toString();
	    }
	}
	
	private boolean compare(GObject leftValue, GObject rightValue) {
        boolean result;
        if(leftValue.getType() == Type.INT && rightValue.getType() == Type.INT) {
            BigInteger leftNum = (BigInteger)(leftValue.getValue());
            BigInteger rightNum = (BigInteger)(rightValue.getValue());
            result = leftNum.equals(rightNum);
        } else if(leftValue.getType() == Type.FLOAT && rightValue.getType() == Type.FLOAT) {
            BigDecimal leftNum = (BigDecimal)(leftValue.getValue());
            BigDecimal rightNum = (BigDecimal)(rightValue.getValue());
            result = leftNum.equals(rightNum);
        } else if(leftValue.getType() == Type.BOOL && rightValue.getType() == Type.BOOL) {
            boolean leftNum = (boolean)leftValue.getValue();
            boolean rightNum = (boolean)rightValue.getValue();
            result = leftNum == rightNum;
        } else if(leftValue.getType() == Type.CHAR && rightValue.getType() == Type.CHAR) {
            char leftNum = (char)leftValue.getValue();
            char rightNum = (char)rightValue.getValue();
            result = leftNum == rightNum;
        } else if(leftValue.getType() == Type.STRING && rightValue.getType() == Type.STRING) {
            String leftNum = (String)leftValue.getValue();
            String rightNum = (String)rightValue.getValue();
            result = leftNum.equals(rightNum);
        } else if(leftValue.getType() == Type.CLASS && rightValue.getType() == Type.CLASS) {
            String leftNum = (String)leftValue.getValue();
            String rightNum = (String)rightValue.getValue();
            result = leftNum.equals(rightNum);
        } else if(leftValue.getType() == Type.LIST && rightValue.getType() == Type.LIST) {
            GObject[] leftList = (GObject[])leftValue.getValue();
            GObject[] rightList = (GObject[])rightValue.getValue();
            if(rightList.length != leftList.length) {
                result = false;
            } else {
                result = true;
                for(int i = 0; i < leftList.length; i++) {
                    if(!rightList[i].equals(leftList[i])) {
                        result = false;
                    }
                }
            }
        } else if(leftValue.getType() == Type.FUNCTION && rightValue.getType() == Type.FUNCTION) {
            ASTFunction left = (ASTFunction)leftValue.getValue();
            ASTFunction right = (ASTFunction)rightValue.getValue();
            result = left == right;
        } else {
            result = false;
        }
        return result;
	}
}
