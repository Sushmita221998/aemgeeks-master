package com.aem.geeks.core.services;

import com.aem.geeks.core.models.SportsChannel;

import java.util.List;

public interface SportService {

    List<SportsChannel> sortByDate(List<SportsChannel> channels);
}
