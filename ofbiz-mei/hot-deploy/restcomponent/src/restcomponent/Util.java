package restcomponent;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.Response;

import org.ofbiz.base.conversion.ConversionException;
import org.ofbiz.base.lang.JSON;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.Converters.GenericValueToJSON;


public class Util {
	
	synchronized static String convertListGenericValueToJSON(List<GenericValue> listIn) {
		JsonArrayBuilder builder = Json.createArrayBuilder();

		for(GenericValue value: listIn) {
			JSON json=null;
			try {
				json = new GenericValueToJSON().convert(value);
			} catch (ConversionException e) {
				return null;
			}
			
			
			JsonReader jsonReader = Json.createReader(new StringReader(json.toString()));
			JsonObject object = jsonReader.readObject();
			jsonReader.close();
			 
			builder.add(object);
		}
		
		JsonArray arr = builder.build();
		return arr.toString();
	}	

	synchronized static String getProduct(String productId) {
    	GenericValue product = null;
    	JsonObject object=null;

    	GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");

    	try {
    	       product = delegator.findOne("Product",
    	                          UtilMisc.toMap("productId", productId), false);
    	       
   			JSON json=null;

   			json = new GenericValueToJSON().convert(product);
   			
   			JsonReader jsonReader = Json.createReader(new StringReader(json.toString()));
   			object = jsonReader.readObject();
   			jsonReader.close();
    	}
    	catch (GenericEntityException e) {
			return null;
    	}
    	catch (ConversionException e) {
   				return null;
   		}
    	
    	return object.toString();
	}
	synchronized static String getRecipe(String recipeId) {
    	GenericValue recipe = null;
    	JsonObject object=null;

    	GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");

    	try {
    	       recipe = delegator.findOne("Recipe",
    	                          UtilMisc.toMap("recipeId", recipeId), false);
    	       
   			JSON json=null;

   			json = new GenericValueToJSON().convert(recipe);
   			
   			JsonReader jsonReader = Json.createReader(new StringReader(json.toString()));
   			object = jsonReader.readObject();
   			jsonReader.close();
    	}
    	catch (GenericEntityException e) {
			return null;
    	}
    	catch (ConversionException e) {
   				return null;
   		}
    	
    	return object.toString();
	}




	
	/* Memorix 
	 * ********************************************************************
	 */
	
	
	synchronized static String getNote(String noteId) {
    	GenericValue note = null;
    	JsonObject object=null;

    	GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");

    	try {
    	       note = delegator.findOne("Note",
    	                          UtilMisc.toMap("noteId", noteId), false);
    	       
   			JSON json=null;

   			json = new GenericValueToJSON().convert(note);
   			
   			JsonReader jsonReader = Json.createReader(new StringReader(json.toString()));
   			object = jsonReader.readObject();
   			jsonReader.close();
    	}
    	catch (GenericEntityException e) {
			return null;
    	}
    	catch (ConversionException e) {
   				return null;
   		}
    	
    	return object.toString();
	}

	

}

