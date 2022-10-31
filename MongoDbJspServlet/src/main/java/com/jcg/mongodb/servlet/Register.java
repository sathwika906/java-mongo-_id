package com.jcg.mongodb.servlet;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient; 
//import com.mongodb.MongoCredential;
import com.mongodb.MongoException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class register
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	public static String email;
	private static final long serialVersionUID = 1L;
	
	String id="";
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response);}
		public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
		{
	
		//response.setContentType("text/html");
        String firstname = request.getParameter("firstname");
        String middlename = request.getParameter("middlename");
        String lastname = request.getParameter("lastname");
        email=request.getParameter("email");
        String gender = request.getParameter("gender");
        String pincode = request.getParameter("pin");
        String birthday = request.getParameter("birthday");
        String pid="";
        
        try (MongoClient mongo = new MongoClient( "localhost" , 27017 ))
	    {
	    
                MongoDatabase database = mongo.getDatabase("Project1Db"); 
		    
		    
		    try
		    {
		    	database.createCollection("PatientDetails");
		    } catch (Exception exception) 
		    {
		        System.err.println("Collection:- PatientDetails already Exists");
		    }
		    MongoCollection<Document> collection = database.getCollection("PatientDetails"); 
		    System.out.println(" PatientDetails selected successfully"); 
		    
		    try
		    {
		    	List<Document> results = new ArrayList<>();
		    	FindIterable<Document> iterable = collection.find();
		    	iterable.into(results);
		    	
		    	if(!results.isEmpty())
		    	{
		    		for(Document document: results) 
		    		{
		    		    
		    			 long  min = 99999999999999L;
		    		     long  max = 10000000000000L;
		    		     long random_int = (long)(Math.random() * (max - min + 1) + min);
		    		     id = Long.toString(random_int);
		    			
		    			
		    			if(id!=document.get("patient id"))
		    			{
				Document doc = new Document("patient id",id)
						.append("First Name", firstname)
						.append("Middle Name", middlename)
		    			.append("Last Name", lastname)
		    			.append("Email", email)
		    			.append("Gender", gender)
		    			.append("Birthday", birthday)
		    			.append("Pincode", pincode);
		    			
		    			collection.insertOne(doc);	
		    			break;
	    			}
	    			else
	    			{
	    				continue;
	    			}
	    		}
	    	}
	    	else
	    	{
	    		
	    		long  min = 99999999999999L;
    		    long  max = 10000000000000L;
    		    long random_int = (long)(Math.random() * (max - min + 1) + min);
    		    id = Long.toString(random_int);
	    		
	    		Document doc = new Document("patient id", id)
		    			.append("First Name", firstname)
		    			.append("Middle Name", middlename)
		    			.append("Last Name", lastname)
		    			.append("Email", email)
		    			.append("Gender", gender)
		    			.append("Birthday", birthday)
		    			.append("Pincode", pincode);
		    			
		    			//Inserting document into the collection
		    			collection.insertOne(doc);
	    	}
	    }
	    catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
	    
	    
	    
	    //selecting patient_id to be displayed in the next field
	    try {
	    	List<Document> results = new ArrayList<>();
	    	FindIterable<Document> pl = collection.find(Filters.eq("Email", email));
	    	pl.into(results);
	    	
	    	
	    	pid =results.get(0).get("patient id").toString();
	    	
	    
	    	
	    }
	    catch(MongoException me)
	    {
	    	System.err.println("Unable to select id due to an error: " + me);
	    }
	    
	    request.setAttribute("pid", pid);
		    			
		request.getRequestDispatcher("/Add.jsp").forward(request, response);}
    
		
}}