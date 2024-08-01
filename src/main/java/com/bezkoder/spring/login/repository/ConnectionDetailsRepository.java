package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.ConnectionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionDetailsRepository extends JpaRepository<ConnectionDetails, Long> {
}
