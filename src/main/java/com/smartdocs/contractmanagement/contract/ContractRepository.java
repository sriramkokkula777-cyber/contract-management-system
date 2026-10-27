package com.smartdocs.contractmanagement.contract;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findByTitleContainingIgnoreCaseOrVendorContainingIgnoreCase(String title, String vendor);

    List<Contract> findByStatus(ContractStatus status);

    @Query("""
            select c from Contract c
            where (lower(c.title) like lower(concat('%', :search, '%'))
                or lower(c.vendor) like lower(concat('%', :search, '%')))
              and c.status = :status
            """)
    List<Contract> searchByTermAndStatus(@Param("search") String search,
                                         @Param("status") ContractStatus status);
}
