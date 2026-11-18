package dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * Persona que dona uno o varios equipos dañados a ReparaLoop.
 * Relación: un Donante tiene 0..* EquiposDonantes.
 */
public class Donante {

    // Atributo static: lo comparten todos los objetos Donante
    private static int contadorDonantes = 0;

    // El código es final: se asigna una sola vez en el constructor
    private final String codigo;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String direccion;
    private final List<EquipoDonante> equiposDonados;

    public Donante(String codigo, String nombreCompleto, String telefono, String correo, String direccion) {
        this.codigo = validarTexto(codigo, "código");
        setNombreCompleto(nombreCompleto);
        setTelefono(telefono);
        setCorreo(correo);
        setDireccion(direccion);
        this.equiposDonados = new ArrayList<>();
        contadorDonantes++;
    }

    /**
     * Agrega a la lista un equipo que fue creado para este donante.
     */
    public void agregarEquipo(EquipoDonante equipo) {
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo donante es obligatorio.");
        }
        if (equipo.getDonante() != this) {
            throw new IllegalArgumentException("El equipo " + equipo.getCodigo() + " pertenece a otro donante.");
        }
        if (equiposDonados.contains(equipo)) {
            throw new IllegalArgumentException("El equipo " + equipo.getCodigo() + " ya está registrado para este donante.");
        }
        equiposDonados.add(equipo);
    }

    // ---------- Validación ----------

    private String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    // ---------- Getters ----------

    public static int getContadorDonantes() {
        return contadorDonantes;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getDireccion() {
        return direccion;
    }

    /**
     * Devuelve una copia de la lista para que no se modifique desde fuera de la clase.
     */
    public List<EquipoDonante> getEquiposDonados() {
        return new ArrayList<>(equiposDonados);
    }

    // ---------- Setters con validación ----------

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = validarTexto(nombreCompleto, "nombre completo");
    }

    public void setTelefono(String telefono) {
        this.telefono = validarTelefono(telefono);
    }

    public void setCorreo(String correo) {
        this.correo = validarCorreo(correo);
    }

    // ---------- Validaciones públicas (static) ----------
    // Son static porque no necesitan un objeto Donante: el menú las usa
    // para revisar el dato antes de crear el donante.

    public static String validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo teléfono es obligatorio.");
        }
        String valor = telefono.trim();
        if (valor.length() != 10) {
            throw new IllegalArgumentException("El teléfono debe tener 10 dígitos.");
        }
        for (int i = 0; i < valor.length(); i++) {
            if (!Character.isDigit(valor.charAt(i))) {
                throw new IllegalArgumentException("El teléfono solo debe contener números.");
            }
        }
        return valor;
    }

    public static String validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo correo es obligatorio.");
        }
        String valor = correo.trim();
        if (!valor.contains("@")) {
            throw new IllegalArgumentException("El correo debe contener el símbolo @.");
        }
        return valor;
    }

    public void setDireccion(String direccion) {
        this.direccion = validarTexto(direccion, "dirección");
    }

    public String toString() {
        return codigo + " - " + nombreCompleto + " - " + telefono + " - " + equiposDonados.size() + " equipo(s) donado(s)";
    }
}
