package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.order.domain.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long>, CommentRepositoryCustom {
}
