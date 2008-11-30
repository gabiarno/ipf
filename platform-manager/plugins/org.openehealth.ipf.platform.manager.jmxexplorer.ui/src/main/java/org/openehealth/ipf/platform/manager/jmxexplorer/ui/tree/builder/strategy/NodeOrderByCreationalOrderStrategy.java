/*
 * Copyright 2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.builder.strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Orders the tree elements in the order, the ObjectName is created.
 * 
 * @author Mitko Kolev
 */
public class NodeOrderByCreationalOrderStrategy extends
        NodeOrderStrategyAbstract {

    @Override
    public List<String> parseProperties(String propertiesString) {
        List<String> properties = new ArrayList<String>();
        String[] propertyValueArray = propertiesString.split(",");
        for (int i = 0; i < propertyValueArray.length; i++) {
            String property = propertyValueArray[i];
            int index = property.indexOf('=');
            if (index == -1)
                continue;
            String key = property.substring(0, index);
            properties.add(key);
        }
        return properties;
    }
}