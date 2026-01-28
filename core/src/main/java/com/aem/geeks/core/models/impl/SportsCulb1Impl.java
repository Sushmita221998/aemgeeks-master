package com.aem.geeks.core.models.impl;

import com.aem.geeks.core.models.SportClub1;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
       adapters = SportClub1.class)
public class SportsCulb1Impl implements SportClub1 {

    private static final Logger LOG = LoggerFactory.getLogger(SportsCulb1Impl.class);

    @ScriptVariable
    Resource resource;


    public List<Map<String, String>> getSports() {
        List<Map<String, String>> sportsDetailsMap=new ArrayList<>();
        try {
            Resource sportsDetail = resource.getChild("sports");
            if(sportsDetail!=null){
                for (Resource sports : sportsDetail.getChildren()) {
                    Map<String,String> sportMap=new HashMap<>();
                    sportMap.put("title",sports.getValueMap().get("title",String.class));
                    sportMap.put("timing",sports.getValueMap().get("timing",String.class));
                    sportMap.put("players",sports.getValueMap().get("players",String.class));
                    sportMap.put("date",sports.getValueMap().get("date",String.class));
                    sportMap.put("icon",sports.getValueMap().get("icon",String.class));

                    sportsDetailsMap.add(sportMap);
                }
            }
        }catch (Exception e){
            LOG.info("\n ERROR while getting Book Details {} ",e.getMessage());
        }
        return sportsDetailsMap;
    }

}
