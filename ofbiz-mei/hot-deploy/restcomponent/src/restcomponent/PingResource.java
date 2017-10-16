/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package restcomponent;


import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import javax.servlet.http.HttpServletRequest;

import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.service.GenericDispatcherFactory;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import org.ofbiz.entity.GenericEntity;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;

@Path("/ping")
public class PingResource {

	@Context
	HttpServletRequest httpRequest;
	
	@GET
	@Produces("text/plain")
	@Path("{message}")
    public Response sayHello(@PathParam("message") String message) {
    	
		String username = null;
		String password = null;

		try {
			username = httpRequest.getHeader("login.username");
			password = httpRequest.getHeader("login.password");
		} catch (NullPointerException e) {
			return Response.serverError().entity("Problem reading http header(s): login.username or login.password").build();
		}
		
		if (username == null || password == null) {
			return Response.serverError().entity("Problem reading http header(s): login.username or login.password").build();
		}
		
    	GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
    	LocalDispatcher dispatcher = org.ofbiz.service.ServiceDispatcher.getLocalDispatcher("default", delegator);

    	Map<String, String> paramMap = UtilMisc.toMap( 
    			"message", message, 
    			"login.username", username,
    			"login.password", password
    		);
		
		Map<String, Object> result = FastMap.newInstance();
		try {
			result = dispatcher.runSync("ping", paramMap);
		} catch (GenericServiceException e1) {
			Debug.logError(e1, PingResource.class.getName());
			return Response.serverError().entity(e1.toString()).build();
		}
		
		if (ServiceUtil.isSuccess(result)) {    	
			return Response.ok("RESPONSE: *** " + result.get("message") + " ***").type("text/plain").build();
		}
		
		if (ServiceUtil.isError(result) || ServiceUtil.isFailure(result)) {
			return Response.serverError().entity(ServiceUtil.getErrorMessage(result)).build();
		}
		
		// shouldn't ever get here ... should we?
		throw new RuntimeException("Invalid ");
    }
	
	@GET
	@Produces("text/plain")
	@Path("/product/{id}")
    public Response getProductById(@PathParam("id") String productId) {
		// id example="GC-001-C100"
		
		String username = null;
		String password = null;

		try {
			username = httpRequest.getHeader("login.username");
			password = httpRequest.getHeader("login.password");
		} catch (NullPointerException e) {
			return Response.serverError().entity("Problem reading http header(s): login.username or login.password").build();
		}
		
		if (username == null || password == null) {
			return Response.serverError().entity("Problem reading http header(s): login.username or login.password").build();
		}
		
    	GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
    	GenericValue product = null;
    	
    	try {
    	       product = delegator.findOne("Product",
    	                          UtilMisc.toMap("productId", productId), false);
    	}
    	catch (GenericEntityException e) {
			return Response.serverError().entity(e.toString()).build();
    	}
    	
	   // product is a GenericValue object, so we can access field
	   // values directly as shown
	   if (product != null) {    // Note: a null GenericValue object is valid
		   String productName = (String) product.get("productName");
		
		   return Response.ok("RESPONSE: *** " + productName + " ***").type("text/plain").build();
	   }
	   
		// shouldn't ever get here ... should we?
		throw new RuntimeException("Invalid ");
	}

}
