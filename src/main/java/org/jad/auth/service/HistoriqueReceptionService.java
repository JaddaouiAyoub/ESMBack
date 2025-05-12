package org.jad.auth.service;

import lombok.RequiredArgsConstructor;
import org.jad.auth.entity.HistoriqueReception;
import org.jad.auth.repository.HistoriqueReceptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class HistoriqueReceptionService {

    private final HistoriqueReceptionRepository historiqueReceptionRepository;

    public Page<HistoriqueReception> getAllHistorique(Pageable pageable) {
        return historiqueReceptionRepository.findAll(pageable);
    }

    public Page<HistoriqueReception> getHistoriqueFiltre(LocalDate dateDebut, LocalDate dateFin, Pageable pageable) {
        if (dateDebut != null && dateFin != null) {
            return historiqueReceptionRepository.findByDateReceptionBetween(dateDebut, dateFin, pageable);
        } else if (dateDebut != null) {
            return historiqueReceptionRepository.findByDateReceptionAfter(dateDebut.minusDays(1), pageable);
        } else if (dateFin != null) {
            return historiqueReceptionRepository.findByDateReceptionBefore(dateFin.plusDays(1), pageable);
        } else {
            return historiqueReceptionRepository.findAll(pageable);
        }
    }
}
