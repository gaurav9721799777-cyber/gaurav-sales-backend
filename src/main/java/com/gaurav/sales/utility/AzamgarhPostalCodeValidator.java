package com.gaurav.sales.utility;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.gaurav.sales.exceptions.ResourceNotFoundException;

@Component
public final class AzamgarhPostalCodeValidator {

	private AzamgarhPostalCodeValidator() {
	}

	private static final Set<String> ALLOWED_POSTAL_CODES = Set.of("221602", "221603", "221706", "223221", "223222",
			"223223", "223224", "223225", "223226", "223227", "275101", "275301", "275302", "275304", "275305",
			"275306", "275307", "276001", "276121", "276122", "276123", "276124", "276125", "276126", "276127",
			"276128", "276129", "276131", "276135", "276136", "276137", "276138", "276139", "276140", "276141",
			"276142", "276143", "276201", "276202", "276203", "276204", "276205", "276206", "276207", "276208",
			"276288", "276301", "276302", "276303", "276304", "276305", "276306", "276402", "276403", "276404",
			"276405", "276406", "281401");

	public static void isValid(String city, String postalCode) {

		if (city == null || city.isBlank()) {
			throw new IllegalArgumentException("City is required");
		}

		if (!"Azamgarh".equalsIgnoreCase(city.trim())) {
			throw new IllegalArgumentException("Delivery is available only in Azamgarh");
		}

		if (postalCode == null || postalCode.isBlank()) {
			throw new IllegalArgumentException("Postal code is required");
		}

		String normalizedPostalCode = postalCode.trim();

		if (!ALLOWED_POSTAL_CODES.contains(normalizedPostalCode)) {
			throw new ResourceNotFoundException("Delivery is only available for Azamgarh district!!!  ");
		}
	}
}