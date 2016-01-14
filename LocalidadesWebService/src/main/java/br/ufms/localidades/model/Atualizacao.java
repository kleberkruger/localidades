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

import java.io.Serializable;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author kleberkruger
 */
public class Atualizacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient LocalTime localTime;
    private transient ZoneId zoneId;

    private String horario;
    private String zona;

    /**
     * Cria um objeto Atualizacao com o horário atual e a zona padrão do 
     * sistema.
     */
    public Atualizacao() {
        this(LocalTime.now());
    }

    /**
     * Cria um objeto Atualizacao com o horário informado e a zona padrão do 
     * sistema.
     * 
     * @param localTime o horário da atualização
     */
    public Atualizacao(LocalTime localTime) {
        this(localTime, ZoneId.systemDefault());
    }

    /**
     * Cria um objeto Atualizacao com o horário e a zona informada.
     * 
     * @param localTime o horário da atualização
     * @param zoneId a zona da região
     */
    public Atualizacao(LocalTime localTime, ZoneId zoneId) {
        setLocalTime(localTime);
        setZoneId(zoneId);
    }

    /**
     * @return the localTime
     */
    public final LocalTime getLocalTime() {
        if (localTime == null) {
            this.localTime = LocalTime.parse(horario);
        }
        return localTime;
    }

    /**
     * @param localTime the localTime to set
     */
    public final void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
        this.horario = localTime.format(DateTimeFormatter.ISO_TIME);
    }

    /**
     * @return the zoneId
     */
    public final ZoneId getZoneId() {
        if (zoneId == null) {
            this.zoneId = ZoneId.of(zona);
        }
        return zoneId;
    }

    /**
     * @param zoneId the zoneId to set
     */
    public final void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
        this.zona = zoneId.toString();
    }

    /**
     * @return the horario
     */
    public final String getHorario() {
        return horario;
    }

    /**
     * @param horario the horario to set
     */
    public final void setHorario(String horario) {
        this.horario = horario;
        this.localTime = LocalTime.parse(horario);
    }

    /**
     * @return the zona
     */
    public final String getZona() {
        return zona;
    }

    /**
     * @param zona the zona to set
     */
    public final void setZona(String zona) {
        this.zona = zona;
        this.zoneId = ZoneId.of(zona);
    }
}
