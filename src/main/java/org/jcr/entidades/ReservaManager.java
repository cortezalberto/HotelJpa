package org.jcr.entidades;



import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.Getter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
public class ReservaManager implements ReservaService {

    private final EntityManager entityManager;

    public ReservaManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Reserva crearReserva(Cliente cliente, Habitacion habitacion, LocalDate checkIn, LocalDate checkOut, BigDecimal precio) throws ReservaException {

        validarReserva(checkIn, checkOut, precio);

        // Verificar disponibilidad (esto requeriría una lógica más compleja para evitar concurrencia)
        if (!esHabitacionDisponible(habitacion, checkIn, checkOut)) {
            throw new ReservaException("La habitación no está disponible en las fechas solicitadas.");
        }

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setHabitacion(habitacion);
        reserva.setCheckIn(checkIn);
        reserva.setCheckOut(checkOut);
        reserva.setPrecio(precio);

        entityManager.persist(reserva);
        cliente.getReservas().add(reserva);  // Update relationship
        habitacion.getReservas().add(reserva); //Update relationship

        entityManager.merge(cliente); // Persist changes on cliente
        entityManager.merge(habitacion); //Persist changes on habitacion


        return reserva;
    }

    private void validarReserva(LocalDate checkIn, LocalDate checkOut, BigDecimal precio) throws ReservaException {
        if (checkIn.isAfter(checkOut)) {
            throw new ReservaException("La fecha de check-in no puede ser posterior a la fecha de check-out.");
        }

        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReservaException("El precio debe ser mayor que cero.");
        }
    }

    private boolean esHabitacionDisponible(Habitacion habitacion, LocalDate checkIn, LocalDate checkOut) {
        TypedQuery<Reserva> query = entityManager.createQuery(
                "SELECT r FROM Reserva r WHERE r.habitacion = :habitacion " +
                        "AND ((r.checkOut > :checkIn AND r.checkIn < :checkOut))", Reserva.class);
        query.setParameter("habitacion", habitacion);
        query.setParameter("checkIn", checkIn);
        query.setParameter("checkOut", checkOut);
        List<Reserva> reservasExistentes = query.getResultList();

        return reservasExistentes.isEmpty();
    }

    @Override
    public List<Reserva> getReservasPorCliente(Cliente cliente) {
        TypedQuery<Reserva> query = entityManager.createQuery(
                "SELECT r FROM Reserva r WHERE r.cliente = :cliente", Reserva.class);
        query.setParameter("cliente", cliente);
        return query.getResultList();
    }

    @Override
    public List<Reserva> getReservasPorHabitacion(Habitacion habitacion) {
        TypedQuery<Reserva> query = entityManager.createQuery(
                "SELECT r FROM Reserva r WHERE r.habitacion = :habitacion", Reserva.class);
        query.setParameter("habitacion", habitacion);
        return query.getResultList();
    }

    @Override
    public void guardarReservas(String filename) throws IOException {
        // Not applicable in JPA-based implementation
        throw new UnsupportedOperationException("GuardarReservas no soportado en esta implementacion");
    }

    @Override
    public void cargarReservas(String filename, Map<String, Cliente> clientes, Map<Integer, Habitacion> habitaciones) throws IOException, ClassNotFoundException, ReservaException {
        // Not applicable in JPA-based implementation
        throw new UnsupportedOperationException("CargarReservas no soportado en esta implementacion");
    }
}
