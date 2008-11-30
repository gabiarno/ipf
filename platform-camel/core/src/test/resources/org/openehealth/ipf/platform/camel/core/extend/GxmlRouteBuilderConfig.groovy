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
package org.openehealth.ipf.platform.camel.core.extend

import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig

import org.apache.camel.builder.RouteBuilder

/**
 * @author Martin Krasser
 */
class GxmlRouteBuilderConfig implements RouteBuilderConfig {
    
    void apply(RouteBuilder builder) {
        
        builder.from('direct:input1')
            .unmarshal().gnode()
            .transmogrify { doc -> doc.details.text() }
            .to('mock:output')
        
        builder.from('direct:input2')
            .unmarshal().gpath()
            .transmogrify { doc -> doc.details.text() }
            .to('mock:output')

        builder.from('direct:input3')
            .unmarshal().gnode()
            .marshal().gnode()
            .convertBodyTo(String.class)
            .to('mock:output')
    
    }
    
}