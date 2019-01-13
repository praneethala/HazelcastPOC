package com.GlobalPayments.HazelcastPOC.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GlobalPayments.HazelcastPOC.model.Token;
import com.GlobalPayments.HazelcastPOC.services.TokenGenerator;
import com.GlobalPayments.HazelcastPOC.services.TokenJDBCRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IdGenerator;

@RestController
@RequestMapping(value = "/rest")
public class MyController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TokenGenerator tokenGenerator;

	@Autowired
	TokenJDBCRepository repository;

	@RequestMapping(value = "/welcome")
	public String welcome() {
		return "welcome to nnn new this" + tokenGenerator.generateRandomTokenSeeds();
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/createHazleCastInstances")
	public StringBuffer createHazleCastInstances() {

		HazelcastInstance instance = Hazelcast.newHazelcastInstance();
		HazelcastInstance instance2 = Hazelcast.newHazelcastInstance();
		StringBuffer sb = new StringBuffer();

		Map<Long, String> map = instance.getMap("a");
		IdGenerator gen = instance.getIdGenerator("gen");
		for (int i = 0; i < 10; i++) {
			map.put(gen.newId(), "  stuff here " + i);
		}
		map.put(gen.newId(), "Adding another extra value");
		Map<Long, String> map2 = instance2.getMap("a");
		for (Map.Entry<Long, String> entry : map2.entrySet()) {
			sb.append("entry: %d; %s\n" + entry.getKey() + entry.getValue() + "%s\n");
		}

		System.out.println("********************* the cache ******************");

		return sb;
	}

	public void insertTokens() {
		Map<String, String[]> randomTokenGenerator = new HashMap<String, String[]>();
		String[] tokenMerchantandGlobal = new String[] { tokenGenerator.generateRandomTokenSeeds(),
				tokenGenerator.generateRandomTokenSeeds() };
		// Hashmap to insert values into the map
		for (int i = 0; i < 10; i++) {
			randomTokenGenerator.put(tokenGenerator.generateRandomTokenSeeds(), tokenMerchantandGlobal);
		}

		// save into cache and return..

		// Method 1:
		// TODO 1: Insert Directly into database/api.
		// TODO 2: Check Inserted data in Cache already in both maps(Global and
		// Merchant).
		// IF Existis in Cache, then also insert in Cache Map.
		// If Not Exist, dont insert the data into cache.

		// Method 2:
		// Insert Directly into Database.
		// Retrieve all records from database and stroe in Cache.

		// Method 3:
		//

	}

	@RequestMapping(value = "/getPANTokenbyGlobalToken/{globalToken}")
	public String getPANTokenbyGlobalToken(@PathVariable("globalToken") String globalToken) {
		final long startTime = System.currentTimeMillis();
		HazelcastInstance instance = Hazelcast.newHazelcastInstance();
		List<Token> tokens = null;
		Map map = instance.getMap("GlobalToken");
		if (map.containsKey(globalToken)) {
			tokens = (List<Token>) map.get(globalToken);
		} else {
			tokens = repository.findByGlobalToken(globalToken);
			map.put(globalToken, tokens);
		}

		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: getPANTokenbyGlobalToken " + (endTime - startTime));
		StringBuffer sb=new StringBuffer();
		sb.append("Total execution time: getPANTokenbyMerchantToken " + (endTime - startTime)+" :: TotalRecords : "+tokens.size()+"<br /><br />");
		for(Token t: tokens) {
			sb.append(t+"<br />");
		}
		instance.shutdown();
		return sb.toString();
	}

	@RequestMapping(value = "/getPANTokenbyMerchantToken/{merchantToken}")
	public String getPANTokenbyMerchantToken(@PathVariable("merchantToken") String merchantToken) {
		final long startTime = System.currentTimeMillis();
		HazelcastInstance instance = Hazelcast.newHazelcastInstance();
		List<Token> tokens = null;
		Map map = instance.getMap("MerchantToken");
		if (map.containsKey(merchantToken)) {
			tokens = (List<Token>) map.get(merchantToken);
		} else {
			tokens = repository.findByMerchantToken(merchantToken);
			map.put(merchantToken, tokens);
		}

		final long endTime = System.currentTimeMillis();
			
//		System.out.println("Total execution time: getPANTokenbyMerchantToken " + (endTime - startTime));
		StringBuffer sb=new StringBuffer();
		sb.append("Total execution time: getPANTokenbyMerchantToken " + (endTime - startTime)+" :: TotalRecords : "+tokens.size()+"<br /><br />");
		for(Token t: tokens) {
			sb.append(t+"<br />");
		}
		instance.shutdown();
		return sb.toString();

	}

//	public void getPANTokenbyMap(@PathVariable("globalToken") String globalToken) {
//		final long startTime = System.currentTimeMillis();
//		HazelcastInstance instance = Hazelcast.newHazelcastInstance();
//		Map panMap = instance.getMap("PANToken");
//		if (panMap.containsKey(merchantToken)) {
//			tokens = (List<Token>) panMap.get(merchantToken);
//		} else {
//			tokens = repository.findByMerchantToken(merchantToken);
//			panMap.put(merchantToken, tokens);
//		}
//
//		
//		final long endTime = System.currentTimeMillis();
//
//		System.out.println("Total execution time: getPANTokenbyMap " + (endTime - startTime));
//	}

	@RequestMapping(value = "/dbCalls")
	public StringBuffer dbCalls() {

		StringBuffer sb = new StringBuffer();
		sb.append("Token id 10001 -> {}" + repository.findByPANToken("10010"));
		sb.append("All users 1 -> {}" + repository.findAll());
		sb.append("Inserting -> {}" + repository.insert(new Token("10010L", "John", "A1234657")));
		sb.append("Update 10001 -> {}" + repository.update(new Token("10001L", "Name-Updated", "New-Passport")));
//	        repository.deleteById("");
		sb.append("All users 2 -> {}" + repository.findAll());

		return sb;
	}

	@RequestMapping(value = "/insertrecords")
	public void insert1000RandomTokens() {
		final long startTime = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++) {

			repository.insert(
					new Token(tokenGenerator.generateRandomTokenSeeds(), (1 + i / 100) + "", (1 + i / 1000) + ""));
		}
		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: insert1000RandomTokens " + (endTime - startTime));
	}
	
	private void checkAndInsertIntoMap(Map tokenMap, String key,Token t) {
		if(tokenMap.containsKey(key)) {
			List<Token> list=(List<Token>) tokenMap.get(key);
			list.add(t);
			tokenMap.remove(key);
			tokenMap.put(key, list);
		}
		else {
			List<Token> list=new ArrayList<Token>();
			list.add(t);
			tokenMap.put(key, list);
		}
	}
	
	@RequestMapping(value = "/putTokens/{pToken}/{mToken}/{gToken}")
	public void insertTokens(@PathVariable("pToken") String pToken,@PathVariable("mToken") String mToken,@PathVariable("gToken") String gToken) {
		final long startTime = System.currentTimeMillis();
		HazelcastInstance instance = Hazelcast.newHazelcastInstance();
		Map pMap = instance.getMap("PANToken");
		Map mMap = instance.getMap("MerchantToken");
		Map gMap = instance.getMap("GlobalToken");
		Token t=new Token(pToken, mToken, gToken);
		checkAndInsertIntoMap(pMap,pToken,t);
		checkAndInsertIntoMap(mMap,mToken,t);
		checkAndInsertIntoMap(gMap,gToken,t);
		repository.insert(t);
		
		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: insert1000RandomTokens " + (endTime - startTime));
	}
}
