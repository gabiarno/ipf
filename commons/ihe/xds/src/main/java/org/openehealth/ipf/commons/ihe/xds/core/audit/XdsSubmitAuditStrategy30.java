/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.audit;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;

/**
 * Basis for Strategy pattern implementation for ATNA Auditing
 * in ebXML 3.0-based submission-related XDS transactions.
 *
 * @author Dmytro Rud
 */
abstract public class XdsSubmitAuditStrategy30 extends XdsAuditStrategy<XdsSubmitAuditDataset> {

    /**
     * Constructs an XDS audit strategy.
     *
     * @param serverSide
     *      whether this is a server-side or a client-side strategy.
     * @param allowIncompleteAudit
     *      whether this strategy should allow incomplete audit records
     *      (parameter initially configurable via endpoint URL).
     */
    public XdsSubmitAuditStrategy30(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }


    @Override
    public void enrichDatasetFromRequest(Object pojo, XdsSubmitAuditDataset auditDataset) {
        SubmitObjectsRequest submitObjectsRequest = (SubmitObjectsRequest) pojo;
        EbXMLSubmitObjectsRequest ebXML = new EbXMLSubmitObjectsRequest30(submitObjectsRequest);
        auditDataset.enrichDatasetFromSubmitObjectsRequest(ebXML);
    }


    @Override
    public RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCode(Object pojo) {
        RegistryResponseType response = (RegistryResponseType) pojo;
        EbXMLRegistryResponse ebXML = new EbXMLRegistryResponse30(response);
        return getEventOutcomeCodeFromRegistryResponse(ebXML);
    }


    @Override
    public XdsSubmitAuditDataset createAuditDataset() {
        return new XdsSubmitAuditDataset(isServerSide());
    }
}
