package org.jad.auth.service;

import lombok.RequiredArgsConstructor;
import org.jad.auth.entity.HistoriqueReception;
import org.jad.auth.repository.HistoriqueReceptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoriqueReceptionService {

    private final HistoriqueReceptionRepository historiqueReceptionRepository;

    public Page<HistoriqueReception> getAllHistorique(Pageable pageable) {
        return historiqueReceptionRepository.findAll(pageable);
    }
}
