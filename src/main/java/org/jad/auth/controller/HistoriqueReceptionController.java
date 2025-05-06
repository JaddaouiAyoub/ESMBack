package org.jad.auth.controller;

import lombok.RequiredArgsConstructor;
import org.jad.auth.entity.HistoriqueReception;
import org.jad.auth.service.HistoriqueReceptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/historique-receptions")
@RequiredArgsConstructor
public class HistoriqueReceptionController {

    private final HistoriqueReceptionService historiqueReceptionService;

    @GetMapping
    public Page<HistoriqueReception> getHistorique(Pageable pageable) {
        return historiqueReceptionService.getAllHistorique(pageable);
    }
}
