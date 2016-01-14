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


import br.ufms.localidades.model.Macrorregiao;
import br.ufms.localidades.model.Microrregiao;
import br.ufms.localidades.model.Municipio;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author kleberkruger
 */
public class LeitorXML {

    public List<Municipio> carregar(InputStream inputStream) {
        XStream stream = new XStream(new DomDriver());
        stream.autodetectAnnotations(true);
        stream.alias("listaMunicipio", List.class);
        stream.alias("municipio", Municipio.class);
        stream.alias("microrregiao", Microrregiao.class);
        stream.alias("macrorregiao", Macrorregiao.class);
        return (List<Municipio>) stream.fromXML(inputStream);
    }

}
