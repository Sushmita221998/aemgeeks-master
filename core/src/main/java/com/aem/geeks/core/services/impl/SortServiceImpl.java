package com.aem.geeks.core.services.impl;

import com.aem.geeks.core.models.SportsChannel;
import com.aem.geeks.core.services.SportService;
import org.osgi.service.component.annotations.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component(service = SportService.class)
public class SortServiceImpl implements SportService {

    @Override
    public List<SportsChannel> sortByDate(List<SportsChannel> channels) {

        if (channels == null || channels.isEmpty()) {
            return channels;
        }

        Collections.sort(channels, (o1, o2) -> {
            if (o1.getDate() == null) return -1;
            if (o2.getDate() == null) return 1;
            return o1.getDate().compareTo(o2.getDate()); // small → big
        });

        return channels;
    }
}
