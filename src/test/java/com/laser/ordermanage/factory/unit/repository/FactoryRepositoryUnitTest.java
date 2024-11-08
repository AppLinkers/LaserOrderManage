package com.laser.ordermanage.factory.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.domain.FactoryBuilder;
import com.laser.ordermanage.factory.repository.FactoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = FactoryRepository.class)
public class FactoryRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private FactoryRepository factoryRepository;

    @Test
    public void findFactoryByFactoryManagerUserEmail_존재_O() {
        // given
        final Long expectedFactoryId = 1L;
        final Factory expectedFactory = FactoryBuilder.build();
        final String factoryManagerUserEmail = "admin@kumoh.org";

        // when
        final Optional<Factory> optionalFactory = factoryRepository.findFactoryByFactoryManagerUserEmail(factoryManagerUserEmail);

        // then
        Assertions.assertThat(optionalFactory.isPresent()).isTrue();
        optionalFactory.ifPresent(
                actualFactory -> {
                    Assertions.assertThat(actualFactory.getId()).isEqualTo(expectedFactoryId);
                    FactoryBuilder.assertFactory(actualFactory, expectedFactory);
                }
        );
    }

    @Test
    public void findFactoryByFactoryManagerUserEmail_존재_X() {
        // given
        final String unknownUserEmail = "unknown-user@gmail.com";

        // when
        final Optional<Factory> optionalFactory = factoryRepository.findFactoryByFactoryManagerUserEmail(unknownUserEmail);

        // then
        Assertions.assertThat(optionalFactory.isEmpty()).isTrue();
    }
}
