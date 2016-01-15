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
import br.ufms.localidades.model.Atualizacao;
import br.ufms.localidades.util.AgendadorTarefas;
import com.thoughtworks.xstream.XStream;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author kleberkruger
 */
public class AtualizacaoService {
    
    private final AgendadorTarefas agendador = new AgendadorTarefas();
    
    private final AtualizadorRunnable atualiazador = new AtualizadorRunnable();

    private static final Atualizacao[] ATUALIZACOES_PADRAO = {
        new Atualizacao(LocalTime.of(00, 00), ZoneId.of("America/Sao_Paulo")),
        new Atualizacao(LocalTime.of(06, 00), ZoneId.of("America/Sao_Paulo")),
        new Atualizacao(LocalTime.of(12, 00), ZoneId.of("America/Sao_Paulo")),
        new Atualizacao(LocalTime.of(18, 00), ZoneId.of("America/Sao_Paulo"))
    };

    /**
     * Carrega atualizações de um arquivo de configuração. Se o arquivo não 
     * existir, carrega as atualizações padrão.
     * 
     * @param configuracao 
     */
    public void carregar(Path configuracao) {
        if (configuracao == null) {
            for (Atualizacao a : ATUALIZACOES_PADRAO) {
                agendar(a);
            }
        } else {
            XStream stream = new XStream();
            stream.autodetectAnnotations(true);
            stream.alias("atualizacoes", List.class);
            stream.alias("atualizacao", Atualizacao.class);

            List<Atualizacao> atualizacoes = (List<Atualizacao>) stream.
                    fromXML(configuracao.toFile());
            
            atualizacoes.stream().forEach((a) -> {
                agendar(a);
            });
        }
    }

    public void agendar(Atualizacao atualizacao) {        
        agendador.agendar(atualiazador, atualizacao.getLocalTime(), atualizacao.getZoneId());
        System.out.println("Atualização agendada: " + 
                atualizacao.getLocalTime().format(DateTimeFormatter.ISO_TIME) +
                " " + atualizacao.getZoneId());
    }
    
    private final class AtualizadorRunnable implements Runnable {

        @Override
        public void run() {
            BaseDados.getInstance().atualizar();
        }
    }
}
