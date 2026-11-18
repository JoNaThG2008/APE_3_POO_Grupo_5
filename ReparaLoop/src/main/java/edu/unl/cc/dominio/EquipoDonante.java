package dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * Equipo dañado que entrega un donante y del que se recuperan componentes.
 * Relaciones: pertenece a 1 Donante y tiene 0..* ComponentesRecuperados (agregación).
 */
public class EquipoDonante {

    private static int contadorEquiposDonantes = 0;

    private final String codigo;
    private String marca;
    private String modelo;
    private String diagnostico;
    private final Donante donante;
    private final List<ComponenteRecuperado> componentes;

    public EquipoDonante(String codigo, String marca, String modelo, String diagnostico, Donante donante) {
        this.codigo = validarTexto(codigo, "código");
        setMarca(marca);
        setModelo(modelo);
        setDiagnostico(diagnostico);
        if (donante == null) {
            throw new IllegalArgumentException("El donante es obligatorio.");
        }
        this.donante = donante;
        this.componentes = new ArrayList<>();
        contadorEquiposDonantes++;
    }

    /**
     * Agrega a la lista un componente que fue recuperado de este equipo.
     */
    public void agregarComponente(ComponenteRecuperado componente) {
        if (componente == null) {
            throw new IllegalArgumentException("El componente es obligatorio.");
        }
        if (componente.getEquipoDonante() != this) {
            throw new IllegalArgumentException("El componente " + componente.getCodigo() + " proviene de otro equipo.");
        }
        if (componentes.contains(componente)) {
            throw new IllegalArgumentException("El componente " + componente.getCodigo() + " ya está registrado en este equipo.");
        }
        componentes.add(componente);
    }

    // ---------- Validación ----------

    private String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    // ---------- Getters ----------

    public static int getContadorEquiposDonantes() {
        return contadorEquiposDonantes;
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

    public String getDiagnostico() {
        return diagnostico;
    }

    public Donante getDonante() {
        return donante;
    }

    public List<ComponenteRecuperado> getComponentes() {
        return new ArrayList<>(componentes);
    }

    // ---------- Setters con validación ----------

    public void setMarca(String marca) {
        this.marca = validarTexto(marca, "marca");
    }

    public void setModelo(String modelo) {
        this.modelo = validarTexto(modelo, "modelo");
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = validarTexto(diagnostico, "diagnóstico");
    }

    public String toString() {
        return codigo + " - " + marca + " " + modelo + " - Diagnóstico: " + diagnostico
                + " - Donante: " + donante.getCodigo();
    }
}
