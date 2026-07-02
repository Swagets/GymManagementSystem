package gymapp.controllers;

public class Rutina {
    private int id;
    private String nombre;
    private String objetivo;
    private int duracion;

    public Rutina(int id, String nombre, String objetivo, int duracion) {
        this.id = id;
        this.nombre = nombre;
        this.objetivo = objetivo;
        this.duracion = duracion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getObjetivo() { return objetivo; }
    public int getDuracion() { return duracion; }
}
