package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.UserDefinedListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDefinedListItemRepository extends JpaRepository<UserDefinedListItem, Long> {
    
    List<UserDefinedListItem> findByUser_IdAndListType(Long userId, String listType);
    
    @Query("SELECT DISTINCT u.listType FROM UserDefinedListItem u WHERE u.user.id = :userId")
    List<String> findDistinctListTypesByUserId(@Param("userId") Long userId);
}
