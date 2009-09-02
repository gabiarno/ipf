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

package org.openehealth.ipf.platform.camel.ihe.xds.iti18.service;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.xds.ports.Iti18PortType;
import org.openehealth.ipf.commons.ihe.xds.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiWebService;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.converters.EbXML30Converters;

/**
 * Service implementation for the IHE ITI-18 transaction (Registry Stored Query).
 * <p>
 * This implementation delegates to a Camel consumer by creating an exchange.
 * 
 * @author Jens Riemschneider
 */
public class Iti18Service extends DefaultItiWebService implements Iti18PortType {
    public AdhocQueryResponse documentRegistryRegistryStoredQuery(AdhocQueryRequest body) {
        Exchange result = process(body);
        if (result.getException() != null) {
            QueryResponse errorResponse = new QueryResponse();
            configureError(errorResponse, result.getException(), ErrorCode.REGISTRY_METADATA_ERROR, ErrorCode.REGISTRY_ERROR);
            return EbXML30Converters.convert(errorResponse);
        }
        
        return Exchanges.resultMessage(result).getBody(AdhocQueryResponse.class);            
    }
}
