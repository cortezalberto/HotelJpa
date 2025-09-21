package org.jcr.entidades;



import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"hotel", "reservas"})
@EqualsAndHashCode(exclude = {"hotel", "reservas"})
public class Habitacion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int numero;

    @Column(nullable = false)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new java.util.ArrayList<>();

    public void addReserva(Reserva reserva) {
        this.reservas.add(reserva);
        reserva.setHabitacion(this);
    }
}
