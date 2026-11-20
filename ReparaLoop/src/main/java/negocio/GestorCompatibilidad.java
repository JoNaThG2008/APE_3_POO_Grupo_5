package negocio;

import dominio.ComponenteRecuperado;
import dominio.EquipoReceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Busca qué componentes recuperados sirven para un equipo receptor.
 */
public class GestorCompatibilidad {

    /**
     * Un componente sirve si está disponible y es compatible con el equipo.
     */
    public boolean evaluarCompatibilidad(ComponenteRecuperado componente, EquipoReceptor receptor) {
        if (componente == null || receptor == null) {
            throw new IllegalArgumentException("El componente y el equipo receptor son obligatorios.");
        }
        return componente.isDisponible() && componente.esCompatible(receptor);
    }

    /**
     * Recorre la lista y devuelve solo los componentes que sirven para el equipo.
     */
    public List<ComponenteRecuperado> filtrarCompatibles(List<ComponenteRecuperado> componentes, EquipoReceptor receptor) {
        if (componentes == null || receptor == null) {
            throw new IllegalArgumentException("La lista de componentes y el equipo receptor son obligatorios.");
        }
        List<ComponenteRecuperado> compatibles = new ArrayList<>();
        for (ComponenteRecuperado componente : componentes) {
            if (evaluarCompatibilidad(componente, receptor)) {
                compatibles.add(componente);
            }
        }
        return compatibles;
    }
}
