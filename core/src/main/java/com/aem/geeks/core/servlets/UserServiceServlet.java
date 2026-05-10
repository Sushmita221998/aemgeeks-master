package com.aem.geeks.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.aem.geeks.core.services.UserService;
import com.google.gson.Gson;

@Component(service = Servlet.class)
@SlingServletResourceTypes(resourceTypes = "aemgeeks/components/test/test1", selectors = "details", extensions = "json", methods = "GET")
public class UserServiceServlet extends SlingSafeMethodsServlet {

	@Reference
	private UserService service;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");

		response.setContentType("application/json");

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("fullName", service.getUserFullName(firstName, lastName));
		PrintWriter writer = response.getWriter();
		writer.write(new Gson().toJson(map));
	}

}
