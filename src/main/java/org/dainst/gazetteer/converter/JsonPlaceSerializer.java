package org.dainst.gazetteer.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dainst.gazetteer.dao.PlaceChangeRecordRepository;
import org.dainst.gazetteer.dao.PlaceRepository;
import org.dainst.gazetteer.dao.UserRepository;
import org.dainst.gazetteer.domain.Comment;
import org.dainst.gazetteer.domain.Identifier;
import org.dainst.gazetteer.domain.Link;
import org.dainst.gazetteer.domain.Location;
import org.dainst.gazetteer.domain.Place;
import org.dainst.gazetteer.domain.PlaceChangeRecord;
import org.dainst.gazetteer.domain.PlaceName;
import org.dainst.gazetteer.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.support.RequestContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonPlaceSerializer {
	
	private final static Logger logger = LoggerFactory.getLogger("org.dainst.gazetteer.JsonPlaceSerializer");

	private String baseUri;
	
	private ObjectMapper mapper;
	
	public JsonPlaceSerializer(String baseUri) {
		this.baseUri = baseUri;
		mapper = new ObjectMapper();
	}
	
	public String serialize(Place place, PlaceRepository placeDao) {
		return serialize(place, null, null, placeDao, null, 1);
	}
	
	public String serialize(Place place, PlaceRepository placeDao, int lod) {
		return serialize(place, null, null, placeDao, null, lod);
	}
	
	public String serialize(Place place, UserRepository userDao, PlaceChangeRecordRepository changeRecordDao, PlaceRepository placeDao, HttpServletRequest request) {
		return serialize(place, userDao, changeRecordDao, placeDao, request, 1);
	}
	
	public String serialize(Place place, UserRepository userDao, PlaceChangeRecordRepository changeRecordDao, PlaceRepository placeDao, HttpServletRequest request, int lod) {
		
		if (place == null) return null;
		
		User user = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User)
			user = (User) principal;
		
		if (!checkPlaceAccess(place, user, placeDao))
			return null;
		
		ObjectNode placeNode = mapper.createObjectNode();
		logger.debug("serializing: {}", place);
		placeNode.put("@id", baseUri + "place/" + place.getId());
		placeNode.put("gazId", place.getId());
		
		if (place.isDeleted()) {
			placeNode.put("deleted", true);
			try {
				return mapper.writeValueAsString(placeNode);
			} catch (Exception e) {
				logger.error("Unable to serialize place in JSON.", e);
				return "";
			}
		}
		
		if (place.getTypes() != null && !place.getTypes().isEmpty()) {
			ArrayNode typesNode = mapper.createArrayNode();
			for (String type : place.getTypes())
				typesNode.add(type);
			placeNode.put("types", typesNode);
		}
		
		// preferred name
		if (place.getPrefName() != null) {
			ObjectNode prefNameNode = mapper.createObjectNode();
			prefNameNode.put("title", place.getPrefName().getTitle());
			if (place.getPrefName().getLanguage() != null)
				prefNameNode.put("language", place.getPrefName().getLanguage());
			if (place.getPrefName().isAncient())
				prefNameNode.put("ancient", true);
			if (place.getPrefName().isTransliterated())
				prefNameNode.put("transliterated", true);
			placeNode.put("prefName", prefNameNode);
		}
		
		// other names
		if (!place.getNames().isEmpty()) {
			ArrayNode namesNode = mapper.createArrayNode();
			for (PlaceName name : place.getNames()) {
				ObjectNode nameNode = mapper.createObjectNode();
				nameNode.put("title", name.getTitle());
				if (name.getLanguage() != null) 
					nameNode.put("language", name.getLanguage());
				if (name.isAncient())
					nameNode.put("ancient", true);
				if (name.isTransliterated())
					nameNode.put("transliterated", true);
				namesNode.add(nameNode);
			}
			placeNode.put("names", namesNode);
		}
		
		// preferred location
		if (place.getPrefLocation() != null) {
			ObjectNode locationNode = mapper.createObjectNode();
			if (place.getPrefLocation().getCoordinates() != null) {
				ArrayNode coordinatesNode = mapper.createArrayNode();
				coordinatesNode.add(place.getPrefLocation().getLng());
				coordinatesNode.add(place.getPrefLocation().getLat());
				locationNode.put("coordinates", coordinatesNode);
			}
			if (place.getPrefLocation().getShape() != null) {
				ArrayNode shapeNode = mapper.createArrayNode();
				for (int i = 0; i < place.getPrefLocation().getShape().getCoordinates().length; i++) {
					ArrayNode shapeCoordinatesNode1 = mapper.createArrayNode();
					for (int j = 0; j < place.getPrefLocation().getShape().getCoordinates()[i].length; j++) {
						ArrayNode shapeCoordinatesNode2 = mapper.createArrayNode();
						for (int k = 0; k < place.getPrefLocation().getShape().getCoordinates()[i][j].length; k++) {
							ArrayNode shapeCoordinatesNode3 = mapper.createArrayNode();
							for (int l = 0; l < place.getPrefLocation().getShape().getCoordinates()[i][j][k].length; l++) {
								shapeCoordinatesNode3.add(place.getPrefLocation().getShape().getCoordinates()[i][j][k][l]);
							}
							shapeCoordinatesNode2.add(shapeCoordinatesNode3);
						}
						shapeCoordinatesNode1.add(shapeCoordinatesNode2);
					}
					shapeNode.add(shapeCoordinatesNode1);
				}
				locationNode.put("shape", shapeNode);
			}								
			locationNode.put("confidence", place.getPrefLocation().getConfidence());
			locationNode.put("publicSite", place.getPrefLocation().isPublicSite());
			locationNode.put("area", 0);
			placeNode.put("prefLocation", locationNode);
		}
		
		// other locations
		if (!place.getLocations().isEmpty()) {
			ArrayNode locationsNode = mapper.createArrayNode();
			for (Location location : place.getLocations()) {
				ObjectNode locationNode = mapper.createObjectNode();
				if (location.getCoordinates() != null) {
					ArrayNode coordinatesNode = mapper.createArrayNode();
					coordinatesNode.add(location.getLng());
					coordinatesNode.add(location.getLat());
					locationNode.put("coordinates", coordinatesNode);
				}
				if (location.getShape() != null) {
					ArrayNode shapeNode = mapper.createArrayNode();
					for (int i = 0; i < location.getShape().getCoordinates().length; i++) {
						ArrayNode shapeCoordinatesNode1 = mapper.createArrayNode();
						for (int j = 0; j < location.getShape().getCoordinates()[i].length; j++) {
							ArrayNode shapeCoordinatesNode2 = mapper.createArrayNode();
							for (int k = 0; k < location.getShape().getCoordinates()[i][j].length; k++) {
								ArrayNode shapeCoordinatesNode3 = mapper.createArrayNode();
								for (int l = 0; l < location.getShape().getCoordinates()[i][j][k].length; l++) {
									shapeCoordinatesNode3.add(location.getShape().getCoordinates()[i][j][k][l]);
								}
								shapeCoordinatesNode2.add(shapeCoordinatesNode3);
							}
							shapeCoordinatesNode1.add(shapeCoordinatesNode2);
						}
						shapeNode.add(shapeCoordinatesNode1);
					}
					locationNode.put("shape", shapeNode);
				}
				locationNode.put("confidence", location.getConfidence());
				locationNode.put("publicSite", location.isPublicSite());
				locationsNode.add(locationNode);
			}
			placeNode.put("locations", locationsNode);
		}
		
		// identifiers
		if (!place.getIdentifiers().isEmpty()) {
			ArrayNode idsNode = mapper.createArrayNode();
			for (Identifier id : place.getIdentifiers()) {
				ObjectNode idNode = mapper.createObjectNode();
				idNode.put("value", id.getValue());
				idNode.put("context", id.getContext());
				idsNode.add(idNode);
			}
			placeNode.put("identifiers", idsNode);
		}
		
		// links
		if (!place.getLinks().isEmpty()) {
			ArrayNode linksNode = mapper.createArrayNode();
			for (Link link : place.getLinks()) {
				ObjectNode linkNode = mapper.createObjectNode();
				linkNode.put("object", link.getObject());
				linkNode.put("predicate", link.getPredicate());
				linksNode.add(linkNode);
			}
			placeNode.put("links", linksNode);
		}
		
		// parent
		if (place.getParent() != null && !place.getParent().isEmpty())
			placeNode.put("parent", baseUri + "place/" + place.getParent());
		
		// related places
		if (!place.getRelatedPlaces().isEmpty()) {
			ArrayNode relatedPlacesNode = mapper.createArrayNode();
			for (String relatedPlaceId : place.getRelatedPlaces()) {
				if (relatedPlaceId != null) {
					relatedPlacesNode.add(baseUri + "place/" + relatedPlaceId);
				}
			}
			placeNode.put("relatedPlaces", relatedPlacesNode);
		}
		
		// comments
		if (!place.getComments().isEmpty()) {
			ArrayNode commentsNode = mapper.createArrayNode();
			for (Comment comment : place.getComments()) {
				ObjectNode commentNode = mapper.createObjectNode();
				commentNode.put("text", comment.getText());
				commentNode.put("language", comment.getLanguage());
				commentsNode.add(commentNode);
			}
			placeNode.put("comments", commentsNode);
		}
		
		// tags
		if (!place.getTags().isEmpty()) {
			ArrayNode tagsNode = mapper.createArrayNode();
			for (String tag : place.getTags()) {
				tagsNode.add(tag);
			}
			placeNode.put("tags", tagsNode);
		}
		
		// provenance
		if (!place.getProvenance().isEmpty()) {
			ArrayNode provenanceNode = mapper.createArrayNode();
			for (String provenanceEntry : place.getProvenance()) {
				provenanceNode.add(provenanceEntry);
			}
			placeNode.put("provenance", provenanceNode);
		}
		
		// user group
		if (place.getUserGroupId() != null && !place.getUserGroupId().isEmpty())
			placeNode.put("userGroupId", place.getUserGroupId());
		
		// reisestipendium content		
		logger.debug("serializing reisestipendium content?");
		if (user != null && user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_REISESTIPENDIUM"))) {
			
			logger.debug("serializing reisestipendium note");
			if (place.getNoteReisestipendium() != null && !place.getNoteReisestipendium().isEmpty()) {
				placeNode.put("noteReisestipendium", place.getNoteReisestipendium());
				logger.debug("serialized reisestipendium note");
			}
				if (!place.getCommentsReisestipendium().isEmpty()) {
				ArrayNode commentsNode = mapper.createArrayNode();
				for (Comment comment : place.getCommentsReisestipendium()) {
					ObjectNode commentNode = mapper.createObjectNode();
					commentNode.put("text", comment.getText());
					commentNode.put("user", comment.getUser());
					commentsNode.add(commentNode);
				}
				placeNode.put("commentsReisestipendium", commentsNode);
			}
		}
		
		// change history
		if (user != null  && user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EDITOR")) && userDao != null && changeRecordDao != null && request != null) {
			logger.debug("serializing change history");
			
			List<PlaceChangeRecord> changeHistory = changeRecordDao.findByPlaceId(place.getId());
			
			if (!changeHistory.isEmpty()) {
				
				Collections.sort(changeHistory, new PlaceChangeRecord.ChangeDateComparator());
				ArrayNode changeHistoryNode = mapper.createArrayNode();
								
				for (PlaceChangeRecord changeRecord : changeHistory) {
					ObjectNode changeRecordNode = mapper.createObjectNode();
				
					User changeRecordUser = userDao.findById(changeRecord.getUserId());
					if (changeRecordUser != null) {
						changeRecordNode.put("username", changeRecordUser.getUsername());
						changeRecordNode.put("userId", changeRecord.getUserId());
					} else {
						RequestContext context = new RequestContext(request);
						changeRecordNode.put("username", context.getMessage("ui.changeHistory.deletedUser"));
						changeRecordNode.put("userId", "");
					}
					
					if (changeRecord.getChangeType() != null)
						changeRecordNode.put("changeType", changeRecord.getChangeType());
					else
						changeRecordNode.put("changeType", "unknown");
				
					DateFormat format = new SimpleDateFormat("dd.MM.yyyy (HH:mm:ss z)");
					changeRecordNode.put("changeDate", format.format(changeRecord.getChangeDate()));
					changeHistoryNode.add(changeRecordNode);
				}
				
				placeNode.put("changeHistory", changeHistoryNode);
			}
		}
				
		try {
			return mapper.writeValueAsString(placeNode);
		} catch (Exception e) {
			logger.error("Unable to serialize place in JSON.", e);
			return "";
		}
		
	}
	
	private boolean checkPlaceAccess(Place place, User user, PlaceRepository placeDao) {
		
		if (place.getUserGroupId() != null && !place.getUserGroupId().isEmpty() && 
				(user == null || !user.getUserGroupIds().contains(place.getUserGroupId())))
			return false;
		
		if (place.getParent() != null && !place.getParent().isEmpty()) {
			Place parent = placeDao.findOne(place.getParent());
			if (parent != null)
				return checkPlaceAccess(parent, user, placeDao);
		}
		
		return true;
	}
	
}
