package memorix;

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

public class Memorix {

	@SuppressWarnings("null")

	public static Map<String, Object> createNote(DispatchContext dctx, Map<String, Object> context) {

		// Lets create note
		GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
		String noteId = delegator.getNextSeqId("Note");

		String loginName = (String) context.get("loginName");
		String notes = (String) context.get("note");
		// String noteDateTime = (String)context.get("noteDateTime");

		GenericValue note = delegator.makeValue("Note");
		note.set("noteId", noteId);
		note.set("loginName", loginName);
		note.set("note", notes);
		// note.set("noteDateTime", noteDateTime);

		GenericValue myNewNote = null;
		try {
			myNewNote = delegator.create(note);
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> result = null;
		if (myNewNote != null) {
			result = ServiceUtil.returnSuccess();
			result.put("noteId", noteId);
			return result;
		} else {
			return ServiceUtil.returnFailure();
		}

	}

	public static Map<String, Object> updateNote(DispatchContext dctx, Map<String, Object> context) {

		// Lets update a note
		GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
		// String noteId = delegator.getNextSeqId("Note");

		String noteId = (String) context.get("noteId");
		String loginName = (String) context.get("loginName");
		String notes = (String) context.get("note");
		// String noteDateTime = (String)context.get("noteDateTime");

		GenericValue note = delegator.makeValue("Note");
		note.set("noteId", noteId);
		note.set("loginName", loginName);
		note.set("note", notes);
		// note.set("noteDateTime", noteDateTime);

		/// GenericValue myNewNote = null;
		try {
			/// myNewNote = delegator.store(note);
			delegator.createOrStore(note);
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> result = null;
		/// if(myNewNote != null)
		/// {
		result = ServiceUtil.returnSuccess();
		result.put("noteId", noteId);
		return result;
		/// else
		/// {
		/// return ServiceUtil.returnFailure();
		/// }

	}
}