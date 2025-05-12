package org.jad.auth.repository;

import org.jad.auth.entity.HistoriqueReception;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface HistoriqueReceptionRepository extends PagingAndSortingRepository<HistoriqueReception, Long>, JpaRepository<HistoriqueReception, Long> {
    Page<HistoriqueReception> findByDateReceptionBetween(LocalDate start, LocalDate end, Pageable pageable);
    Page<HistoriqueReception> findByDateReceptionAfter(LocalDate start, Pageable pageable);
    Page<HistoriqueReception> findByDateReceptionBefore(LocalDate end, Pageable pageable);
}
