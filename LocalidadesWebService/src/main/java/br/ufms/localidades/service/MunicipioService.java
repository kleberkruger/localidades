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

import br.ufms.localidades.model.Municipio;
import java.util.List;

/**
 *
 * @author kleberkruger
 */
public class MunicipioService {

    private static final MunicipioService INSTANCE = new MunicipioService();

    public static MunicipioService getInstance() {
        return INSTANCE;
    }

    private MunicipioService() {

    }

    /**
     * Retorna uma lista contendo todos os municípios com o nome informado pelo
     * parâmetro. Caso não exista nenhum, retorna uma lista vazia.
     *
     * @param nome nome do município
     * @return uma lista com todos os municípios com este nome
     */
    public List<Municipio> getMunicipios(String nome) {
        return null;
    }

    /**
     * Retorna o municipio do estado com o mesmo nome do parâmetro informado.
     * Caso não exista dentro deste estado nenhum município com este nome,
     * retorna null.
     *
     * @param nome nome do município
     * @param uf UF do município
     * @return o município
     */
    public Municipio getMunicipio(String nome, String uf) {
        return null;
    }

    /**
     * Retorna uma lista contendo todos os municípios do Brasil.
     *
     * @return uma lista com todos os municípios
     */
    public List<Municipio> getMunicipios() {
        return null;
    }
}
