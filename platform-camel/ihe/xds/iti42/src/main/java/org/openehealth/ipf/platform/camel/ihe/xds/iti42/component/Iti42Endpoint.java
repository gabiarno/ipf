/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti42.component;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.openehealth.ipf.commons.ihe.xds.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.ItiAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.xds.iti42.audit.Iti42ClientAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.iti42.audit.Iti42ServerAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.iti42.service.Iti42Service;

import java.net.URISyntaxException;

/**
 * The Camel endpoint for the ITI-42 transaction.
 */
public class Iti42Endpoint extends DefaultItiEndpoint {
    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti42Component
     *          the component creating this endpoint.
     * @throws URISyntaxException
     *          if the endpoint URI was not a valid URI.
     */
    public Iti42Endpoint(String endpointUri, String address, Iti42Component iti42Component) throws URISyntaxException {
        super(endpointUri, address, iti42Component);
    }

    public Producer createProducer() throws Exception {
        ItiAuditStrategy auditStrategy = 
            isAudit() ? new Iti42ClientAuditStrategy(isAllowIncompleteAudit()) : null;
        return new Iti42Producer(this, ItiServiceInfo.ITI_42, auditStrategy);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        ItiAuditStrategy auditStrategy = 
            isAudit() ? new Iti42ServerAuditStrategy(isAllowIncompleteAudit()) : null;
        return new DefaultItiConsumer(this, processor, ItiServiceInfo.ITI_42, auditStrategy, Iti42Service.class);
    }
}