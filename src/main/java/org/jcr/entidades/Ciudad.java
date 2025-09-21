package org.jcr.entidades;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ciudad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Added ID for JPA

    @Column(nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "ciudad", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Avoid circular dependency in toString
    private java.util.List<Hotel> hoteles = new java.util.ArrayList<>();

    public void agregarHotel(Hotel hotel) {
        if (hotel != null && !hoteles.contains(hotel)) {
            hoteles.add(hotel);
            hotel.setCiudad(this);
        }
    }
}
