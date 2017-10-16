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
 * 
 * Author: atb@isep.ipp.pt
 *******************************************************************************/
package restcomponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.ofbiz.base.conversion.ConversionException;
import org.ofbiz.base.lang.JSON;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.Converters.GenericValueToJSON;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;

import javolution.util.FastMap;


@Path("/note")
public class NoteResource {

	@Context
	HttpServletRequest httpRequest;

	// ../url_resource
	// handling the collection
	//
	// GET return all
	// POST create new entry, returns 201
	// PUT not allowed
	// DELETE not allowed
	//

	/**
	 * This method returns a collection with all the notes...
	 * 
	 * @return
	 */
	@GET
	@Produces("application/json")
	public Response getAllNotes() {
		String username = null;
		String password = null;

		try {
			username = httpRequest.getHeader("login.username");
			password = httpRequest.getHeader("login.password");
		} catch (NullPointerException e) {
			return Response.serverError().entity("Problem reading http header(s): login.username or login.password")
					.build();
		}

		if (username == null || password == null) {
			return Response.serverError().entity("Problem reading http header(s): login.username or login.password")
					.build();
		}

		GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
		List<GenericValue> notes = null;

		try {
			notes = delegator.findAll("Note", false);
		} catch (GenericEntityException e) {
			return Response.serverError().entity(e.toString()).build();
		}

		if (notes != null) {

			String response = Util.convertListGenericValueToJSON(notes);

			if (response == null) {
				return Response.serverError().entity("Erro na conversao do JSON!").build();
			}

			return Response.ok(response).type("application/json").build();
		}

		// shouldn't ever get here ... should we?
		throw new RuntimeException("Invalid ");
	}

	/**
	 * This method creates a new note in the collection
	 * 
	 * @return
	 */

	@POST
	@Produces("application/json")
	public Response createNote() {
		String username = null;
		String password = null;

		try {
			username = httpRequest.getHeader("login.username");
			password = httpRequest.getHeader("login.password");
		} catch (NullPointerException e) {
			return Response.serverError().entity("Problem reading http header(s): login.username or login.password")
					.build();
		}

		if (username == null || password == null) {
			return Response.serverError().entity("Problem reading http header(s): login.username or login.password")
					.build();
		}

		JsonReader jsonReader;
		try {
			jsonReader = Json.createReader(httpRequest.getReader());
		} catch (IOException e) {
			return Response.serverError().entity("Problem reading json body").build();
		}

		JsonObject jsonObj = jsonReader.readObject();

		// Lets now invoke the ofbiz service that creates a note
		GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
		LocalDispatcher dispatcher = org.ofbiz.service.ServiceDispatcher.getLocalDispatcher("default", delegator);

		Map<String, String> paramMap = UtilMisc.toMap(
				"loginName", jsonObj.getString("loginName"), 
				"note", jsonObj.getString("note"));
				//"noteDateTime",jsonObj.getString("noteDateTime"))
				
				//"login.username", username, 
				//"login.password", password)
				

		Map<String, Object> result = FastMap.newInstance();
		try {
			result = dispatcher.runSync("createNote", paramMap);
		} catch (GenericServiceException e1) {
			Debug.logError(e1, PingResource.class.getName());
			return Response.serverError().entity(e1.toString()).build();
		}

		String noteId = result.get("noteId").toString();
		String note = Util.getNote(noteId);
		if (note != null) {
			return Response.ok(note).type("application/json").build();
		} else {
			return Response.serverError().entity("Problem reading the new note after created!").build();
		}
	}

	// ../url_resource/{id}
	// handling individual itens in the collection
	//
	// item id must be present in the request object
	//
	// GET return specific item or 404
	// POST update existing entry or 404
	// PUT overwrite existing or create new given the id.
	// DELETE deletes the item
	//

	/**
	 * This method returns a note given its id.
	 * 
	 * @param noteId
	 * @return
	 */

	@GET
	@Produces("application/json")
	@Path("{id}")
	public Response getNoteById(@PathParam("id") String noteId) {
		
		String username = null;
		String password = null;

		try {
			username = httpRequest.getHeader("login.username");
			password = httpRequest.getHeader("login.password");
		} catch (NullPointerException e) {
			return Response.serverError().entity("Problem reading http header(s): login.username or login.password")
					.build();
		}

		if (username == null || password == null) {
			return Response.serverError().entity("Problem reading http header(s): login.username or login.password")
					.build();
		}

		GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
		GenericValue note = null;

		try {
			note = delegator.findOne("Note", UtilMisc.toMap("noteId", noteId), false);
		} catch (GenericEntityException e) {
			return Response.serverError().entity(e.toString()).build();
		}

		if (note != null) {

			JsonObject object = null;

			JSON json = null;

			try {
				json = new GenericValueToJSON().convert(note);
			} catch (ConversionException e) {
				return Response.serverError().entity("Problem converting the note to json!").build();
			}

			JsonReader jsonReader = Json.createReader(new StringReader(json.toString()));
			object = jsonReader.readObject();
			jsonReader.close();

			return Response.ok(object.toString()).type("application/json").build();
		}

		// shouldn't ever get here ... should we?
		throw new RuntimeException("Invalid ");
}


	// Put para actualizar...
	// https://localhost:8443/webtools/control/ServiceList?sel_service_name=updateProduct
	/**
	 * This method updates a note (given its id).
	 * 
	 * @param noteId
	 * @return
	 */

	@PUT
	@Produces("application/json")
	@Path("{id}")
	public Response updateNoteById(@PathParam("id") String noteId) {
		String username = null;
		String password = null;

		try {
			username = httpRequest.getHeader("login.username");
			password = httpRequest.getHeader("login.password");
		} catch (NullPointerException e) {
			return Response.serverError().entity("Problem reading http header(s): login.username or login.password")
					.build();
		}

		if (username == null || password == null) {
			return Response.serverError().entity("Problem reading http header(s): login.username or login.password")
					.build();
		}

		JsonReader jsonReader;
		try {
			jsonReader = Json.createReader(httpRequest.getReader());
		} catch (IOException e) {
			return Response.serverError().entity("Problem reading json body").build();
		}

		JsonObject jsonObj = jsonReader.readObject();

		// Lets now invoke the ofbiz service that updates a note
		GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
		LocalDispatcher dispatcher = org.ofbiz.service.ServiceDispatcher.getLocalDispatcher("default", delegator);

		Map<String, String> paramMap = UtilMisc.toMap(
				"noteId", noteId,
				"loginName", jsonObj.getString("loginName"), 
				"note", jsonObj.getString("note"));
				//"noteDateTime",jsonObj.getString("noteDateTime"));

		
		Map<String, Object> result = FastMap.newInstance();
		try {
			result = dispatcher.runSync("updateNote", paramMap);
		} catch (GenericServiceException e1) {
			Debug.logError(e1, PingResource.class.getName());
			return Response.serverError().entity(e1.toString()).build();
		}

		if (result.get("responseMessage").toString().compareTo("success") == 0) {
			String note = Util.getNote(noteId);

			if (note != null) {

				return Response.ok(note).type("application/json").build();
			} else {
				return Response.serverError().entity("Problem reading the new note after updated!").build();
			}
		} else {
			return Response.serverError().entity(result.get("responseMessage").toString()).build();
		}
	}
	
	
}
