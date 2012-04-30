/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.arquillian.jsfunitng.lifecycle;

import org.jboss.arquillian.core.api.Injector;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.api.event.ManagerStarted;
import org.jboss.arquillian.jsfunitng.assertion.AssertionRegistry;
import org.jboss.arquillian.jsfunitng.lifecycle.LifecycleManagerStore.ObjectAlreadyAssociatedException;
import org.jboss.arquillian.jsfunitng.lifecycle.LifecycleManagerStore.ObjectNotAssociatedException;
import org.jboss.arquillian.jsfunitng.lifecycle.LifecycleManagerStore.StoreHasAssociatedObjectsException;
import org.jboss.arquillian.jsfunitng.request.AfterRequest;
import org.jboss.arquillian.jsfunitng.request.BeforeRequest;
import org.jboss.arquillian.jsfunitng.request.RequestScoped;

/**
 * Drives {@link LifecycleManager} and {@link AssertionRegistry} lifecycle.
 * 
 * @author Lukas Fryc
 * 
 */
public class LifecycleManagerService {

    @Inject
    @ApplicationScoped
    private InstanceProducer<LifecycleManagerStore> store;

    @Inject
    @RequestScoped
    private InstanceProducer<LifecycleManager> manager;

    @Inject
    @RequestScoped
    private InstanceProducer<AssertionRegistry> registry;

    @Inject
    private Instance<Injector> injector;

    public void initializeStore(@Observes ManagerStarted event) {
        store.set(inject(new LifecycleManagerStore()));
    }

    private <T> T inject(T object) {
        return injector.get().inject(object);
    }

    public void initializeManagerAndDriver(@Observes BeforeRequest event) {
        manager.set(inject(new LifecycleManager()));
        registry.set(inject(new AssertionRegistry()));
    }

    public void finalizeManager(@Observes AfterRequest event) {
        try {
            getStore().verifyManagerUnbound();
        } catch (StoreHasAssociatedObjectsException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> void bindManager(@Observes BindLifecycleManager event) {
        try {
            getStore().bind(event.getDeterminator(), event.getBoundObject());
        } catch (ObjectAlreadyAssociatedException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> void unbindManager(@Observes UnbindLifecycleManager event) {
        try {
            getStore().unbind(event.getDeterminator(), event.getBoundObject());
        } catch (ObjectNotAssociatedException e) {
            throw new IllegalStateException(e);
        }
    }

    private LifecycleManagerStore getStore() {
        return store.get();
    }
}
