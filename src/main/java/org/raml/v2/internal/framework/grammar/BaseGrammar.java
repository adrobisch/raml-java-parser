/*
 * Copyright 2013 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.v2.internal.framework.grammar;

import com.google.common.collect.Range;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.raml.v2.internal.framework.grammar.rule.AllOfRule;
import org.raml.v2.internal.framework.grammar.rule.AnyOfRule;
import org.raml.v2.internal.framework.grammar.rule.AnyValueRule;
import org.raml.v2.internal.framework.grammar.rule.ArrayRule;
import org.raml.v2.internal.framework.grammar.rule.BooleanTypeRule;
import org.raml.v2.internal.framework.grammar.rule.ConditionalRule;
import org.raml.v2.internal.framework.grammar.rule.ConditionalRules;
import org.raml.v2.internal.framework.grammar.rule.DefaultValue;
import org.raml.v2.internal.framework.grammar.rule.DiscriminatorRule;
import org.raml.v2.internal.framework.grammar.rule.FieldPresentRule;
import org.raml.v2.internal.framework.grammar.rule.FirstOfRule;
import org.raml.v2.internal.framework.grammar.rule.FloatTypeRule;
import org.raml.v2.internal.framework.grammar.rule.IntegerTypeRule;
import org.raml.v2.internal.framework.grammar.rule.IntegerValueRule;
import org.raml.v2.internal.framework.grammar.rule.KeyValueRule;
import org.raml.v2.internal.framework.grammar.rule.MinLengthRule;
import org.raml.v2.internal.framework.grammar.rule.NegativeRule;
import org.raml.v2.internal.framework.grammar.rule.NullValueRule;
import org.raml.v2.internal.framework.grammar.rule.ObjectRule;
import org.raml.v2.internal.framework.grammar.rule.ParentKeyDefaultValue;
import org.raml.v2.internal.framework.grammar.rule.ReferenceRule;
import org.raml.v2.internal.framework.grammar.rule.RegexValueRule;
import org.raml.v2.internal.framework.grammar.rule.Rule;
import org.raml.v2.internal.framework.grammar.rule.ScalarTypeRule;
import org.raml.v2.internal.framework.grammar.rule.StringTypeRule;
import org.raml.v2.internal.framework.grammar.rule.StringValueRule;

public class BaseGrammar
{

    private GrammarContext context;

    public BaseGrammar()
    {
        this.context = new GrammarContext();
    }

    public ObjectRule objectType()
    {
        return new ObjectRule();
    }

    public ObjectRule objectType(String name)
    {
        final ObjectRule mapping = objectType();
        this.context.registerRule(name, mapping);
        return mapping;
    }

    public DiscriminatorRule whenChildIs(Rule discriminator, Rule then)
    {
        return new DiscriminatorRule(discriminator, then);
    }

    public FieldPresentRule whenPresent(String selector, Rule then)
    {
        return new FieldPresentRule(selector, then);
    }

    public AnyValueRule any()
    {
        return new AnyValueRule();
    }

    public ArrayRule array(Rule of)
    {
        return new ArrayRule(of);
    }

    public IntegerTypeRule integerType()
    {
        return new IntegerTypeRule(null);
    }

    public FloatTypeRule floatType()
    {
        return new FloatTypeRule();
    }

    public Rule numberType()
    {
        return anyOf(integerType(), floatType());
    }

    public IntegerTypeRule range(Range<Integer> range)
    {
        return new IntegerTypeRule(range);
    }

    public IntegerValueRule integer(Integer value)
    {
        return new IntegerValueRule(new BigInteger(value.toString()));
    }

    public KeyValueRule field(Rule keyRule, Rule valueRule)
    {
        return new KeyValueRule(keyRule, optional(valueRule));
    }

    public KeyValueRule fieldWithRequiredValue(Rule keyRule, Rule valueRule)
    {
        return new KeyValueRule(keyRule, valueRule);
    }

    public KeyValueRule requiredField(Rule keyRule, Rule valueRule)
    {
        return new KeyValueRule(keyRule, valueRule).required();
    }

    public ScalarTypeRule scalarType()
    {
        return new ScalarTypeRule();
    }

    public StringTypeRule stringType()
    {
        return new StringTypeRule();
    }

    public BooleanTypeRule booleanType()
    {
        return new BooleanTypeRule();
    }

    public StringValueRule string(String value)
    {
        return new StringValueRule(value);
    }

    public RegexValueRule regex(String pattern)
    {
        return new RegexValueRule(Pattern.compile(pattern));
    }

    public AnyOfRule anyOf(Rule... rules)
    {
        return new AnyOfRule(Arrays.asList(rules));
    }

    public AnyOfRule anyOf(String name, Rule... rules)
    {
        AnyOfRule rule = new AnyOfRule(Arrays.asList(rules));
        context.registerRule(name, rule);
        return rule;
    }

    public AnyOfRule anyOf(List<Rule> rules)
    {
        return new AnyOfRule(rules);
    }

    public AnyOfRule firstOf(Rule... rules)
    {
        return new FirstOfRule(Arrays.asList(rules));
    }

    public NegativeRule not(Rule rule)
    {
        return new NegativeRule(rule);
    }

    public AllOfRule allOf(Rule... rules)
    {
        return new AllOfRule(Arrays.asList(rules));
    }

    public ReferenceRule ref(String name)
    {
        return new ReferenceRule(context, name);
    }

    public AnyOfRule optional(Rule rule)
    {
        return anyOf(rule, nullValue());
    }

    public MinLengthRule minLength(int length)
    {
        return new MinLengthRule(length);
    }

    @Nonnull
    protected NullValueRule nullValue()
    {
        return new NullValueRule();
    }

    public ConditionalRules when(String expr, ConditionalRule... cases)
    {
        return new ConditionalRules(expr, cases);
    }

    public ConditionalRule is(Rule rule)
    {
        return new ConditionalRule(rule);
    }

    public DefaultValue parentKey()
    {
        return new ParentKeyDefaultValue();
    }
}