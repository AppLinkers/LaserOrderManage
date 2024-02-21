package com.laser.ordermanage.common;

import com.laser.ordermanage.common.config.QuerydslTestConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Import(QuerydslTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryUnitTest {
}
