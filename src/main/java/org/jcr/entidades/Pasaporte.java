package org.jcr.entidades;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pasaporte implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Added ID for JPA

    @Column(nullable = false, unique = true)
    private String numero;
}
