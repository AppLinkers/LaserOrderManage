package com.laser.ordermanage.common;

import com.laser.ordermanage.common.config.QuerydslTestConfig;
import org.assertj.core.api.Assertions;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Import(QuerydslTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryUnitTest {

    public void assertPage(Pageable pageable, List<?> expectedList, Page<?> actualResponse) {
        Assertions.assertThat(actualResponse.getNumber()).isEqualTo(pageable.getPageNumber());
        Assertions.assertThat(actualResponse.getNumberOfElements()).isEqualTo(expectedList.size());
        Assertions.assertThat(actualResponse.getContent()).isEqualTo(expectedList);
    }
}
