package com.gillessed.advancedmath.function.parser;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;
import com.gillessed.advancedmath.function.field.FieldAddition;
import com.gillessed.advancedmath.function.field.FieldConstant;
import com.gillessed.advancedmath.function.field.FieldDivision;
import com.gillessed.advancedmath.function.field.FieldExponentiation;
import com.gillessed.advancedmath.function.field.FieldFunction;
import com.gillessed.advancedmath.function.field.FieldMultiplication;
import com.gillessed.advancedmath.function.field.FieldParenthetical;
import com.gillessed.advancedmath.function.field.FieldSubtraction;
import com.gillessed.advancedmath.function.field.FieldVariable;
import com.gillessed.advancedmath.utils.CharUtils;


public class FieldFunctionParser<E extends FieldElement> {

private final String expression;
	private final Field<E> field;
	public FieldFunctionParser(Field<E> field, String expression) {
		this.field = field;
		this.expression = expression;
	}
	public FieldFunction<E> parseFunction(FieldFunction<E> parent) {
		int index = CharUtils.topLevelIndexOf(expression, '+');
		if(index >= 0) {
			return new FieldAddition<E>(field, expression, parent);
		}
		index = CharUtils.topLevelIndexOf(expression, '-');
		if(index >= 0) {
			return new FieldSubtraction<E>(field, expression, parent);
		}
		index = CharUtils.topLevelIndexOf(expression, '*');
		if(index >= 0) {
			return new FieldMultiplication<E>(field, expression, parent);
		}
		index = CharUtils.topLevelIndexOf(expression, '/');
		if(index >= 0) {
			return new FieldDivision<E>(field, expression, parent);
		}
		index = CharUtils.topLevelIndexOf(expression, '^');
		if(index >= 0) {
			return new FieldExponentiation<E>(field, expression, parent);
		}
		int firstParen = expression.indexOf('(');
		if(firstParen >= 0) {
			if(firstParen == 0) {
				return new FieldParenthetical<E>(field, expression.substring(1, expression.length() - 1), parent, 1);
			} else {
				String name = expression.substring(0, firstParen);
				return field.getFunction(name, field, expression.substring(firstParen + 1, expression.length() - 1), parent);
			}
		} else {
			if(field.belongsToField(expression)) {
				return new FieldConstant<E>(field, expression, field.parseString(expression), parent);
			} else {
				return new FieldVariable<E>(field, expression, parent);
			}
		}
	}
	public List<FieldFunction<E>> splitFunction(FieldFunction<E> parent) {
		int index = CharUtils.topLevelIndexOf(expression, '+');
		if(index >= 0) {
			return split(parent, index);
		}
		index = CharUtils.topLevelIndexOf(expression, '-');
		if(index >= 0) {
			return split(parent, index);
		}
		index = CharUtils.topLevelIndexOf(expression, '*');
		if(index >= 0) {
			return split(parent, index);
		}
		index = CharUtils.topLevelIndexOf(expression, '/');
		if(index >= 0) {
			return split(parent, index);
		}
		index = CharUtils.topLevelIndexOf(expression, '^');
		if(index >= 0) {
			return split(parent, index);
		}
		int firstParen = expression.indexOf('(');
		String unParenExpr = expression.substring(firstParen + 1, expression.length() - 1);
		List<FieldFunction<E>> subExpressions = new ArrayList<FieldFunction<E>>();
		index = CharUtils.topLevelIndexOf(unParenExpr, ',');
		if(index >= 0) {
			while (index >= 0) {
				String funcExpr = expression.substring(0,index);
				FieldFunctionParser<E> pp = new FieldFunctionParser<E>(field, funcExpr);
				FieldFunction<E> parenFunc = pp.parseFunction(parent);
				subExpressions.add(parenFunc);
				unParenExpr = unParenExpr.substring(index + 1,unParenExpr.length());
				index = CharUtils.topLevelIndexOf(unParenExpr, ',');
			}
		}
		FieldFunctionParser<E> pp = new FieldFunctionParser<E>(field, unParenExpr);
		FieldFunction<E> parenFunc = pp.parseFunction(parent);
		subExpressions.add(parenFunc);
		return subExpressions;
	}
	public List<FieldFunction<E>> splitParenthetical(FieldFunction<E> parent) {
		String unParenExpr = expression;
		List<FieldFunction<E>> subExpressions = new ArrayList<FieldFunction<E>>();
		int index = CharUtils.topLevelIndexOf(unParenExpr, ',');
		if(index >= 0) {
			while (index >= 0) {
				String funcExpr = expression.substring(0,index);
				FieldFunctionParser<E> pp = new FieldFunctionParser<E>(field, funcExpr);
				FieldFunction<E> parenFunc = pp.parseFunction(parent);
				subExpressions.add(parenFunc);
				unParenExpr = unParenExpr.substring(index + 1,unParenExpr.length());
				index = CharUtils.topLevelIndexOf(unParenExpr, ',');
			}
		}
		FieldFunctionParser<E> pp = new FieldFunctionParser<E>(field, unParenExpr);
		FieldFunction<E> parenFunc = pp.parseFunction(parent);
		subExpressions.add(parenFunc);
		return subExpressions;
	}
	public List<FieldFunction<E>> split(FieldFunction<E> parent, int operatorIndex) {
		List<FieldFunction<E>> functions = new ArrayList<FieldFunction<E>>();
		String expr1 = expression.substring(0,operatorIndex);
		FieldFunctionParser<E> pp1 = new FieldFunctionParser<E>(field, expr1);
		functions.add(pp1.parseFunction(parent));
		String expr2 = expression.substring(operatorIndex + 1,expression.length());
		FieldFunctionParser<E> pp2 = new FieldFunctionParser<E>(field, expr2);
		functions.add(pp2.parseFunction(parent));
		return functions;
	}
}
