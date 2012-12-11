package org.dainst.gazetteer.controller;

import javax.servlet.http.HttpServletResponse;

import org.dainst.gazetteer.dao.PlaceRepository;
import org.dainst.gazetteer.domain.Place;
import org.dainst.gazetteer.helpers.IdGenerator;
import org.dainst.gazetteer.helpers.Merger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MergeController {

	private static final Logger logger = LoggerFactory.getLogger(MergeController.class);
	
	@Autowired
	private PlaceRepository placeDao;
	
	@Autowired
	private Merger merger;
	
	@Autowired
	private IdGenerator idGenerator;
	
	@Value("${baseUri}")
	private String baseUri;

	@RequestMapping(value="/merge/{id1}/{id2}", method=RequestMethod.POST)
	public ModelAndView getPlace(@PathVariable String id1,
			@PathVariable String id2,
			HttpServletResponse response) {
		
		Place place1 = placeDao.findOne(id1);
		Place place2 = placeDao.findOne(id2);
		
		Place newPlace = merger.merge(place1, place2);
		newPlace.setId(idGenerator.generate(newPlace));
		placeDao.save(newPlace);
		
		Place parent = placeDao.findOne(newPlace.getParent());
		if (parent != null) {
			parent.getChildren().remove(id1);
			parent.getChildren().remove(id2);
			parent.addChild(newPlace.getId());
		}
		
		for (String childId : newPlace.getChildren()) {
			Place child = placeDao.findOne(childId);
			child.setParent(newPlace.getId());
			placeDao.save(child);
		}
		
		for (String relatedPlaceId : newPlace.getRelatedPlaces()) {
			Place relatedPlace = placeDao.findOne(relatedPlaceId);
			relatedPlace.getRelatedPlaces().remove(id1);
			relatedPlace.getRelatedPlaces().remove(id2);
			relatedPlace.getRelatedPlaces().add(newPlace.getId());
			placeDao.save(relatedPlace);
		}
		
		place1.setReplacedBy(newPlace.getId());
		place1.setDeleted(true);
		placeDao.save(place1);
		place2.setReplacedBy(newPlace.getId());
		place2.setDeleted(true);
		placeDao.save(place2);
		
		response.setStatus(201);
		response.setHeader("location", baseUri + "place/" + newPlace.getId());
		
		ModelAndView mav = new ModelAndView("place/get");
		mav.addObject("place", newPlace);
		mav.addObject("baseUri", baseUri);
		return mav;
		
	}
	
}
