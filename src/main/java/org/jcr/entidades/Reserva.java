package org.jcr.entidades;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Added ID for JPA

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @ToString.Exclude // Avoid potential circular toString
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = false)
    @ToString.Exclude // Avoid potential circular toString
    private Habitacion habitacion;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private BigDecimal precio;
}
