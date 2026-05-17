package com.aem.geeks.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

@Component(service = Servlet.class, property = {
		ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/aemgeeks/pageinfo",
		ServletResolverConstants.SLING_SERVLET_METHODS + "=GET",
		ServletResolverConstants.SLING_SERVLET_EXTENSIONS + "=json" })
public class TestSlingServlet2 extends SlingSafeMethodsServlet {
	
	// Not Tested in AEM
	
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(TestSlingServlet2.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

		String path = request.getParameter("path");

		ResourceResolver resolver = request.getResourceResolver();

		response.setContentType("application/json");

		if (StringUtils.isBlank(path)) {

			response.getWriter().write("{\"error\":\"path is required\"}");
			return;
		} else {
			Resource resource = resolver.getResource(path);

			if (resource != null) {

				Resource jcrData = resource.getChild("jcr:content");

				if (jcrData != null) {
					ValueMap valueMapData = jcrData.getValueMap();

					String jcrTitle = valueMapData.get("jcr:title", String.class);
					String lastModifiedBy = valueMapData.get("cq:lastModifiedBy", String.class);

					Map<String, String> responseData = new HashMap<>();
					responseData.put("path", path);
					responseData.put("title", jcrTitle);
					responseData.put("lastModifiedBy", lastModifiedBy);

					// Option 1
					PrintWriter writer = response.getWriter();
					writer.write(new Gson().toJson(responseData));

					// Option2
					// response.getWriter().write(new Gson().toJson(responseData));
				}
			} else {
				response.getWriter().write("{\"error\":\"resource not found\"}");
			}
		}

	}
}
