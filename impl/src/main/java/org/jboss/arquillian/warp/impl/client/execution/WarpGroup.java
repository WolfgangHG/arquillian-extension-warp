/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.warp.impl.client.execution;

import java.util.Collection;

import org.jboss.arquillian.test.spi.TestResult;
import org.jboss.arquillian.test.spi.TestResult.Status;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.RequestObserver;
import org.jboss.arquillian.warp.client.execution.GroupExecutionSpecifier;
import org.jboss.arquillian.warp.client.execution.GroupInspectionSpecifier;
import org.jboss.arquillian.warp.client.filter.Request;
import org.jboss.arquillian.warp.client.result.WarpGroupResult;
import org.jboss.arquillian.warp.impl.shared.RequestPayload;
import org.jboss.arquillian.warp.impl.shared.ResponsePayload;

/**
 * <p>
 * The Warp request group as seen by Warp internally.
 * </p>
 * <p>
 * <p>
 * Extends {@link WarpGroupResult} which is interface exposed to user.
 * </p>
 * <p>
 * <p>
 * The logic of group is as follows:
 * </p>
 * <p>
 * <ul>
 * <li>group is created with given id (if not id is provided, it is generated from zero-based sequence of integers)</li>
 * <li>group can be setup with filter and inspections</li>
 * <li>group can generated {@link RequestPayload}</li>
 * <li>group receives {@link ResponsePayload} to be paired with adequeate {@link RequestPayload}</li>
 * <li>group can determine if all requests has been paired</li>
 * <li>group allows to analyze all associated {@link RequestPayload}s in order to get first non-successful {@link TestResult}</li>
 * <li>group provides interface {@link WarpGroupResult} to be used by final user</li>
 * </ul>
 *
 * @author Lukas Fryc
 */
public interface WarpGroup extends WarpGroupResult, GroupExecutionSpecifier, GroupInspectionSpecifier {

    /**
     * The id of this group
     */
    Object getId();

    /**
     * Returns a filter associated with this group or null if no filter was associated with this group
     */
    RequestObserver getObserver();

    /**
     * Adds inspections to this group
     */
    void addInspections(Inspection... inspections);

    /**
     * Generates {@link RequestPayload} which can be verified on server.
     */
    RequestPayload generateRequestPayload(Request request);

    /**
     * Pushes response payload associated with this group.
     */
    boolean pushResponsePayload(ResponsePayload payload);

    /**
     * Get a first result of verification of this group which isn't {@link Status.PASSED}.
     */
    TestResult getFirstNonSuccessfulResult();

    /**
     * Get a number of requests expected by this group
     *
     * @return number of requests expected by this group
     */
    int getExpectedRequestCount();

    /**
     * Returns the list of all requests which were observed in this group
     */
    Collection<Request> getAllRequests();

    /**
     * Returns the list of requests which were observed in this group but no response came back from the server.
     */
    Collection<Request> getRequestsWithoutResponse();
}
