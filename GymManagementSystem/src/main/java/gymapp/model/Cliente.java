package gymapp.model;

public class Cliente {

    private int id;
    private String nombre;
    private int edad;
    private double peso;

    public Cliente(int id, String nombre, int edad, double peso) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.peso = peso;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public double getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return nombre;
    }
}