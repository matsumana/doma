/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.util;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

/**
 * @author taedium
 * 
 */
public final class AnnotationValueUtil {

    public static List<String> toStringList(AnnotationValue value) {
        if (value == null) {
            return null;
        }
        final List<String> results = new ArrayList<String>();
        value.accept(new SimpleAnnotationValueVisitor6<Void, Void>() {

            @Override
            public Void visitArray(List<? extends AnnotationValue> values,
                    Void p) {
                for (AnnotationValue value : values) {
                    value.accept(this, p);
                }
                return null;
            }

            @Override
            public Void visitString(String s, Void p) {
                results.add(s);
                return null;
            }

        }, null);
        return results;
    }

    public static Boolean toBoolean(AnnotationValue value) {
        if (value == null) {
            return null;
        }
        return value.accept(new SimpleAnnotationValueVisitor6<Boolean, Void>() {

            @Override
            public Boolean visitBoolean(boolean b, Void p) {
                return b;
            }

        }, null);
    }

    public static Integer toInteger(AnnotationValue value) {
        if (value == null) {
            return null;
        }
        return value.accept(new SimpleAnnotationValueVisitor6<Integer, Void>() {

            @Override
            public Integer visitInt(int i, Void p) {
                return i;
            }

        }, null);
    }

    public static String toString(AnnotationValue value) {
        if (value == null) {
            return null;
        }
        return value.accept(new SimpleAnnotationValueVisitor6<String, Void>() {

            @Override
            public String visitString(String s, Void p) {
                return s;
            }

        }, null);
    }

    public static TypeMirror toTypeMirror(AnnotationValue value) {
        if (value == null) {
            return null;
        }
        return value.accept(
                new SimpleAnnotationValueVisitor6<TypeMirror, Void>() {

                    @Override
                    public TypeMirror visitType(TypeMirror t, Void p) {
                        return t;
                    }

                }, null);
    }

    public static boolean isEqual(Object object, AnnotationValue value) {
        assertNotNull(object);
        if (value == null) {
            return false;
        }
        return object.equals(value.getValue());
    }

}
