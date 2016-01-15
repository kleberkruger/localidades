/*
 * Copyright (C) 2016 kleberkruger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.ufms.localidades.service;

import br.ufms.localidades.app.BaseDados;
import br.ufms.localidades.model.Estado;

/**
 *
 * @author kleberkruger
 */
public class EstadoService {

    private EstadoService() {
        // Nada a fazer...
    }

    /**
     * Retorna o estado da UF recebida por parâmetro. Caso a UF seja
     * inexistente, retorna null.
     *
     * @param uf UF - Unidade Federativa do estado
     * @return o estado
     */
    public Estado getEstado(String uf) {
        for (Estado e : BaseDados.getInstance().getEstados()) {
            if (e.getUf().equalsIgnoreCase(uf)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Retorna um vetor contendo os estados pedidos. Caso alguma das UFs seja
     * inválida, na posição desta uf estará nula.
     *
     * @param ufs UF - Unidades Federativas de cada estado.
     * @return o vetor de estados
     */
    public Estado[] getEstados(String... ufs) {
        return new Estado[ufs.length];
    }

    /**
     * Retorna um vetor contendo todos os estados brasileiros.
     *
     * @return o vetor de estados
     */
    public Estado[] getEstados() {
        return BaseDados.getInstance().getEstados();
    }

    /**
     * Atributo estático da classe EstadoService que armazona a referência para
     * a única instância desta classe.
     */
    private static final EstadoService INSTANCE = new EstadoService();

    /**
     * Retorna a única instância desta classe (Singleton).
     *
     * @return a instância da classe EstadoService
     */
    public static EstadoService getInstance() {
        return INSTANCE;
    }
}
