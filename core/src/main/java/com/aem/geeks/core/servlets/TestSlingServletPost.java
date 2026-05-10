package com.aem.geeks.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/contact/submit")
public class TestSlingServletPost extends SlingAllMethodsServlet {

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

		// For request parameter data
		/*
		 * String name=request.getParameter("name"); String
		 * email=request.getParameter("email"); String
		 * message=request.getParameter("message");
		 */

//		For JSON Payload

		StringBuilder sb = new StringBuilder();
		request.getReader().lines().forEach(sb::append);

		String body = sb.toString();

		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(body, JsonObject.class);

		String name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : null;
		String email = jsonObject.has("email") ? jsonObject.get("email").getAsString() : null;
		String message = jsonObject.has("message") ? jsonObject.get("message").getAsString() : null;

		response.setContentType("application/json");

		HashMap<String, String> map = new HashMap<String, String>();

//		if((name==null || name.isEmpty()) || (email==null || email.isEmpty()) || (message==null || message.isEmpty())) {
//			if(StringUtils.isBlank(name) || StringUtils.isBlank(email) || StringUtils.isBlank(message)) {
		// Use this clean one liner:
		if (StringUtils.isAnyBlank(name, email, message)) {
			response.setStatus(400);
			map.put("status", "error");
			map.put("message", "All fields are required");
		} else {
			response.setStatus(200);
			map.put("status", "success");
			map.put("message", new StringBuilder().append("Thank you ").append(name).append("! We will contact you at ")
					.append(email).toString());
		}

		PrintWriter writer = response.getWriter();
		writer.write(new Gson().toJson(map));

	}
}
