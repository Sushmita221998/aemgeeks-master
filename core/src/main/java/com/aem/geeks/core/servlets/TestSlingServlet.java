package com.aem.geeks.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import com.google.gson.Gson;

@Component(service=Servlet.class)
@SlingServletPaths(value="/bin/greeting")
public class TestSlingServlet extends SlingSafeMethodsServlet{
	
	StringBuilder stringBuilder;

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException{
		
		String name=request.getParameter("name");
		HashMap<String,String> map=new HashMap<String, String>();
		map.put("message", new StringBuilder().append("Hello, ").append(name).append("! Welcome to AEM.").toString());
		response.setContentType("application/json");
		PrintWriter writer=response.getWriter();
		writer.write(new Gson().toJson(map));
	}
	
}
