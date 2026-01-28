package com.aem.geeks.core.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;

import javax.servlet.ServletException;
import java.io.IOException;

@Component(
        service = SlingSafeMethodsServlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/sportsdata"
        }
)
public class SportsServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String path = "/content/aemgeeks/us/en/home/jcr:content/parsys/sports_component/sports";

        ResourceResolver resolver = request.getResourceResolver();
        Resource sportsResource = resolver.getResource(path);

        JsonArray sportsArray = new JsonArray();

        if (sportsResource != null) {
            for (Resource child : sportsResource.getChildren()) {
                JsonObject obj = new JsonObject();

                obj.addProperty("stype", child.getValueMap().get("stype", ""));
                obj.addProperty("timings", child.getValueMap().get("timings", ""));
                obj.addProperty("players", child.getValueMap().get("players", ""));
                obj.addProperty("date", child.getValueMap().get("date", ""));
                obj.addProperty("icon", child.getValueMap().get("icon", ""));

                sportsArray.add(obj);
            }
        }

        JsonObject finalResponse = new JsonObject();
        finalResponse.add("sports", sportsArray);

        response.setContentType("application/json");
        response.getWriter().write(finalResponse.toString());
    }
}
