package dominio;

/**
 * Equipo que necesita un componente para volver a funcionar.
 * Indica qué tipo de componente requiere y la capacidad mínima aceptada.
 */
public class EquipoReceptor {

    private static int contadorEquiposReceptores = 0;

    private final String codigo;
    private String marca;
    private String modelo;
    private String tipoComponenteRequerido;
    private int capacidadMinimaRequerida;

    public EquipoReceptor(String codigo, String marca, String modelo,
                          String tipoComponenteRequerido, int capacidadMinimaRequerida) {
        this.codigo = validarTexto(codigo, "código");
        setMarca(marca);
        setModelo(modelo);
        setTipoComponenteRequerido(tipoComponenteRequerido);
        setCapacidadMinimaRequerida(capacidadMinimaRequerida);
        contadorEquiposReceptores++;
    }

    // ---------- Validación ----------

    private String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    // ---------- Getters ----------

    public static int getContadorEquiposReceptores() {
        return contadorEquiposReceptores;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getTipoComponenteRequerido() {
        return tipoComponenteRequerido;
    }

    public int getCapacidadMinimaRequerida() {
        return capacidadMinimaRequerida;
    }

    // ---------- Setters con validación ----------

    public void setMarca(String marca) {
        this.marca = validarTexto(marca, "marca");
    }

    public void setModelo(String modelo) {
        this.modelo = validarTexto(modelo, "modelo");
    }

    public void setTipoComponenteRequerido(String tipoComponenteRequerido) {
        this.tipoComponenteRequerido = validarTexto(tipoComponenteRequerido, "tipo de componente requerido");
    }

    public void setCapacidadMinimaRequerida(int capacidadMinimaRequerida) {
        if (capacidadMinimaRequerida < ComponenteRecuperado.CAPACIDAD_MINIMA) {
            throw new IllegalArgumentException("La capacidad mínima requerida debe ser mayor o igual a "
                    + ComponenteRecuperado.CAPACIDAD_MINIMA + ".");
        }
        this.capacidadMinimaRequerida = capacidadMinimaRequerida;
    }

    public String toString() {
        return codigo + " - " + marca + " " + modelo + " - Requiere: " + tipoComponenteRequerido
                + " de " + capacidadMinimaRequerida + " o más";
    }
}
