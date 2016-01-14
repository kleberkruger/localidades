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
package br.ufms.localidades.util;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author kleberkruger
 */
public class AgendadorTarefas {

    private static final long QTD_SEGUNDOS_NO_DIA = 86400;

    private final ScheduledExecutorService scheduler;
    private ZoneId zona;

    public AgendadorTarefas() {
        this(ZoneId.systemDefault());
    }

    public AgendadorTarefas(ZoneId zona) {
        this.zona = zona;
        scheduler = new ScheduledThreadPoolExecutor(1);
    }

    /**
     *
     * @param tarefa
     * @param horario
     */
    public void agendar(Runnable tarefa, LocalTime horario) {
        agendar(tarefa, horario, zona);
    }

    /**
     *
     * @param tarefa
     * @param horario
     * @param diariamente
     */
    public void agendar(Runnable tarefa, LocalTime horario, boolean diariamente) {
        agendar(tarefa, horario, zona, diariamente);
    }

    /**
     *
     * @param tarefa
     * @param horario
     * @param zona
     */
    public void agendar(Runnable tarefa, LocalTime horario, ZoneId zona) {
        scheduler.scheduleAtFixedRate(tarefa, getSegundosRestantes(horario, zona),
                QTD_SEGUNDOS_NO_DIA, TimeUnit.SECONDS);
    }

    /**
     *
     * @param tarefa
     * @param horario
     * @param zona
     * @param diariamente
     */
    public void agendar(Runnable tarefa, LocalTime horario, ZoneId zona,
            boolean diariamente) {

        Runnable tarefaAgendada = diariamente ? new Runnable() {
            @Override
            public void run() {
                tarefa.run();
                scheduler.scheduleAtFixedRate(tarefa, QTD_SEGUNDOS_NO_DIA,
                        QTD_SEGUNDOS_NO_DIA, TimeUnit.SECONDS);
            }
        } : tarefa;
        scheduler.scheduleAtFixedRate(tarefaAgendada, getSegundosRestantes(horario, zona),
                QTD_SEGUNDOS_NO_DIA, TimeUnit.SECONDS);
    }

    /**
     * Calcula quantos segundos restam para o horário informado. Caso já tenha
     * passado, calcula levando em consideração o horário do próximo dia.
     *
     * @param horario
     * @param zona
     * @return
     */
    private long getSegundosRestantes(LocalTime horario, ZoneId zona) {
        ZonedDateTime horarioAtual = ZonedDateTime.now(zona);
        ZonedDateTime proximoHorario = horarioAtual.withHour(horario.getHour()).
                withMinute(horario.getMinute()).withSecond(horario.getSecond());

        if (horarioAtual.compareTo(proximoHorario) > 0) {
            proximoHorario = proximoHorario.plusDays(1);
        }

        return Duration.between(horarioAtual, proximoHorario).getSeconds();
    }

    /**
     *
     * @param tarefa
     * @param horario
     * @param zona
     */
    public void desagendar(Runnable tarefa, LocalTime horario, ZoneId zona) {
        // TODO: Implementar esta funcionalidade. Use o método cancel.
        throw new UnsupportedOperationException("Funcionalidade ainda não implementada");

//        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(tarefa, 1,
//                QTD_SEGUNDOS_NO_DIA, TimeUnit.SECONDS);
//        future.cancel(false);
    }
}
