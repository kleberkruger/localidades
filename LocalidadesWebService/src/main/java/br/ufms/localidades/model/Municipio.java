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
package br.ufms.localidades.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;

/**
 *
 * @author kleberkruger
 */
@XStreamAlias("municipio")
public class Municipio implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String uf;
    @XStreamAlias("codigoIbge9")
    private Long codigoIBGE9;
    private Microrregiao microrregiao;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the uf
     */
    public String getUf() {
        return uf;
    }

    /**
     * @param uf the uf to set
     */
    public void setUf(String uf) {
        this.uf = uf;
    }

    /**
     * @return the codigoIBGE9
     */
    public Long getCodigoIBGE9() {
        return codigoIBGE9;
    }

    /**
     * @param codigoIBGE9 the codigoIBGE9 to set
     */
    public void setCodigoIBGE9(Long codigoIBGE9) {
        this.codigoIBGE9 = codigoIBGE9;
    }

    /**
     * @return the microrregiao
     */
    public Microrregiao getMicrorregiao() {
        return microrregiao;
    }

    /**
     * @param microrregiao the microrregiao to set
     */
    public void setMicrorregiao(Microrregiao microrregiao) {
        this.microrregiao = microrregiao;
    }
}
