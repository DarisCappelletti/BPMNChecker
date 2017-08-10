package com.javacodegeeks.enterprise.rest.jersey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import org.json.simple.JSONObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

import choreography.Choreography;
import collaboration.Collaboration;

import org.apache.commons.io.IOUtils;


@Path("/files")
public class JerseyFileUpload {

	//private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C:/Users/Daris/Desktop/Upload/";
	private  String collaborationFolder = "C:/Users/Daris/Desktop/Upload/";
	private  String choreographyFolder = "C:/Users/Daris/Desktop/Upload1/";
	
	/**
	 * Upload a File
	 * 
	 * @throws IOException
	 */

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(FormDataMultiPart form) throws IOException {

		 File collaborationFile;
		 File choreographyFile;
		 FormDataBodyPart collaboration = form.getField("collaboration");
		 FormDataBodyPart choreography = form.getField("choreography");
		 ContentDisposition headerOfCollaboration = collaboration.getContentDisposition();
		 ContentDisposition headerOfChoreography = choreography.getContentDisposition();
		 InputStream collaborationInputStream = collaboration.getValueAs(InputStream.class);
		 InputStream choreographyInputStream = choreography.getValueAs(InputStream.class);
		  
		 /* String filePath = SERVER_UPLOAD_LOCATION_FOLDER +
		 * headerOfFilePart.getFileName();
		 * 
		 * // save the file to the server saveFile(fileInputStream, filePath);
		 * 
		 * String output = "File saved to server location using FormDataMultiPart : " +
		 * filePath;
		 */
		 collaborationFile = File.createTempFile("collaboration", ".aut",  new File(collaborationFolder));
		 choreographyFile = File.createTempFile("choreography", ".aut",  new File(choreographyFolder));
		 
		 Collaboration coobj = new Collaboration();
		 Choreography  chobj = new Choreography();
		 coobj.init(collaborationInputStream,false,collaborationFile);
		 chobj.init(choreographyInputStream,false,choreographyFile);
		 
		JSONObject obj = new JSONObject();

		obj.put("choreography", choreographyFile.getAbsolutePath());
		obj.put("collaboration",  collaborationFile.getAbsolutePath());
		
		//byte[] bytes = IOUtils.toByteArray(fileInputStream);
		//obj.put("File", DatatypeConverter.printBase64Binary(bytes));
		
		StringWriter out = new StringWriter();
		obj.writeJSONString(out);

		String jsonText = out.toString();

		return Response.status(200).entity(jsonText).build();

	}

	// save uploaded file to a defined location on the server
	/*private void saveFile(InputStream uploadedInputStream, String serverLocation) {

		try {
			OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			outpuStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}

			outpuStream.flush();
			outpuStream.close();

			uploadedInputStream.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}*/

}