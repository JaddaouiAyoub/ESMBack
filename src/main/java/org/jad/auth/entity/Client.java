package org.jad.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor


public class Client extends User {

//    @OneToMany(mappedBy = "client")
//    private List<Commande> commandes;  // Client peut avoir plusieurs commandes
}
