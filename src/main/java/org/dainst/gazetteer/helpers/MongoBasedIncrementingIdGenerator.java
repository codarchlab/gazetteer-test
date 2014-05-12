package org.dainst.gazetteer.helpers;

import org.dainst.gazetteer.domain.Place;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;

public class MongoBasedIncrementingIdGenerator implements IdGenerator {
	
	private MongoTemplate mongoTemplate;
	
	private int blockSize = 100;
	
	private long nextId = 0;

	private Counter counter;
	
	public MongoBasedIncrementingIdGenerator(MongoTemplate mongoTemplate, String counterId, long start) {
		this.mongoTemplate = mongoTemplate;
		counter = mongoTemplate.findById(counterId, Counter.class);
		if (counter == null) {
			counter = new Counter();
			counter.setId(counterId);
			counter.setValue(start);
		}
		nextId = counter.getValue();
		allocateBlock();
	}

	@Override
	public String generate(Place place) {
		if (nextId >= counter.getValue()) {
			allocateBlock();
		}
		return String.valueOf(nextId++);
	}
	
	private void allocateBlock() {
		counter.inc(blockSize);
		mongoTemplate.save(counter);
	}

	public void setBlockSize(int step) {
		this.blockSize = step;
	}
	
	@Document
	public static class Counter {
		
		@Id
		private String id;
		
		private long value = 1;
		
		public Counter() {
			
		}
		
		public void inc(int step) {
			this.value += step;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
		
	}

}
