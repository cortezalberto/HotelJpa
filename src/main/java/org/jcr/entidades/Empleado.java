package org.jcr.entidades;



import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"hotel", "legajo"})
@EqualsAndHashCode(callSuper = true)
public class Empleado extends Persona implements Serializable {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cargos cargo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "legajo_id")
    private Legajo legajo;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;  // Added relationship to Hotel
}
