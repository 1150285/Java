import java.util.Map;



import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntity;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ServiceUtil;


public class MyServices{

@SuppressWarnings("null")

public static Map<String, Object> createRecipe(DispatchContext dctx, Map<String, Object>context) {

// Lets create recipe
GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
String recipeId = delegator.getNextSeqId("Recipe");

String recipeName = (String)context.get("recipeName");
String notes = (String)context.get("notes");
String recipeTypeId = (String)context.get("recipeTypeId");

GenericValue recipe = delegator.makeValue("Recipe");
recipe.set("recipeId", recipeId);
recipe.set("recipeName", recipeName);
recipe.set("notes", notes);
recipe.set("recipeTypeId", recipeTypeId);


GenericValue myNewRecipe = null; 
try {
	myNewRecipe = delegator.create(recipe);
} catch (GenericEntityException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
Map<String, Object> result = null;
if(myNewRecipe != null)
{
    result = ServiceUtil.returnSuccess () ;
	result.put("recipeId", recipeId);
	return result; }
else
{
    return ServiceUtil.returnFailure();
 }


}

public static Map<String, Object> updateRecipe(DispatchContext dctx, Map<String, Object>context) {

	// Lets update a recipe
	GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
	//String recipeId = delegator.getNextSeqId("Recipe");
	
		String recipeId = (String)context.get("recipeId");
	String recipeName = (String)context.get("recipeName");
	String notes = (String)context.get("notes");
	String recipeTypeId = (String)context.get("recipeTypeId");

	GenericValue recipe = delegator.makeValue("Recipe");
	recipe.set("recipeId", recipeId);
	recipe.set("recipeName", recipeName);
	recipe.set("notes", notes);
	recipe.set("recipeTypeId", recipeTypeId);


	///GenericValue myNewRecipe = null; 
	try {
		///myNewRecipe = delegator.store(recipe);
		delegator.createOrStore(recipe);
	} catch (GenericEntityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Map<String, Object> result = null;
	///if(myNewRecipe != null)
	///{
	    result = ServiceUtil.returnSuccess () ;
		result.put("recipeId", recipeId);
		return result; 
	///else
	///{
	 ///   return ServiceUtil.returnFailure();
	 ///}


	
}
}
