package dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * Persona que realiza las reparaciones.
 * Relación: un Tecnico es responsable de 0..* Reparaciones.
 */
public class Tecnico {

    private static int contadorTecnicos = 0;

    private final String idTecnico;
    private String nombre;
    private final List<Reparacion> listaReparaciones;

    public Tecnico(String idTecnico, String nombre) {
        this.idTecnico = validarTexto(idTecnico, "ID del técnico");
        setNombre(nombre);
        this.listaReparaciones = new ArrayList<>();
        contadorTecnicos++;
    }

    /**
     * Agrega una reparación que tiene a este técnico como responsable.
     */
    public void agregarReparacion(Reparacion reparacion) {
        if (reparacion == null) {
            throw new IllegalArgumentException("La reparación es obligatoria.");
        }
        if (reparacion.getTecnicoResponsable() != this) {
            throw new IllegalArgumentException("La reparación " + reparacion.getCodigoReparacion()
                    + " tiene otro técnico responsable.");
        }
        if (listaReparaciones.contains(reparacion)) {
            throw new IllegalArgumentException("La reparación ya está en la lista del técnico.");
        }
        listaReparaciones.add(reparacion);
    }

    // ---------- Validación ----------

    private String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    // ---------- Getters y setters ----------

    public static int getContadorTecnicos() {
        return contadorTecnicos;
    }

    public String getIdTecnico() {
        return idTecnico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarTexto(nombre, "nombre");
    }

    public List<Reparacion> getListaReparaciones() {
        return new ArrayList<>(listaReparaciones);
    }

    public String toString() {
        return idTecnico + " - " + nombre + " - " + listaReparaciones.size() + " reparación(es)";
    }
}
