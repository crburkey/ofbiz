/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.ofbiz.minilang.method.entityops;

import java.util.Map;

import org.ofbiz.base.util.collections.FlexibleMapAccessor;
import org.ofbiz.base.util.string.FlexibleStringExpander;
import org.ofbiz.minilang.MiniLangException;
import org.ofbiz.minilang.MiniLangValidate;
import org.ofbiz.minilang.SimpleMethod;
import org.ofbiz.minilang.method.MethodContext;
import org.ofbiz.minilang.method.MethodOperation;
import org.w3c.dom.Element;

/**
 * Implements the &lt;clear-cache-line&gt; element.
 * 
 * @see <a href="https://cwiki.apache.org/OFBADMIN/mini-language-reference.html#Mini-languageReference-{{%3Cclearcacheline%3E}}">Mini-language Reference</a>
 */
public final class ClearCacheLine extends MethodOperation {

    private final FlexibleStringExpander entityNameFse;
    private final FlexibleMapAccessor<Map<String, ? extends Object>> mapFma;

    public ClearCacheLine(Element element, SimpleMethod simpleMethod) throws MiniLangException {
        super(element, simpleMethod);
        if (MiniLangValidate.validationOn()) {
            MiniLangValidate.attributeNames(simpleMethod, element, "entity-name", "map");
            MiniLangValidate.requiredAttributes(simpleMethod, element, "entity-name");
            MiniLangValidate.expressionAttributes(simpleMethod, element, "map");
            MiniLangValidate.noChildElements(simpleMethod, element);
        }
        entityNameFse = FlexibleStringExpander.getInstance(element.getAttribute("entity-name"));
        mapFma = FlexibleMapAccessor.getInstance(element.getAttribute("map"));
    }

    @Override
    public boolean exec(MethodContext methodContext) throws MiniLangException {
        String entityName = entityNameFse.expandString(methodContext.getEnvMap());
        Map<String, ? extends Object> fieldsMap = mapFma.get(methodContext.getEnvMap());
        if (fieldsMap == null) {
            methodContext.getDelegator().clearCacheLine(entityName);
        } else {
            methodContext.getDelegator().clearCacheLine(entityName, fieldsMap);
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<clear-cache-line ");
        sb.append("entity-name=\"").append(this.entityNameFse).append("\" ");
        if (!this.mapFma.isEmpty()) {
            sb.append("map=\"").append(this.mapFma).append("\" ");
        }
        sb.append("/>");
        return sb.toString();
    }

    /**
     * A factory for the &lt;clear-cache-line&gt; element.
     */
    public static final class ClearCacheLineFactory implements Factory<ClearCacheLine> {
        @Override
        public ClearCacheLine createMethodOperation(Element element, SimpleMethod simpleMethod) throws MiniLangException {
            return new ClearCacheLine(element, simpleMethod);
        }

        @Override
        public String getName() {
            return "clear-cache-line";
        }
    }
}
