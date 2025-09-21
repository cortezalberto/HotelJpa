package org.jcr;

import org.jcr.entidades.*;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final Random random = new Random();

    // Helper methods to generate unique values
    private static String generateUniquePasaporteNumber() {
        return "AR-" + String.format("%06d", random.nextInt(1000000));
    }

    private static String generateUniqueLegajoNumber() {
        return String.format("%04d", random.nextInt(10000));
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HotelPU");
        EntityManager em = emf.createEntityManager();
        ReservaManager reservaManager = new ReservaManager(em); // Inject EntityManager

        try {
            em.getTransaction().begin();
            LOGGER.info("Starting data creation and persistence...");

            // --- Create data ---
            Ciudad mendoza = new Ciudad();
            mendoza.setNombre("Mendoza");

            Ciudad sanJuan = new Ciudad();
            sanJuan.setNombre("San Juan");

            Hotel hotelSolAndino = new Hotel();
            hotelSolAndino.setNombre("Sol Andino");
            hotelSolAndino.setCiudad(mendoza); // Set relationship
            mendoza.agregarHotel(hotelSolAndino); // Maintain consistency

            Hotel hotelCordillera = new Hotel();
            hotelCordillera.setNombre("Cordillera Suites");
            hotelCordillera.setCiudad(mendoza);
            mendoza.agregarHotel(hotelCordillera);

            Hotel hotelOeste = new Hotel();
            hotelOeste.setNombre("Oeste");
            hotelOeste.setCiudad(sanJuan);
            sanJuan.agregarHotel(hotelOeste);

            Habitacion hab101 = new Habitacion();
            hab101.setNumero(101);
            hab101.setTipo("Doble");
            hab101.setHotel(hotelSolAndino);
            hotelSolAndino.getHabitaciones().add(hab101);

            Habitacion hab102 = new Habitacion();
            hab102.setNumero(102);
            hab102.setTipo("Suite");
            hab102.setHotel(hotelSolAndino);
            hotelSolAndino.getHabitaciones().add(hab102);

            Habitacion hab201 = new Habitacion();
            hab201.setNumero(201);
            hab201.setTipo("Doble");
            hab201.setHotel(hotelCordillera);
            hotelCordillera.getHabitaciones().add(hab201);

            Habitacion hab301 = new Habitacion();
            hab301.setNumero(301);
            hab301.setTipo("Suite");
            hab301.setHotel(hotelOeste);
            hotelOeste.getHabitaciones().add(hab301);

            Cliente cliente1 = new Cliente();
            cliente1.setNombre("Ana");
            cliente1.setApellido("Gomez");
            cliente1.setObraSocial(ObraSocial.OSDE);
            Pasaporte pasaporte1 = new Pasaporte();
            pasaporte1.setNumero(generateUniquePasaporteNumber()); // Generate unique Pasaporte number
            cliente1.setPasaporte(pasaporte1);

            Cliente cliente2 = new Cliente();
            cliente2.setNombre("Carlos");
            cliente2.setApellido("Lopez");
            cliente2.setObraSocial(ObraSocial.PAMI);
            Pasaporte pasaporte2 = new Pasaporte();
            pasaporte2.setNumero(generateUniquePasaporteNumber()); // Generate unique Pasaporte number
            cliente2.setPasaporte(pasaporte2);

            Empleado empleado1 = new Empleado();
            empleado1.setNombre("Juan");
            empleado1.setApellido("Perez");
            empleado1.setCargo(Cargos.Administrativo);
            Legajo legajo1 = new Legajo();
            legajo1.setNumero(generateUniqueLegajoNumber()); // Generate unique Legajo number
            empleado1.setLegajo(legajo1);
            empleado1.setHotel(hotelSolAndino);
            hotelSolAndino.getEmpleados().add(empleado1);

            Empleado empleado2 = new Empleado();
            empleado2.setNombre("Maria");
            empleado2.setApellido("Rodriguez");
            empleado2.setCargo(Cargos.Conserje);
            Legajo legajo2 = new Legajo();
            legajo2.setNumero(generateUniqueLegajoNumber()); // Generate unique Legajo number
            empleado2.setLegajo(legajo2);
            empleado2.setHotel(hotelCordillera);
            hotelCordillera.getEmpleados().add(empleado2);

            // --- Persist entities in the correct order ---
            em.persist(mendoza);
            LOGGER.info("Persisted: " + mendoza);
            em.persist(sanJuan);
            LOGGER.info("Persisted: " + sanJuan);
            em.persist(hotelSolAndino);
            LOGGER.info("Persisted: " + hotelSolAndino);
            em.persist(hotelCordillera);
            LOGGER.info("Persisted: " + hotelCordillera);
            em.persist(hotelOeste);
            LOGGER.info("Persisted: " + hotelOeste);
            em.persist(hab101);
            LOGGER.info("Persisted: " + hab101);
            em.persist(hab102);
            LOGGER.info("Persisted: " + hab102);
            em.persist(hab201);
            LOGGER.info("Persisted: " + hab201);
            em.persist(hab301);
            LOGGER.info("Persisted: " + hab301);
            em.persist(cliente1);
            LOGGER.info("Persisted: " + cliente1);
            em.persist(cliente2);
            LOGGER.info("Persisted: " + cliente2);
            em.persist(pasaporte1);
            LOGGER.info("Persisted: " + pasaporte1);
            em.persist(pasaporte2);
            LOGGER.info("Persisted: " + pasaporte2);
            em.persist(empleado1);
            LOGGER.info("Persisted: " + empleado1);
            em.persist(empleado2);
            LOGGER.info("Persisted: " + empleado2);
            em.persist(legajo1);
            LOGGER.info("Persisted: " + legajo1);
            em.persist(legajo2);
            LOGGER.info("Persisted: " + legajo2);
            try{
                em.getTransaction().commit();
            } catch(Exception e) {
                if (em != null&& em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                    LOGGER.log(Level.SEVERE, "Global transaction rolled back due to error: " + e.getMessage(), e);
                }
                em.getTransaction().begin();
                Hotel  hotelSolAndino2= em.find(Hotel.class, hotelSolAndino.getId());
                Hotel hotelCordillera2 = em.find(Hotel.class, hotelCordillera.getId());
                Hotel hotelOeste2= em.find(Hotel.class,hotelOeste.getId());
                Habitacion hab1012= em.find(Habitacion.class, hab101.getId());
                Habitacion hab1022 = em.find(Habitacion.class, hab102.getId());
                Habitacion hab2012 = em.find(Habitacion.class, hab201.getId());
                Habitacion hab3012 = em.find(Habitacion.class, hab301.getId());
                em.persist(hotelSolAndino2);
                em.persist(hotelCordillera2);
                em.persist(hotelOeste2);
                em.persist(hab1012);
                em.persist(hab1022);
                em.persist(hab2012);
                em.persist(hab3012);
                em.getTransaction().commit();
            }
            LOGGER.info("All base objects  persisted: " );
            // Create reservations using ReservaManager
            Reserva reserva1 = null;
            Reserva reserva2 = null;

            try {

                reserva1 = reservaManager.crearReserva(cliente1, hab101, LocalDate.of(2025, 9, 10), LocalDate.of(2025, 9, 15), new BigDecimal("250000.00"));
                LOGGER.info("Created and persisted: " + reserva1);
            }catch(Exception e){
                em.getTransaction().rollback();
                em.getTransaction().begin();
                Habitacion habitacion1= em.find(Habitacion.class, hab101.getId());
                if(habitacion1==null) {

                }
                reserva1 = reservaManager.crearReserva(cliente1, habitacion1, LocalDate.of(2025, 9, 10), LocalDate.of(2025, 9, 15), new BigDecimal("250000.00"));
                LOGGER.info("Created and persisted: " + reserva1);
            }

            try {

                reserva2 = reservaManager.crearReserva(cliente2, hab102, LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 5), new BigDecimal("180000.00"));
                LOGGER.info("Created and persisted: " + reserva2);
            } catch(Exception e) {
                em.getTransaction().rollback();
                em.getTransaction().begin();
                Habitacion habitacion2= em.find(Habitacion.class, hab102.getId());
                if(habitacion2==null) {

                }
                reserva2 = reservaManager.crearReserva(cliente2, habitacion2, LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 5), new BigDecimal("180000.00"));
                LOGGER.info("Created and persisted: " + reserva2);
            } finally {
                try {
                    em.getTransaction().commit();
                    LOGGER.info("Transaction  to reserve commited successfully.");
                } catch(Exception e) {
                    if (em != null&& em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                        LOGGER.log(Level.SEVERE, "Transaction rolled back due to error: " + e.getMessage(), e);
                    }
                }
            }
            // Create and persist another entities


            em.getTransaction().commit();
            LOGGER.info("Transaction committed successfully.");

            // --- Query data ---
            LOGGER.info("Starting data querying...");
            System.out.println("--- Ciudades ---");
            TypedQuery<Ciudad> ciudadQuery = em.createQuery("SELECT c FROM Ciudad c", Ciudad.class);
            List<Ciudad> ciudades = ciudadQuery.getResultList();
            ciudades.forEach(c -> LOGGER.info(c.toString()));

            System.out.println("--- Hoteles in Mendoza ---");
            TypedQuery<Hotel> hotelQuery = em.createQuery("SELECT h FROM Hotel h WHERE h.ciudad = :ciudad", Hotel.class);
            hotelQuery.setParameter("ciudad", mendoza);
            List<Hotel> hotelesMendoza = hotelQuery.getResultList();
            hotelesMendoza.forEach(h -> LOGGER.info(h.toString()));

            System.out.println("--- Reservas for Cliente 1 ---");
            List<Reserva> reservasCliente1 = reservaManager.getReservasPorCliente(cliente1);
            reservasCliente1.forEach(r -> LOGGER.info(r.toString()));

            System.out.println("--- Reservas for Habitacion 101 ---");
            List<Reserva> reservasHabitacion101 = reservaManager.getReservasPorHabitacion(hab101);
            reservasHabitacion101.forEach(r -> LOGGER.info(r.toString()));

            // --- Update data ---
            em.getTransaction().begin();
            hotelSolAndino.setNombre("Grand Sol Andino");
            em.merge(hotelSolAndino); // Update in database
            em.getTransaction().commit();
            LOGGER.info("Updated Hotel Sol Andino name.");

            System.out.println("--- Updated Hotel Sol Andino ---");
            Hotel updatedHotel = em.find(Hotel.class, hotelSolAndino.getId());
            LOGGER.info(updatedHotel.toString());

            // --- Delete data ---
            em.getTransaction().begin();
            Ciudad cityToDelete = em.find(Ciudad.class, mendoza.getId());
            if (cityToDelete != null) {
                em.remove(cityToDelete); // Delete the city and associated hotels due to cascade
            }
            em.getTransaction().commit();
            LOGGER.info("Deleted Mendoza and associated hotels (cascade delete).");

            System.out.println("--- After deleting Mendoza ---");
            TypedQuery<Ciudad> ciudadQueryAfterDelete = em.createQuery("SELECT c FROM Ciudad c", Ciudad.class);
            List<Ciudad> ciudadesAfterDelete = ciudadQueryAfterDelete.getResultList();
            ciudadesAfterDelete.forEach(c -> LOGGER.info(c.toString()));

            LOGGER.info("Data operations completed successfully.");

        } catch (Exception e) {
            if (em != null&& em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                LOGGER.log(Level.SEVERE, "Global transaction rolled back due to error: " + e.getMessage(), e);
            } else {
                LOGGER.log(Level.SEVERE, "Error occurred: " + e.getMessage(), e);
            }
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
                LOGGER.info("EntityManager closed.");
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
                LOGGER.info("EntityManagerFactory closed.");
            }
        }
    }
}
