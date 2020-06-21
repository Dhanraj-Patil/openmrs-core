/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package liquibase.ext.change.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.util.H2DatabaseIT;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class InsertWithUuidDataChangeDatabaseIT extends H2DatabaseIT {
	
	@Test
	public void shouldInsertUuids() throws Exception {
		this.updateDatabase("org/openmrs/liquibase/liquibase-test-insert-with-uuid.xml");
		
		Map<String, String> expected = new HashMap<>();
		expected.put("alpha", "alpha123-alph-alph-alph-alpha1234567");
		expected.put("bravo", "bravo123-brav-brav-brav-bravo1234567");
		
		Map<String, String> actual = getNamesWithUuids();
		
		assertEquals(3, actual.size());
		assertEquals(expected.get("alpha"), actual.get("alpha"));
		assertEquals(expected.get("bravo"), actual.get("bravo"));
		
		String uuid = actual.get("charlie");
		assertNotNull(uuid);
		try {
			UUID.fromString(uuid);
		}
		catch (RuntimeException re) {
			Assert.fail("uuid generated for name 'charlie' is not valid");
		}
	}
	
	protected Map getNamesWithUuids() throws SQLException {
		Map<String, String> result = new HashMap();
		Connection connection = getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String query = "select * from name_with_uuid";
			statement.execute(query);
			
			ResultSet resultSet = statement.getResultSet();
			while (resultSet.next()) {
				result.put(resultSet.getString(1), resultSet.getString(2));
			}
			
			return result;
		}
		finally {
			connection.close();
		}
	}
}
