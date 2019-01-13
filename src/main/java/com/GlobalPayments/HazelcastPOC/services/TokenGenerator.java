package com.GlobalPayments.HazelcastPOC.services;

import java.util.Random;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/TokenGenerator")
@Component
public class TokenGenerator {
	/**
	 * 
	 * @param candidateChars
	 *            the candidate chars
	 * @param length
	 *            the number of random chars to be generated
	 * 
	 * @return
	 */
	@RequestMapping(value = "/generateRandomTokenSeeds")
	public static String generateRandomTokenSeeds() {
		String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i <16; i++) {
	        sb.append(candidateChars.charAt(random.nextInt(candidateChars
	                .length())));
	    }

	    return sb.toString();
	}
}
