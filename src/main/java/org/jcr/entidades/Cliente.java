package org.jcr.entidades;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;




import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = "reservas")
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Persona implements Serializable {
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pasaporte_id")
    private Pasaporte pasaporte;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ObraSocial obraSocial;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new java.util.ArrayList<>();

    public void addReserva(Reserva reserva) {
        this.reservas.add(reserva);
        reserva.setCliente(this);
    }
}
