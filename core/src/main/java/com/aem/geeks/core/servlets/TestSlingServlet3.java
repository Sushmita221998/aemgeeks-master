package com.aem.geeks.core.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.aem.geeks.core.services.TestOSGIService1;
import com.google.gson.Gson;

@Component(service = Servlet.class, immediate = true, property = {
		ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/aem/testServlet",
		ServletResolverConstants.SLING_SERVLET_METHODS + "=GET",
		ServletResolverConstants.SLING_SERVLET_EXTENSIONS + "=json" })
//@Exporter(name = "jackson", extensions = "json")
public class TestSlingServlet3 extends SlingSafeMethodsServlet {

	//Not Tested in AEM
	private static final long serialVersionUID = 1L;

	// private static final Logger log =
	// LoggerFactory.getLogger(TestSlingServlet3.class);

	@Reference
	private TestOSGIService1 testOSGIService1;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

		ResourceResolver resolver = request.getResourceResolver();
		String path = request.getParameter("path");
		response.setContentType("application/json");
		if (StringUtils.isBlank(path)) {

			response.getWriter().write(new Gson().toJson("Request Parameter Path is Null"));
		} else {
			Map<String, String> pageInfoData = testOSGIService1.getPageInfo(path, resolver);

			response.getWriter().write(new Gson().toJson(pageInfoData));
		}
	}

}
