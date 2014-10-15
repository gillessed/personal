package com.gillessed.gscript;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.gillessed.gscript.GObject.Type;

public class GObjectConverter {
    public static GObject convertToGObject(Object obj) {
        if(obj == null) {
            return new GObject(null, Type.NULL);
        } else if(obj.getClass() == Integer.class ||
                obj.getClass() == Long.class ||
                obj.getClass() == Short.class ||
                obj.getClass() == Byte.class) {
            return new GObject(BigInteger.valueOf((long)obj), Type.INT);
        } else if(obj.getClass() == Float.class ||
                obj.getClass() == Double.class) {
            return new GObject(BigDecimal.valueOf((double)obj), Type.FLOAT);
        } else if(obj.getClass() == Boolean.class) {
            return new GObject(obj, Type.BOOL);
        } else if(obj.getClass() == String.class) {
            return new GObject(obj, Type.STRING);
        } else if(obj.getClass() == Character.class) {
            return new GObject(obj, Type.CHAR);
        } else if(obj.getClass() == List.class) {
            @SuppressWarnings("unchecked")
            List<Object> input = (List<Object>)obj;
            List<GObject> list = new ArrayList<>();
            for(Object listObj : input) {
                list.add(convertToGObject(listObj));
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
            @SuppressWarnings("unchecked")
            List<GObject> listValue = (List<GObject>)object.getValue();
            List<Object> normalList = new ArrayList<>();
            for(GObject gobj : listValue) {
                normalList.add(convertFromGObject(gobj));
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
