package org.dainst.gazetteer.harvest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dainst.gazetteer.dao.HarvesterDefinitionRepository;
import org.dainst.gazetteer.dao.PlaceRepository;
import org.dainst.gazetteer.dao.ThesaurusRepository;
import org.dainst.gazetteer.domain.HarvesterDefinition;
import org.dainst.gazetteer.domain.Place;
import org.dainst.gazetteer.domain.Thesaurus;
import org.dainst.gazetteer.helpers.EntityIdentifier;
import org.dainst.gazetteer.helpers.IdGenerator;
import org.dainst.gazetteer.helpers.Merger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HarvestingHandler implements Runnable {
	
	private static Logger logger = LoggerFactory.getLogger(HarvestingHandler.class);
	
	private HarvesterDefinition harvesterDefinition;
	
	private PlaceRepository placeDao;

	private ThesaurusRepository thesaurusDao;

	private HarvesterDefinitionRepository harvesterDefinitionDao;

	private EntityIdentifier entityIdentifier;

	private IdGenerator idGenerator;
	
	private Merger merger;
	
	public HarvestingHandler(HarvesterDefinition harvesterDefinition,
			PlaceRepository placeDao, ThesaurusRepository thesaurusDao, 
			HarvesterDefinitionRepository harvesterDefinitionDao,
			IdGenerator idGenerator,
			EntityIdentifier entityIdentifier,
			Merger merger) {
		
		this.harvesterDefinition = harvesterDefinition;
		this.placeDao = placeDao;	
		this.thesaurusDao = thesaurusDao;
		this.harvesterDefinitionDao = harvesterDefinitionDao;
		this.idGenerator = idGenerator;
		this.entityIdentifier = entityIdentifier;
		this.merger = merger;
		
	}

	@Override
	public void run() {
		
		try {
			
			// get current harvesterDefinition from DB
			harvesterDefinition = harvesterDefinitionDao.getByName(harvesterDefinition.getName());
			
			// prevent multiple instances of the same harvester from being executed simultaneously
			if (!harvesterDefinition.isEnabled()) {
				logger.info("Harvester {} is disabled. Skipping execution ...",
						harvesterDefinition.getName());
				return;
			}
			
			if (logger.isInfoEnabled()) {
				String formattedDate = "never";
				if (harvesterDefinition.getLastHarvestedDate() != null)
					formattedDate = new SimpleDateFormat().format(harvesterDefinition.getLastHarvestedDate());
				logger.info("Running {}. Last time run: {}", 
						harvesterDefinition.getName(), formattedDate);
			}
			
			harvesterDefinition.setEnabled(false);
			harvesterDefinitionDao.save(harvesterDefinition);
			
			Thesaurus thesaurus = thesaurusDao.getThesaurusByKey(harvesterDefinition.getTargetThesaurus());
			if (thesaurus == null) {
				throw new IllegalStateException("Target thesaurus not found in database");
			}
			
			@SuppressWarnings("unchecked")
			Class<Harvester> clazz = (Class<Harvester>) Class.forName(harvesterDefinition.getHarvesterType());
			Harvester harvester = clazz.newInstance();
			harvester.harvest(harvesterDefinition.getLastHarvestedDate());
			harvester.setIdGenerator(idGenerator);
			
			while (true) {
				
				List<Place> candidatePlaces = harvester.getNextPlaces();
				
				if (candidatePlaces == null || candidatePlaces.isEmpty()) break;
				
				List<Place> places = new ArrayList<Place>();
				
				// perform entity identification
				for (Place candidatePlace : candidatePlaces) {
					
					logger.debug("got place from harvester: {}", candidatePlace);
					
					Place identifiedPlace = entityIdentifier.identify(candidatePlace, thesaurus);
					if (identifiedPlace != null) {
						logger.info("identified place: {}", identifiedPlace);
						Place mergedPlace = merger.merge(identifiedPlace, candidatePlace);
						mergedPlace.setId(identifiedPlace.getId());
						// replace id in other places in the result
						for (Place place : candidatePlaces) {
							if (place.getParent() != null && place.getParent().equals(candidatePlace.getId()))
								place.setParent(mergedPlace.getId());
							if (place.getChildren().contains(candidatePlace.getId())) {
								place.getChildren().remove(candidatePlace.getId());
								place.getChildren().add(mergedPlace.getId());
							}
						}
						places.add(mergedPlace);
					} else {
						places.add(candidatePlace);
					}
				}
				
				// save places
				for (Place place : places) {					
					place.setThesaurus(thesaurus.getKey());				
					placeDao.save(place);
					logger.info("saved place: {}", place.getId());
				}
				
			}
			
			harvester.close();
			
			harvesterDefinition.setLastHarvestedDate(new Date());
			harvesterDefinition.setEnabled(true);
			harvesterDefinitionDao.save(harvesterDefinition);
			
		} catch (Exception e) {
			//harvesterDefinition.setRunning(false);
			//harvesterDefinitionDao.save(harvesterDefinition);
			throw new RuntimeException("error while creating harvester", e);
		}
		
	}

}