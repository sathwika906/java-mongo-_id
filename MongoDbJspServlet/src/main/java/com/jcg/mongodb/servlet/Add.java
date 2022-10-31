package com.jcg.mongodb.servlet;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;


@WebServlet("/Add")
public class Add extends HttpServlet {
    private static final long serialVersionUID = 1L;

   
    public Add() {
    }
                              
   

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	handleRequest(request, response);
	}

	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
	{
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
       
        String screening=null;
        
        
        int age=Integer.parseInt(request.getParameter("age"));
        int smoke=Integer.parseInt(request.getParameter("smoke"));
        int alcohol=Integer.parseInt(request.getParameter("alcohol"));
        int waist=Integer.parseInt(request.getParameter("waist"));
        int physical =Integer.parseInt(request.getParameter("physical"));
        int Family=Integer.parseInt(request.getParameter("Family"));
        
        int sum=(age+smoke+alcohol+waist+physical+Family);

		if (sum > 4)
			screening ="yes";
		if (sum <= 4)
			screening="no";
		 try (MongoClient mongo = new MongoClient( "localhost" , 27017 ))
		    {
			 MongoDatabase database = mongo.getDatabase("Project1Db"); 
			    
			    
			    try
			    {
			    	// Creating a collection
					database.createCollection("PatientDetails");
			    } catch (Exception exception) 
			    {
			        System.err.println("Collection:- PatientDetails already Exists");
			    }
			    
			    // Retrieving a collection
			    MongoCollection<Document> collection = database.getCollection("PatientDetails"); 
			    System.out.println("Collection PatientDetails selected successfully"); 
			    try
			    {
			    	
			    	BasicDBObject searchQuery = new BasicDBObject("Email",Register.email);
			    	BasicDBObject updateFields = new BasicDBObject();
			    	updateFields.append("Age", age);
			    	updateFields.append("Smoke", smoke);
			    	updateFields.append("Alcohol", alcohol);
			    	updateFields.append("Waist", waist);
			    	updateFields.append("Physical Activity", physical);
			    	updateFields.append("Family History", Family);
			    	updateFields.append("Total Score", sum);
			    	updateFields.append("Screening", screening);
			    	BasicDBObject setQuery = new BasicDBObject();
			    	setQuery.append("$set", updateFields);
			    	collection.updateMany(searchQuery, setQuery);
		    }
			    catch (MongoException me) {
	                System.err.println("Unable to insert due to an error: " + me);
	            }
			    
		    }
		 if (sum == -1)
				out.println("<h2>Please answer the questions first.");
			if (sum > 4)
				out.println("The person needs  screening.");
			if (sum < 4)
				out.println("<br>The person is not at risk of NCDs and doesn't need screening.");

        out.print("THE TOTAL SCORE IS = "+sum);
        out.println("<body>");
        out.println("<html>");
//       
        out.println("<button onclick=location.href='index.jsp'; align=\"center\" class=\"btn btn-primary\" >Back</button>");
        out.println("</body>");
        out.println("</html>");
    }

}

