package org.jad.auth.controller;

import lombok.RequiredArgsConstructor;
import org.jad.auth.entity.HistoriqueReception;
import org.jad.auth.service.HistoriqueReceptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/historique-receptions")
@RequiredArgsConstructor
public class HistoriqueReceptionController {

    private final HistoriqueReceptionService historiqueReceptionService;

    @GetMapping("/all")
    public Page<HistoriqueReception> getHistorique(Pageable pageable) {
        return historiqueReceptionService.getAllHistorique(pageable);
    }
    @GetMapping
    public Page<HistoriqueReception> getHistorique(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            Pageable pageable) {
        return historiqueReceptionService.getHistoriqueFiltre(dateDebut, dateFin, pageable);
    }

}
