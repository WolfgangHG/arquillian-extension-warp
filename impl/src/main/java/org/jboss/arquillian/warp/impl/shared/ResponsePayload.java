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
package org.jboss.arquillian.warp.impl.shared;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.arquillian.test.spi.TestResult;
import org.jboss.arquillian.warp.Inspection;

public class ResponsePayload implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Inspection> inspections;
    private TestResult testResult;
    private int status;
    private Map<String, List<String>> headers;
    private long serialId;
    private Set<ExecutedMethod> executedMethods = new HashSet<ExecutedMethod>();

    public ResponsePayload(long serialId) {
        this.serialId = serialId;
    }

    public List<Inspection> getInspections() {
        return inspections;
    }

    public void setInspections(Inspection... inspections) {
        this.inspections = Arrays.asList(inspections);
    }

    public void setInspections(List<Inspection> inspections) {
        this.inspections = inspections;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResult testResult) {
        if (this.testResult != null) {
            throw new IllegalStateException("test result was already set");
        }
        this.testResult = testResult;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public long getSerialId() {
        return serialId;
    }

    public Set<ExecutedMethod> getExecutedMethods() {
        return executedMethods;
    }
}
