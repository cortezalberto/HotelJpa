package org.jcr.entidades;

import jakarta.persistence.*;


import lombok.*;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"ciudad", "habitaciones", "empleados"}) // Prevent circular dependency
@EqualsAndHashCode(exclude = "ciudad") // Prevent circular dependency

@Entity
public class Hotel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Added ID for JPA

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Habitacion> habitaciones = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Empleado> empleados = new java.util.ArrayList<>();


}
