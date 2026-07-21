package gymapp.model;

import java.time.LocalDate;

public class Asignacion {

    private int id;
    private int clienteId;
    private int rutinaId;

    private String cliente;
    private String rutina;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private String estado;

    public Asignacion(int id,
                      int clienteId,
                      int rutinaId,
                      String cliente,
                      String rutina,
                      LocalDate fechaInicio,
                      LocalDate fechaFin,
                      String estado) {

        this.id = id;
        this.clienteId = clienteId;
        this.rutinaId = rutinaId;
        this.cliente = cliente;
        this.rutina = rutina;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public int getRutinaId() {
        return rutinaId;
    }

    public String getCliente() {
        return cliente;
    }

    public String getRutina() {
        return rutina;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public String getEstado() {
        return estado;
    }
}