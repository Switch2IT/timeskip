/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package be.ehb.rest;

import be.ehb.rest.mappers.IllegalArgumentExceptionMapper;
import be.ehb.rest.mappers.NullPointerExceptionMapper;
import be.ehb.rest.mappers.RestExceptionMapper;
import be.ehb.rest.resources.OrganizationsResource;
import be.ehb.rest.resources.SystemResource;
import be.ehb.rest.resources.UsersResource;
import be.ehb.servlets.CORSFilter;
import be.ehb.servlets.RequestFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * A class extending {@link Application} and annotated with @ApplicationPath is the Java EE 7 "no XML" approach to activating
 * JAX-RS.
 * <p>
 * <p>
 * Resources are served relative to the servlet path specified in the {@link ApplicationPath} annotation.
 * </p>
 */
@ApplicationPath("/api")
public class JaxRsActivator extends Application {

    /**
     * We based ourselves on http://jmchung.github.io/blog/2013/12/14/integrating-swagger-into-jax-rs-with-java-ee-6-specification/
     * for the integration of swagger with the web app maven archetype
     *
     * @return Set of classes
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        //Filters
        resources.add(CORSFilter.class);
        resources.add(RequestFilter.class);

        //Exception mappers
        resources.add(IllegalArgumentExceptionMapper.class);
        resources.add(NullPointerExceptionMapper.class);
        resources.add(RestExceptionMapper.class);

        //REST resources
        resources.add(UsersResource.class);
        resources.add(OrganizationsResource.class);
        resources.add(SystemResource.class);
    }
}

