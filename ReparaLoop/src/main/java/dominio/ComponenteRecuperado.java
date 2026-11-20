package dominio;

/**
 * Pieza que se recupera de un equipo donante y se puede instalar en un equipo receptor.
 */
public class ComponenteRecuperado {

    // Constante: capacidad más pequeña que se acepta
    public static final int CAPACIDAD_MINIMA = 1;

    private static int contadorComponentes = 0;

    private final String codigo;
    private String tipo;
    private int capacidad;
    private String interfaz;
    private final boolean estadoPrueba;
    private boolean disponible;
    private final EquipoDonante equipoDonante;

    public ComponenteRecuperado(String codigo, String tipo, int capacidad, String interfaz,
                                boolean estadoPrueba, EquipoDonante equipoDonante) {
        this.codigo = validarTexto(codigo, "código");
        setTipo(tipo);
        setCapacidad(capacidad);
        setInterfaz(interfaz);
        if (equipoDonante == null) {
            throw new IllegalArgumentException("El equipo donante es obligatorio.");
        }
        this.equipoDonante = equipoDonante;
        this.estadoPrueba = estadoPrueba;
        // Solo queda disponible si aprobó la prueba de funcionamiento.
        // No es final porque cambiará cuando se implemente la asignación a reparaciones (RF05).
        this.disponible = estadoPrueba;
        contadorComponentes++;
    }

    /**
     * Es compatible si el tipo coincide y la capacidad es igual o mayor a la que pide el equipo.
     */
    public boolean esCompatible(EquipoReceptor receptor) {
        if (receptor == null) {
            throw new IllegalArgumentException("El equipo receptor es obligatorio.");
        }
        boolean mismoTipo = tipo.equalsIgnoreCase(receptor.getTipoComponenteRequerido());
        boolean capacidadSuficiente = capacidad >= receptor.getCapacidadMinimaRequerida();
        return mismoTipo && capacidadSuficiente;
    }

    // ---------- Validación ----------

    private String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    // ---------- Getters ----------

    public static int getContadorComponentes() {
        return contadorComponentes;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getInterfaz() {
        return interfaz;
    }

    public boolean isEstadoPrueba() {
        return estadoPrueba;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public EquipoDonante getEquipoDonante() {
        return equipoDonante;
    }

    // ---------- Setters con validación ----------

    public void setTipo(String tipo) {
        this.tipo = validarTexto(tipo, "tipo");
    }

    public void setCapacidad(int capacidad) {
        if (capacidad < CAPACIDAD_MINIMA) {
            throw new IllegalArgumentException("La capacidad debe ser mayor o igual a " + CAPACIDAD_MINIMA + ".");
        }
        this.capacidad = capacidad;
    }

    public void setInterfaz(String interfaz) {
        this.interfaz = validarTexto(interfaz, "interfaz");
    }

    public String toString() {
        String estado;
        if (disponible) {
            estado = "disponible";
        } else {
            estado = "no disponible";
        }
        return codigo + " - " + tipo + " " + capacidad + " - " + interfaz + " - " + estado
                + " - Origen: " + equipoDonante.getCodigo();
    }
}
