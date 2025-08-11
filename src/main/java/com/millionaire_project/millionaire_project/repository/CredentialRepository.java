package com.millionaire_project.millionaire_project.repository;

import com.millionaire_project.millionaire_project.entity.CredentialEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialEntity, Integer> {

    @Query(value = "SELECT * FROM credential c " +
            "WHERE (:id IS NULL OR c.id = :id) " +
            "AND (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:providerName IS NULL OR LOWER(c.provider_name) LIKE LOWER(CONCAT('%', :providerName, '%'))) " +
            "AND (:refreshType IS NULL OR c.refresh_type = :refreshType) " +
            "AND (:active IS NULL OR c.active = :active)", nativeQuery = true)
    Page<CredentialEntity> findByFilterProperties(
            @Param("id") Integer id,
            @Param("email") String email,
            @Param("providerName") String providerName,
            @Param("refreshType") String refreshType,
            @Param("active") Boolean active,
            Pageable pageable
    );

    @Query(value = "SELECT * FROM credential c " +
            "WHERE LOWER(c.provider_name) = LOWER(:providerName) " +
            "AND c.active = TRUE", nativeQuery = true)
    List<CredentialEntity> findByProviderName(@Param("providerName") String providerName);
}
