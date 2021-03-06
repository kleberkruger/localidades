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
package br.ufms.localidades.resource;

import br.ufms.localidades.model.Municipio;
import br.ufms.localidades.service.MunicipioService;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author kleberkruger
 */
@Path("/municipios")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class MunicipiosResource {

    @GET
    public List<Municipio> get() {
        return MunicipioService.getInstance().getMunicipios();
    }

    @GET
    @Path("{municipio}")
    public List<Municipio> get(@PathParam("municipio") String municipio) {
        return MunicipioService.getInstance().getMunicipios(municipio);
    }
    
    @GET
    @Path("/{municipio}/{uf}")
    public Municipio get(@PathParam("municipio") String municipio, 
            @PathParam("uf") String uf) {
        
        return MunicipioService.getInstance().getMunicipio(municipio, uf);
    }
}
