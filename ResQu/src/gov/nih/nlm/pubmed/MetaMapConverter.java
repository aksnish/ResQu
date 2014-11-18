package gov.nih.nlm.pubmed;

import gov.nih.nlm.nls.skr.GenericObject;
import gov.nih.nlm.utils.Constants;

import java.io.File;

public class MetaMapConverter {

	public String getMetaMapFormat(String inputBuf)
	{
		String results = null;
		String batchCommand = Constants.METAMAP_COMMAND;
		String batchNotes = "SKR API Test";
		boolean silentEmail = false;
		boolean silentOnErrors = false;
		boolean singleLineDelimitedInput = false;
		boolean singleLineDelimitedInputWithId = false;
		int priority = -1;

		GenericObject myGenericObj = new GenericObject(Constants.UTS_USER_NAME, Constants.UTS_PASSWORD);
		myGenericObj.setField("Email_Address", Constants.EMAIL);
		myGenericObj.setField("Batch_Command", batchCommand);
		if (batchNotes != null) {
			myGenericObj.setField("BatchNotes", batchNotes);
		}
		myGenericObj.setField("SilentEmail", silentEmail);
		if (silentOnErrors) {
			myGenericObj.setField("ESilent", silentOnErrors);
		}
		if (singleLineDelimitedInput) {
			myGenericObj.setField("SingLine", singleLineDelimitedInput);
		}
		if (singleLineDelimitedInputWithId) {
			myGenericObj.setField("SingLinePMID", singleLineDelimitedInputWithId);
		}
		if (priority > 0) {
			myGenericObj.setField("RPriority", Integer.toString(priority));
		}
		if (inputBuf .length() > 0) {
			File inFile = new File(inputBuf.toString().trim()); 
			if (inFile.exists()) {
				myGenericObj.setFileField("UpLoad_File", inputBuf.toString().trim());
			}
		}
		try
		{
			results = myGenericObj.handleSubmission();
		} catch (RuntimeException ex) {
			System.err.println("");
			System.err.print("An ERROR has occurred while processing your");
			System.err.println(" request, please review any");
			System.err.print("lines beginning with \"Error:\" above and the");
			System.err.println(" trace below for indications of");
			System.err.println("what may have gone wrong.");
			System.err.println("");
			System.err.println("Trace:");
			ex.printStackTrace();
		}
		return results; 
	} 
}
