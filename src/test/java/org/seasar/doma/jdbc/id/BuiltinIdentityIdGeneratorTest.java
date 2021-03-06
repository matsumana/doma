/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.jdbc.id;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.entity.EntityType;

import example.entity.IdGeneratedEmp;
import example.entity._IdGeneratedEmp;

/**
 * @author taedium
 * 
 */
public class BuiltinIdentityIdGeneratorTest extends TestCase {

    public void test_identitySelectSql() throws Exception {
        MockConfig config = new MockConfig();
        config.setDialect(new PostgresDialect());
        MockResultSet resultSet = config.dataSource.connection.preparedStatement.resultSet;
        resultSet.rows.add(new RowData(11L));

        BuiltinIdentityIdGenerator identityIdGenerator = new BuiltinIdentityIdGenerator();
        IdGenerationConfig idGenerationConfig = new IdGenerationConfig(config,
                _IdGeneratedEmp.getSingletonInternal());
        Long value = identityIdGenerator.generatePostInsert(idGenerationConfig,
                config.dataSource.connection.preparedStatement);
        assertEquals(new Long(11), value);
        assertEquals("select currval('\"CATA\".\"EMP_ID_seq\"')",
                config.dataSource.connection.preparedStatement.sql);
    }

    public void test_identityReservationSql() throws Exception {
        MockConfig config = new MockConfig();
        config.setDialect(new PostgresDialect());
        MockResultSet resultSet = config.dataSource.connection.preparedStatement.resultSet;
        resultSet.rows.add(new RowData(11L));
        resultSet.rows.add(new RowData(12L));
        resultSet.rows.add(new RowData(13L));

        EntityType<IdGeneratedEmp> entityType = _IdGeneratedEmp
                .getSingletonInternal();
        BuiltinIdentityIdGenerator identityIdGenerator = new BuiltinIdentityIdGenerator();
        IdGenerationConfig idGenerationConfig = new IdGenerationConfig(config,
                entityType, new ReservedIdProvider(config, entityType, 3));
        Long value = identityIdGenerator.generatePreInsert(idGenerationConfig);
        assertEquals(new Long(11), value);
        assertEquals(
                "select nextval('\"CATA\".\"EMP_ID_seq\"') from generate_series(1, 3)",
                config.dataSource.connection.preparedStatement.sql);
        value = identityIdGenerator.generatePreInsert(idGenerationConfig);
        assertEquals(new Long(12), value);
        value = identityIdGenerator.generatePreInsert(idGenerationConfig);
        assertEquals(new Long(13), value);

        try {
            identityIdGenerator.generatePreInsert(idGenerationConfig);
            fail();
        } catch (IllegalStateException ignored) {
            System.out.println(ignored);
        }
    }

}
