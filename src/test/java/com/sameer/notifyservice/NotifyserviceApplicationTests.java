package com.sameer.notifyservice;

import com.sameer.notifyservice.config.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotifyserviceApplicationTests {

	@Autowired
	private JwtUtil jwtUtil;

	@Test
	void testJwtGenerationAndValidation() {
		String testUser = "sameer@test.com";

		// 1. Generate Token
		String token = jwtUtil.generateToken(testUser);
		assertNotNull(token);
		System.out.println("✅ Generated JWT: " + token);

		// 2. Extract Username
		String extractedUser = jwtUtil.extractUsername(token);
		assertEquals(testUser, extractedUser);
		System.out.println("✅ Extracted Subject: " + extractedUser);

		// 3. Validate Token
		boolean isValid = jwtUtil.isTokenValid(token, testUser);
		assertTrue(isValid);
		System.out.println("✅ Token Validity: " + isValid);

		// 4. Invalidation with wrong user
		boolean isInvalid = jwtUtil.isTokenValid(token, "other@test.com");
		assertFalse(isInvalid);
	}
}