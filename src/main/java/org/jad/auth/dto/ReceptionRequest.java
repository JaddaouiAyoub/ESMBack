package org.jad.auth.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReceptionRequest {
    private int quantiteRecue;
    private LocalDate dateReception;
}
