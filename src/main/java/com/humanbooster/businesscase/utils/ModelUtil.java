// Author Leo Peyronnet

package com.humanbooster.businesscase.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
public class ModelUtil {
    public static <T> void copyFields(T src, T dest) {
        // Raise exception if entities are null
        if (src == null) {
            throw new IllegalArgumentException("Error: Argument <src> cannot be null");
        }
        if (dest == null) {
            throw new IllegalArgumentException("Error: Argument <dest> cannot be null");
        }
        
        // Raise exception if objects aren't same type
        if (!src.getClass().equals(dest.getClass())) {
            throw new IllegalArgumentException("Error: <src> Class does not match <dest> Class");
        }

        try {
            // Get Class Fields
            Field[] fields = src.getClass().getDeclaredFields();
            for (Field field : fields) {
                // Ignore immutable Fields
                if (Modifier.isStatic(field.getModifiers()) ||
                        Modifier.isFinal(field.getModifiers())) {
                    continue;
                }                
                // Ignore Primary Key Field
                if (field.getName().equals("id")) {
                    continue;
                }

                field.setAccessible(true);
                Object valeur = field.get(src);
                
                // Ignore null Values
                if (valeur != null || field.getType().isPrimitive()) {
                    field.set(dest,valeur);
                }
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error: During Field Access", e);
        }
    }
}