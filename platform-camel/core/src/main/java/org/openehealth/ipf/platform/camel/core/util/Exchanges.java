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
package org.openehealth.ipf.platform.camel.core.util;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;

/**
 * Utility related to Camel {@link Exchange}s.
 * 
 * @author Martin Krasser
 */
public class Exchanges {

    /**
     * Creates a {@link ProducerTemplate} from the given {@link Exchange}
     * 
     * @param exchange message exchange.
     * @return a producer template.
     */
    public static ProducerTemplate<Exchange> producerTemplate(Exchange exchange) {
        return exchange.getContext().createProducerTemplate();
    }
    
    /**
     * Returns the message where to write results.
     * 
     * @param exchange message exchange.
     * @return result message.
     */
    public static Message resultMessage(Exchange exchange) {
        if (exchange.getPattern().isOutCapable()) {
            return exchange.getOut();
        } else {
            return exchange.getIn();
        }
    }

    /**
     * Returns the message where to write results. This method copies the
     * in-message to the out-message if the exchange is out-capable.
     * 
     * @param exchange message exchange.
     * @return result message.
     */
    public static Message prepareResult(Exchange exchange) {
        Message result = resultMessage(exchange);
        if (exchange.getPattern().isOutCapable()) {
            result.copyFrom(exchange.getIn());
        }
        return result;
    }
    
    /**
     * Returns the message where to write faults.
     * 
     * @param exchange message exchange.
     * @return fault message.
     */
    public static Message faultMessage(Exchange exchange) {
        return exchange.getFault();
    }

    /**
     * Creates a new {@link Exchange} instance from the given
     * <code>exchange</code>. The resulting exchange's pattern is defined by
     * <code>pattern</code>.
     * 
     * @param source
     *            exchange to copy from.
     * @param pattern
     *            exchange pattern to set.
     * @return created exchange.
     */
    public static Exchange createExchange(Exchange source, ExchangePattern pattern) {
        DefaultExchange target = new DefaultExchange(source.getContext());
        target.copyFrom(source);
        target.setPattern(pattern);
        return target;
    }
    
    /**
     * Creates a new {@link Exchange} instance using <code>context</code>.
     * The resulting exchange's pattern is defined by <code>pattern</code>.
     * 
     * @param context
     *            Camel context.
     * @param pattern
     *            exchange pattern.
     * @return created exchange.
     */
    public static Exchange createExchange(CamelContext context, ExchangePattern pattern) {
        DefaultExchange target = new DefaultExchange(context);
        target.setPattern(pattern);
        return target;
    }

    /**
     * Copies the exchange's in-message to the out-message if the exchange
     * pattern is {@link ExchangePattern#InOut}.
     * 
     * @param exchange
     *            message exchange.
     */
    public static void copyInput(Exchange exchange) {
        prepareResult(exchange);
    }
    
    /**
     * Copies the <code>source</code> exchange to <code>target</code> exchange
     * preserving the {@link ExchangePattern} of <code>target</code>.  
     * 
     * @param source source exchange.
     * @param target target exchange.
     * 
     * @see #resultMessage(Exchange)
     * @see #faultMessage(Exchange)
     */
    public static void copyExchange(Exchange source, Exchange target) {
        if (source == target) {
            // no need to copy
            return;
        }
        
        // copy in message
        Message m = source.getIn();
        target.getIn().copyFrom(m);
    
        // copy out message
        m = source.getOut(false);
        if (m != null) {
            // exchange pattern sensitive
            resultMessage(target).copyFrom(m);
        }
        
        // copy fault message
        m = source.getFault(false);
        if (m != null) {
            faultMessage(target).copyFrom(m);
        }
        
        // copy exception
        target.setException(source.getException());
    }

}