package org.jcr.entidades;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

interface ReservaService {
    Reserva crearReserva(Cliente cliente, Habitacion habitacion, LocalDate checkIn, LocalDate checkOut, BigDecimal precio) throws ReservaException;
    List<Reserva> getReservasPorCliente(Cliente cliente);
    List<Reserva> getReservasPorHabitacion(Habitacion habitacion);
    void guardarReservas(String filename) throws IOException;
    void cargarReservas(String filename, Map<String, Cliente> clientes, Map<Integer, Habitacion> habitaciones) throws IOException, ClassNotFoundException, ReservaException;
}
