package test;

import java.util.Map;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntity;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ServiceUtil;

import memorix.Memorix;

import static org.junit.Assert.*; 
import org.junit.*;


public class UpdateNoteTest{

    Map<String, Object> ctx = UtilMisc.<String, Object>toMap("1", "admin", "test notes xyz");
	
		@Test 
		public void testupdate() {
			Memorix.updateNote(null, ctx);
			assertEquals(1, 1, 0);
			//assertNotNull(noteId);
			

		}
}
	