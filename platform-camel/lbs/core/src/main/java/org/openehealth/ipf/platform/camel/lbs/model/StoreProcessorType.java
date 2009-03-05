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
package org.openehealth.ipf.platform.camel.lbs.model;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Processor;
import org.apache.camel.model.OutputType;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.platform.camel.lbs.process.ResourceHandler;
import org.openehealth.ipf.platform.camel.lbs.process.StoreProcessor;

/**
 * Processor type for {@link StoreProcessor}
 * <p>
 * This processor type provides the fluent API configuration of the processor.
 * @author Jens Riemschneider
 */
public class StoreProcessorType extends OutputType<ProcessorType> {

    private List<ProcessorType<?>> outputs = new ArrayList<ProcessorType<?>>();
    
    private String resourceHandlersBeanName;
    
    /**
     * Sets the {@link ResourceHandler} of the processor.
     * A resource handler enables the processor to handle a specific type of
     * endpoint exchange. The handler contains the actual strategy for 
     * integrating resources from specific message type (e.g. an HTTP message).
     * <p>
     * When calling this method or {@link #with(ResourceHandler)} multiple  
     * times the result is undefined.  
     * <p>
     * This method or {@link #with(ResourceHandler)} must be called at 
     * least once. Otherwise an {@link IllegalArgumentException} is thrown when 
     * creating the route.
     * <p>
     * This method can be called multiple times to add multiple handlers.
     * @param handlerBeanName
     *          the bean name of the handler for integrating resources
     * @return this type instance for usage with a fluent API
     */
    public StoreProcessorType with(String resourceHandlersBeanName) {
        notNull(resourceHandlersBeanName, "resourceHandlersBeanName cannot be null");
        this.resourceHandlersBeanName = resourceHandlersBeanName;
        return this;
    }
    
    /* (non-Javadoc)
     * @see org.apache.camel.model.ProcessorType#createProcessor(org.apache.camel.spi.RouteContext)
     */
    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        StoreProcessor storer = new StoreProcessor();

        storer.setProcessor(routeContext.createProcessor(this));

        if (resourceHandlersBeanName == null) {
            throw new IllegalStateException("resource handlers must be set via with()");
        }
        
        storer.with(routeContext.lookup(resourceHandlersBeanName, List.class));
        
        return storer;
    }
    
    /* (non-Javadoc)
     * @see org.apache.camel.model.ProcessorType#getOutputs()
     */
    @Override
    public List getOutputs() {
        return outputs;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{%1$s: resourceHandlersBeanName=%2$s}",
                getClass().getSimpleName(), resourceHandlersBeanName);
    }
}