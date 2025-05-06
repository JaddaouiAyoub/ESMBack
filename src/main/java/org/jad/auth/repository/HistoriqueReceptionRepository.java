package org.jad.auth.repository;

import org.jad.auth.entity.HistoriqueReception;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueReceptionRepository extends PagingAndSortingRepository<HistoriqueReception, Long>, JpaRepository<HistoriqueReception, Long> {
}
