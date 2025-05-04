package org.jad.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.jad.auth.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)  // <<< Ajoute cette ligne pour dire à JPA de gérer l'héritage
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // <<<<<< AU LIEU DE IDENTITY
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role.getName()));
    }
}
