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
package br.ufms.localidades.app;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Application;

/**
 *
 * @author kleberkruger
 */
public class LocalidadesApp extends Application {

    /**
     * Construtor de LocalidadesApp. Ao criar este objeto, o servidor inicializa
     * a base de dados da aplicação sincronizando-a com o arquivo estados.json
     * salvo localmente. Em seguida, faz uma consulta ao web service para obter
     * os dados atualizados.
     */
    public LocalidadesApp() {
        BaseDados.getInstance();
    }

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap<>();
        // Configura o pacote para fazer scan das classes com anotações REST.
        properties.put("jersey.config.server.provider.packages", "br.ufms.localidades");
        return properties;
    }
}
