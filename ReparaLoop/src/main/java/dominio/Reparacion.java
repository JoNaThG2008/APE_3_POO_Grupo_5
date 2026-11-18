package dominio;

import java.time.LocalDate;

/**
 * Trabajo de reparación de un equipo receptor usando un componente recuperado.
 * Relaciones: 1 EquipoReceptor, 1 Tecnico y 0..1 ComponenteRecuperado.
 */
public class Reparacion {

    // Constantes con los resultados permitidos
    public static final String RESULTADO_PENDIENTE = "Pendiente";
    public static final String RESULTADO_EXITOSO = "Exitoso";
    public static final String RESULTADO_FALLIDO = "Fallido";

    private final String codigoReparacion;
    private final EquipoReceptor equipoReceptor;
    private final Tecnico tecnicoResponsable;
    private ComponenteRecuperado componenteAsignado;
    private String resultado;
    private LocalDate fechaInstalacion;

    public Reparacion(String codigoReparacion, EquipoReceptor equipoReceptor, Tecnico tecnicoResponsable) {
        if (codigoReparacion == null || codigoReparacion.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo código de reparación es obligatorio.");
        }
        if (equipoReceptor == null) {
            throw new IllegalArgumentException("El equipo receptor es obligatorio.");
        }
        if (tecnicoResponsable == null) {
            throw new IllegalArgumentException("El técnico responsable es obligatorio.");
        }
        this.codigoReparacion = codigoReparacion.trim();
        this.equipoReceptor = equipoReceptor;
        this.tecnicoResponsable = tecnicoResponsable;
        this.resultado = RESULTADO_PENDIENTE;
    }

    /**
     * Instala un componente en el equipo receptor.
     * Aquí se hacen todas las validaciones de la asignación.
     */
    public void instalarComponente(ComponenteRecuperado componente) {
        if (componente == null) {
            throw new IllegalArgumentException("El componente es obligatorio.");
        }
        if (estaCerrada()) {
            throw new IllegalStateException("La reparación " + codigoReparacion + " ya está cerrada.");
        }
        if (componenteAsignado != null) {
            throw new IllegalStateException("La reparación " + codigoReparacion + " ya tiene un componente asignado.");
        }
        if (!componente.isDisponible()) {
            throw new IllegalStateException("El componente " + componente.getCodigo() + " no está disponible.");
        }
        if (!componente.esCompatible(equipoReceptor)) {
            throw new IllegalArgumentException("El componente " + componente.getCodigo()
                    + " no es compatible con el equipo " + equipoReceptor.getCodigo() + ".");
        }
        componente.marcarAsignado();
        componenteAsignado = componente;
        fechaInstalacion = LocalDate.now();
    }

    /**
     * Cierra la reparación con resultado Exitoso o Fallido.
     * Si falla, el componente vuelve a quedar disponible.
     */
    public void registrarResultado(String nuevoResultado) {
        if (estaCerrada()) {
            throw new IllegalStateException("La reparación " + codigoReparacion + " ya está cerrada.");
        }
        if (componenteAsignado == null) {
            throw new IllegalStateException("Primero debe instalar un componente.");
        }
        if (RESULTADO_EXITOSO.equals(nuevoResultado)) {
            resultado = RESULTADO_EXITOSO;
            equipoReceptor.setEstadoEquipo(EquipoReceptor.ESTADO_REPARADO);
        } else if (RESULTADO_FALLIDO.equals(nuevoResultado)) {
            resultado = RESULTADO_FALLIDO;
            equipoReceptor.setEstadoEquipo(EquipoReceptor.ESTADO_SIN_REPARAR);
            componenteAsignado.marcarDisponible();
        } else {
            throw new IllegalArgumentException("Resultado no válido: " + nuevoResultado);
        }
    }

    public boolean estaCerrada() {
        return !resultado.equals(RESULTADO_PENDIENTE);
    }

    /**
     * Muestra el recorrido del componente: de qué equipo y donante salió y dónde se instaló.
     */
    public String consultarTrazabilidad() {
        String texto = "Reparación: " + codigoReparacion + "\n";
        texto = texto + "Equipo receptor: " + equipoReceptor.getCodigo() + " (" + equipoReceptor.getEstadoEquipo() + ")\n";
        texto = texto + "Técnico: " + tecnicoResponsable.getNombre() + "\n";
        if (componenteAsignado == null) {
            texto = texto + "Componente: sin asignar\n";
        } else {
            EquipoDonante origen = componenteAsignado.getEquipoDonante();
            texto = texto + "Componente: " + componenteAsignado.getCodigo() + " (" + componenteAsignado.getTipo() + ")\n";
            texto = texto + "Equipo donante: " + origen.getCodigo() + "\n";
            texto = texto + "Donante: " + origen.getDonante().getNombreCompleto() + "\n";
            texto = texto + "Fecha de instalación: " + fechaInstalacion + "\n";
        }
        texto = texto + "Resultado: " + resultado;
        return texto;
    }

    // ---------- Getters ----------

    public String getCodigoReparacion() {
        return codigoReparacion;
    }

    public EquipoReceptor getEquipoReceptor() {
        return equipoReceptor;
    }

    public Tecnico getTecnicoResponsable() {
        return tecnicoResponsable;
    }

    public ComponenteRecuperado getComponenteAsignado() {
        return componenteAsignado;
    }

    public String getResultado() {
        return resultado;
    }

    public LocalDate getFechaInstalacion() {
        return fechaInstalacion;
    }

    public String toString() {
        String componente;
        if (componenteAsignado == null) {
            componente = "sin componente";
        } else {
            componente = "componente " + componenteAsignado.getCodigo();
        }
        return codigoReparacion + " - Equipo " + equipoReceptor.getCodigo() + " - Técnico "
                + tecnicoResponsable.getIdTecnico() + " - " + componente + " - " + resultado;
    }
}
