package com.gillessed.gscript;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.gillessed.gscript.GObject.Type;

public class GObjectConverter {
    public static GObject convertToGObject(Object obj) {
        if(obj == null) {
            return new GObject(null, Type.NULL);
        } else if(obj.getClass() == Byte.class) {
            return new GObject(BigInteger.valueOf(new Long((byte)obj)), Type.INT);
        } else if(obj.getClass() == Short.class) {
            return new GObject(BigInteger.valueOf(new Long((short)obj)), Type.INT);
        } else if(obj.getClass() == Integer.class) {
            return new GObject(BigInteger.valueOf(new Long((int)obj)), Type.INT);
        } else if(obj.getClass() == Long.class) {
            return new GObject(BigInteger.valueOf((Long)obj), Type.INT);
        } else if(obj.getClass() == Float.class ||
                obj.getClass() == Double.class) {
            return new GObject(BigDecimal.valueOf((double)obj), Type.FLOAT);
        } else if(obj.getClass() == Boolean.class) {
            return new GObject(obj, Type.BOOL);
        } else if(obj.getClass() == String.class) {
            return new GObject(obj, Type.STRING);
        } else if(obj.getClass() == Character.class) {
            return new GObject(obj, Type.CHAR);
        } else if(obj.getClass().isArray()) {
            Object[] input = (Object[])obj;
            GObject[] list = new GObject[input.length];
            for(int i = 0; i < input.length; i++) {
                list[i] = convertToGObject(input[i]);
            }
            return new GObject(list, Type.LIST);
        } else {
            return new GObject(obj, Type.CLASS);
        }
    }
    
    public static Object convertFromGObject(GObject object) {
        switch(object.getType()) {
        case BOOL:
            return (Boolean)object.getValue();
        case CHAR:
            return (Character)object.getValue();
        case CLASS:
            return object.getValue();
        case FLOAT:
            BigDecimal doubleValue = (BigDecimal)object.getValue();
            return doubleValue.doubleValue();
        case FUNCTION:
            return object.getValue();
        case INT:
            BigInteger intValue = (BigInteger)object.getValue();
            return intValue.longValue();
        case LIST:
            GObject[] listValue = (GObject[])object.getValue();
            Object[] normalList = new GObject[listValue.length];
            for(int i = 0; i < listValue.length; i++) {
                normalList[i] = convertFromGObject(listValue[i]);
            }
            return normalList;
        case STRING:
            return (String)object.getValue();
        case VOID:
            return null;
        case NULL:
            return null;
        }
        return null;
    }
}
