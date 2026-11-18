package negocio;

import dominio.ComponenteRecuperado;
import dominio.Donante;
import dominio.EquipoDonante;
import dominio.EquipoReceptor;
import dominio.Reparacion;
import dominio.Tecnico;

import java.util.ArrayList;
import java.util.List;

/**
 * Guarda en memoria todos los registros de ReparaLoop y coordina las reparaciones.
 * Cada lista representa una relación 0..* con una clase del dominio.
 */
public class GestorReparaciones {

    private final List<Donante> donantes;
    private final List<EquipoDonante> equiposDonantes;
    private final List<ComponenteRecuperado> componentes;
    private final List<EquipoReceptor> equiposReceptores;
    private final List<Tecnico> tecnicos;
    private final List<Reparacion> reparaciones;
    private final GestorCompatibilidad gestorCompatibilidad;

    public GestorReparaciones() {
        donantes = new ArrayList<>();
        equiposDonantes = new ArrayList<>();
        componentes = new ArrayList<>();
        equiposReceptores = new ArrayList<>();
        tecnicos = new ArrayList<>();
        reparaciones = new ArrayList<>();
        gestorCompatibilidad = new GestorCompatibilidad();
    }

    // ===================== REGISTROS =====================

    public Donante registrarDonante(String codigo, String nombreCompleto, String telefono,
                                    String correo, String direccion) {
        if (buscarDonante(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un donante con el código " + codigo + ".");
        }
        Donante donante = new Donante(codigo, nombreCompleto, telefono, correo, direccion);
        donantes.add(donante);
        return donante;
    }

    public EquipoDonante registrarEquipoDonante(String codigoDonante, String codigo, String marca,
                                                String modelo, String diagnostico) {
        Donante donante = buscarDonante(codigoDonante);
        if (donante == null) {
            throw new IllegalArgumentException("No existe un donante con el código " + codigoDonante + ".");
        }
        if (buscarEquipoDonante(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un equipo donante con el código " + codigo + ".");
        }
        EquipoDonante equipo = new EquipoDonante(codigo, marca, modelo, diagnostico, donante);
        donante.agregarEquipo(equipo);
        equiposDonantes.add(equipo);
        return equipo;
    }

    public ComponenteRecuperado registrarComponente(String codigoEquipoDonante, String codigo, String tipo,
                                                    int capacidad, String interfaz, boolean estadoPrueba) {
        EquipoDonante equipo = buscarEquipoDonante(codigoEquipoDonante);
        if (equipo == null) {
            throw new IllegalArgumentException("No existe un equipo donante con el código " + codigoEquipoDonante + ".");
        }
        if (buscarComponente(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un componente con el código " + codigo + ".");
        }
        ComponenteRecuperado componente = new ComponenteRecuperado(codigo, tipo, capacidad, interfaz, estadoPrueba, equipo);
        equipo.agregarComponente(componente);
        componentes.add(componente);
        return componente;
    }

    public EquipoReceptor registrarEquipoReceptor(String codigo, String marca, String modelo,
                                                  String tipoRequerido, int capacidadMinima) {
        if (buscarEquipoReceptor(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un equipo receptor con el código " + codigo + ".");
        }
        EquipoReceptor receptor = new EquipoReceptor(codigo, marca, modelo, tipoRequerido, capacidadMinima);
        equiposReceptores.add(receptor);
        return receptor;
    }

    public Tecnico registrarTecnico(String idTecnico, String nombre) {
        if (buscarTecnico(idTecnico) != null) {
            throw new IllegalArgumentException("Ya existe un técnico con el ID " + idTecnico + ".");
        }
        Tecnico tecnico = new Tecnico(idTecnico, nombre);
        tecnicos.add(tecnico);
        return tecnico;
    }

    public Reparacion registrarNuevaReparacion(String codigo, String codigoReceptor, String idTecnico) {
        EquipoReceptor receptor = buscarEquipoReceptor(codigoReceptor);
        if (receptor == null) {
            throw new IllegalArgumentException("No existe un equipo receptor con el código " + codigoReceptor + ".");
        }
        Tecnico tecnico = buscarTecnico(idTecnico);
        if (tecnico == null) {
            throw new IllegalArgumentException("No existe un técnico con el ID " + idTecnico + ".");
        }
        if (buscarReparacion(codigo) != null) {
            throw new IllegalArgumentException("Ya existe una reparación con el código " + codigo + ".");
        }
        Reparacion reparacion = new Reparacion(codigo, receptor, tecnico);
        tecnico.agregarReparacion(reparacion);
        reparaciones.add(reparacion);
        return reparacion;
    }

    // ===================== OPERACIONES =====================

    public List<ComponenteRecuperado> buscarCompatibles(String codigoReparacion) {
        Reparacion reparacion = obtenerReparacion(codigoReparacion);
        return gestorCompatibilidad.filtrarCompatibles(componentes, reparacion.getEquipoReceptor());
    }

    public void asignarComponenteAReparacion(String codigoReparacion, String codigoComponente) {
        Reparacion reparacion = obtenerReparacion(codigoReparacion);
        ComponenteRecuperado componente = buscarComponente(codigoComponente);
        if (componente == null) {
            throw new IllegalArgumentException("No existe un componente con el código " + codigoComponente + ".");
        }
        reparacion.instalarComponente(componente);
    }

    public void cerrarReparacion(String codigoReparacion, String resultado) {
        Reparacion reparacion = obtenerReparacion(codigoReparacion);
        reparacion.registrarResultado(resultado);
    }

    public String consultarTrazabilidad(String codigoReparacion) {
        Reparacion reparacion = obtenerReparacion(codigoReparacion);
        return reparacion.consultarTrazabilidad();
    }

    public List<Reparacion> consultarHistorialPorEquipo(String codigoReceptor) {
        if (buscarEquipoReceptor(codigoReceptor) == null) {
            throw new IllegalArgumentException("No existe un equipo receptor con el código " + codigoReceptor + ".");
        }
        List<Reparacion> historial = new ArrayList<>();
        for (Reparacion reparacion : reparaciones) {
            if (reparacion.getEquipoReceptor().getCodigo().equalsIgnoreCase(codigoReceptor.trim())) {
                historial.add(reparacion);
            }
        }
        return historial;
    }

    // ===================== BÚSQUEDAS =====================
    // Cada búsqueda devuelve el objeto encontrado o null si no existe.
    // Los códigos se comparan sin importar mayúsculas o minúsculas.

    public Donante buscarDonante(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (Donante donante : donantes) {
            if (donante.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return donante;
            }
        }
        return null;
    }

    public EquipoDonante buscarEquipoDonante(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (EquipoDonante equipo : equiposDonantes) {
            if (equipo.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return equipo;
            }
        }
        return null;
    }

    public ComponenteRecuperado buscarComponente(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (ComponenteRecuperado componente : componentes) {
            if (componente.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return componente;
            }
        }
        return null;
    }

    public EquipoReceptor buscarEquipoReceptor(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (EquipoReceptor receptor : equiposReceptores) {
            if (receptor.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return receptor;
            }
        }
        return null;
    }

    public Tecnico buscarTecnico(String idTecnico) {
        if (idTecnico == null) {
            return null;
        }
        for (Tecnico tecnico : tecnicos) {
            if (tecnico.getIdTecnico().equalsIgnoreCase(idTecnico.trim())) {
                return tecnico;
            }
        }
        return null;
    }

    public Reparacion buscarReparacion(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (Reparacion reparacion : reparaciones) {
            if (reparacion.getCodigoReparacion().equalsIgnoreCase(codigo.trim())) {
                return reparacion;
            }
        }
        return null;
    }

    /**
     * Igual que buscarReparacion, pero lanza una excepción si no la encuentra.
     */
    private Reparacion obtenerReparacion(String codigo) {
        Reparacion reparacion = buscarReparacion(codigo);
        if (reparacion == null) {
            throw new IllegalArgumentException("No existe una reparación con el código " + codigo + ".");
        }
        return reparacion;
    }

    // ===================== LISTAS (copias) =====================

    public List<Donante> getDonantes() {
        return new ArrayList<>(donantes);
    }

    public List<EquipoDonante> getEquiposDonantes() {
        return new ArrayList<>(equiposDonantes);
    }

    public List<ComponenteRecuperado> getComponentes() {
        return new ArrayList<>(componentes);
    }

    public List<EquipoReceptor> getEquiposReceptores() {
        return new ArrayList<>(equiposReceptores);
    }

    public List<Tecnico> getTecnicos() {
        return new ArrayList<>(tecnicos);
    }

    public List<Reparacion> getReparaciones() {
        return new ArrayList<>(reparaciones);
    }
}
