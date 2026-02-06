package com.aem.geeks.core.models;

import com.aem.geeks.core.services.SportService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class})
public class SportsClub {

    @OSGiService
    private SportService sportService;
    @Inject
    private Resource sports;

    private List<SportsChannel> sportsChannels;


    @PostConstruct
    void init() {
        if ((sports != null)  && (sports.getChild("sports") != null)) {
           Resource childResource = sports.getChild("sports");
            Iterator<Resource> children = childResource.listChildren();
            sportsChannels = new ArrayList<>();
            while (children.hasNext()) {
                Resource next = children.next();
                ValueMap valueMap = next.getValueMap();
                SportsChannel sportsChannel = new SportsChannel();
                sportsChannel.setStype((String) valueMap.get("stype"));
                sportsChannel.setTimings((String) valueMap.get("timings"));
                sportsChannel.setPlayers((String) valueMap.get("players"));
                sportsChannel.setDate(valueMap.get("date",Date.class));
                //sportsChannel.setDate((Date) valueMap.get("date"));
                sportsChannel.setIcon((String) valueMap.get("icon"));
                sportsChannels.add(sportsChannel);
            }
            sportsChannels = sportService.sortByDate(sportsChannels);
        }
    }

    public List<SportsChannel> getSportsChannels() {
        return sportsChannels;
    }
}
