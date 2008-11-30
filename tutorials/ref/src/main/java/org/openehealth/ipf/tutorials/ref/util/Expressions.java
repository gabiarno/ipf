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
package org.openehealth.ipf.tutorials.ref.util;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;

/**
 * @author Martin Krasser
 */
public class Expressions {

    public static Expression<Exchange> orderFilename(final String extension) {
        return filename("order-", extension);
    }
    
    public static Expression<Exchange> errorFilename(final String extension) {
        return filename("error-", extension);
    }
    
    public static Expression<Exchange> filename(final String prefix, final String extension) {
        return new Expression<Exchange>() {
            public Object evaluate(Exchange exchange) {
                ManagedMessage message = new PlatformMessage(exchange);
                Long flowId = message.getFlowId();
                if (flowId == null) {
                    return prefix + UUID.randomUUID() + "." + extension;            
                } else {
                    return prefix + message.getFlowId() + "." + extension;
                }
            }
        };
    }

}