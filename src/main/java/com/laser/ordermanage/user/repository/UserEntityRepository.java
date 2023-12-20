package com.laser.ordermanage.user.repository;

import com.laser.ordermanage.user.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserEntityRepository extends CrudRepository<UserEntity, Long>, UserEntityRepositoryCustom{

    Optional<UserEntity> findFirstByEmail(String email);
}
