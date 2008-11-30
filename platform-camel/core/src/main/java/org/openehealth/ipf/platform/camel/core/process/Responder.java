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
package org.openehealth.ipf.platform.camel.core.process;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.producerTemplate;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.processor.DelegateProcessor;

/**
 * Implements a response generation process to be used in combination with
 * {@link ProcessorType#intercept(DelegateProcessor)}. For generating a
 * response the incoming {@link Exchange} is sent to an endpoint identified by
 * <code>responseGeneratorUri</code> (set at construction time) that is
 * generating a response message from the original {@link Exchange}. The
 * response is then communicated back to the initiator and the original exchange
 * is forwarded to the next processor as in-only {@link Exchange}.
 * 
 * @author Martin Krasser
 */
public class Responder extends DelegateProcessor {

    private Processor responseGeneratorProcessor;
    
    private String responseGeneratorUri;
    
    /**
     * Creates a new {@link Responder}.
     * 
     * @param responseGeneratorUri
     *            URI of the endpoint that generates the response message for an
     *            exchange.
     */
    public Responder(String responseGeneratorUri) {
        this(null, responseGeneratorUri);
    }
    
    /**
     * Creates a new {@link Responder}.
     * 
     * @param responseGeneratorProcessor
     *            processor that generates the response message for an exchange.
     */
    public Responder(Processor responseGeneratorProcessor) {
        this(responseGeneratorProcessor, null);
    }
    
    /**
     * Creates a new {@link Responder}.
     * 
     * @param responseGeneratorUri
     *            URI of the endpoint that generates the response message for an
     *            exchange.
     * @param responseGeneratorProcessor
     *            processor that generates the response message for an exchange.
     */
    private Responder(Processor responseGeneratorProcessor, String responseGeneratorUri) {
        this.responseGeneratorProcessor = responseGeneratorProcessor;
        this.responseGeneratorUri = responseGeneratorUri;
    }
    
    /**
     * Sends the incoming {@link Exchange} to an endpoint identified by
     * <code>responseGeneratorUri</code> (set at construction time) that is
     * generating a response message from the original {@link Exchange}. The
     * response is then communicated back to the initiator and the original
     * exchange is forwarded to the next processor as in-only {@link Exchange}.
     * 
     * @param exchange original exchange.
     */
    @Override
    protected void processNext(Exchange exchange) throws Exception {
        Exchange serviceExchange = null;
        
        // Create delegate exchange before initial exchange is processed
        Exchange delegateExchange = createDelegateExchange(exchange);
        
        if (responseGeneratorUri != null) {
            // communication with response service via template
            serviceExchange = producerTemplate(exchange).send(responseGeneratorUri, exchange.copy());
        } else {
            // communication with response service via processor
            serviceExchange = exchange.copy();
            responseGeneratorProcessor.process(serviceExchange);
        }
        
        // copy service exchange over to original exchange
        // (sends response back to exchange initiator)
        exchange.copyFrom(serviceExchange);
        
        if (process(exchange, serviceExchange)) {
            super.processNext(delegateExchange);
        }
        
    }

    /**
     * Processes the <code>original</code> exchange and the
     * <code>response</code> exchange (returned from endpoint identified by
     * <code>responseGeneratorUri</code>) and returns a decision whether to
     * continue processing. The default implementation always returns
     * <code>true</code>. This method is intended to be overwritten by
     * subclasses.
     * 
     * @param original
     *            original message exchange.
     * @param response
     *            response message exchange.
     * @return <code>true</code> if processing shall continue,
     *         <code>false</code> otherwise.
     */
    protected boolean process(Exchange original, Exchange response) {
        return true;
    }
    
    private Exchange createDelegateExchange(Exchange source) {
        DefaultExchange result = new DefaultExchange(source.getContext());
        result.getIn().copyFrom(source.getIn());
        return result;
    }
    
}