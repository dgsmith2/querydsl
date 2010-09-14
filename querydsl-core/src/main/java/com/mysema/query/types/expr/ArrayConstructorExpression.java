/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Visitor;

/**
 * ArrayConstructorExpression extends {@link ConstructorExpression} to represent array initializers
 *
 * @author tiwe
 *
 * @param <D> component type
 */
public class ArrayConstructorExpression<D> extends SimpleExpression<D[]> implements FactoryExpression<D[]> {

    private static final long serialVersionUID = 8667880104290226505L;

    private final Class<D> elementType;
    
    private final List<Expression<?>> args;

    @SuppressWarnings("unchecked")
    public ArrayConstructorExpression(Expression<?>... args) {
        this((Class)Object[].class, (Expression[])args);
    }

    @SuppressWarnings("unchecked")
    public ArrayConstructorExpression(Class<D[]> type, Expression<D>... args) {
        super(type);
        this.elementType = (Class<D>) Assert.notNull(type.getComponentType(),"componentType");
        this.args = Arrays.<Expression<?>>asList(args);
    }

    public final Class<D> getElementType() {
        return elementType;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public D[] newInstance(Object... args){
        if (args.getClass().getComponentType().equals(elementType)){
            return (D[])args;
        }else{
            D[] rv = (D[]) Array.newInstance(elementType, args.length);
            System.arraycopy(args, 0, rv, 0, args.length);
            return rv;
        }
    }


    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }else if (obj instanceof ArrayConstructorExpression<?>){
            ArrayConstructorExpression<?> c = (ArrayConstructorExpression<?>)obj;
            return args.equals(c.args) && getType().equals(c.getType());
        }else{
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return getType().hashCode();
    }
    
}