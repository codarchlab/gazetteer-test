package org.dainst.gazetteer.helpers;

import java.util.List;

import org.dainst.gazetteer.dao.PlaceRepository;
import org.dainst.gazetteer.domain.Identifier;
import org.dainst.gazetteer.domain.Place;
import org.dainst.gazetteer.domain.PlaceName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SimpleNameAndIdBasedEntityIdentifier implements EntityIdentifier {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleNameAndIdBasedEntityIdentifier.class);
	
	@Autowired
	PlaceRepository placeDao;

	@Override
	public Place identify(Place place) {
		
		// identifier equality is a perfect match
		for (Identifier id : place.getIdentifiers()) {
			Place matchedPlace = placeDao.findByIdsAndType(id, place.getType());
			if (matchedPlace != null && id.getValue() != null) {
				logger.debug("matched id: " + id);
				return matchedPlace;
			}
		}
		
		if ("continent".equals(place.getType())) {
			
			// we suppose that the names of continents are unique
			List<Place> resultList = placeDao.findByPrefNameTitleAndType(
					place.getPrefName().getTitle(), "continent");
			logger.debug("matched continents: " + resultList.size());
			if (resultList.size() == 1) return resultList.get(0);
		
		} else if ("country".equals(place.getType())) {

			// we suppose that the names of countries are unique
			List<Place> resultList = placeDao.findByPrefNameTitleAndType(
					place.getPrefName().getTitle(), "country");
			logger.debug("matched countries: " + resultList.size());
			if (resultList.size() == 1) return resultList.get(0);
			
		} else {

			// XXX we suppose that the names of cities in the same country are unique
			List<Place> resultList = placeDao.findByPrefNameTitle(
					place.getPrefName().getTitle());
			resultList.addAll(placeDao.findByNamesTitle(place.getPrefName().getTitle()));
			for (PlaceName name : place.getNames()) {
				resultList.addAll(placeDao.findByPrefNameTitle(name.getTitle()));
				resultList.addAll(placeDao.findByNamesTitle(name.getTitle()));
			}
			logger.debug("matched cities: " + resultList.size());
			
			if (place.getParent() == null) {
				if (resultList.size() == 1) {
					Place candidate = resultList.get(0);
					if (candidate.getParent() == null) return candidate;
				}
			} else {
				for (Place candidate : resultList) {
					if (candidate.getParent() != null
							&& candidate.getParent().equals(place.getParent()))
						return candidate;
				}
			}
			
		}
		
		return null;
		
	}

}
