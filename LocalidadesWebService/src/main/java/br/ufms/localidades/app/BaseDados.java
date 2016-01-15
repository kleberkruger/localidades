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

import br.ufms.localidades.model.Estado;
import br.ufms.localidades.model.Municipio;
import br.ufms.localidades.model.UF;
import br.ufms.localidades.service.AtualizacaoService;
import br.ufms.localidades.util.LeitorXML;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kleberkruger
 */
public class BaseDados {

    private static final int TENTATIVAS_CONEXAO = 3;

    private Estado[] estados;

    private final Estado[] estadosWeb = {
        new Estado(UF.AC), new Estado(UF.AL), new Estado(UF.AP),
        new Estado(UF.AM), new Estado(UF.BA), new Estado(UF.CE),
        new Estado(UF.DF), new Estado(UF.ES), new Estado(UF.GO),
        new Estado(UF.MA), new Estado(UF.MT), new Estado(UF.MS),
        new Estado(UF.MG), new Estado(UF.PA), new Estado(UF.PB),
        new Estado(UF.PR), new Estado(UF.PE), new Estado(UF.PI),
        new Estado(UF.RJ), new Estado(UF.RN), new Estado(UF.RS),
        new Estado(UF.RO), new Estado(UF.RR), new Estado(UF.SC),
        new Estado(UF.SP), new Estado(UF.SE), new Estado(UF.TO)
    };

    private final List<Municipio> municipios = new ArrayList<>();

    private BaseDados() {
        inicializar();
    }

    /**
     * Inicializa primeiramente os dados deste servidor web com os dados já
     * existente no arquivo padrão "estados.json", em seguida, agenda uma
     * atualização no banco de dados do governo.
     */
    private void inicializar() {
        try {
            carregarDoArquivoLocal();
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
        atualizar();
        agendarAtualizacoes();
    }

    /**
     * Agenda os horários de atualizações de acordo com o arquivo de
     * configuração atualizacoes.xml. Caso o arquivo não exista, agenda conforme
     * os horários padrão.
     */
    private void agendarAtualizacoes() {
        try {
            URL url = getClass().getResource("/xml/atualizacoes.xml");
            Path path = url != null ? Paths.get(url.toURI()) : null;
            new AtualizacaoService().carregar(path);
        } catch (URISyntaxException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /*
     * -------------------------------------------------------------------------
     * Caso não dê certo a leitura do arquivo estados.json no servidor, mude-o 
     * para o diretório WEB-INF e leia-o com um objeto ServletContext: 
     * URL url = context.getResource("/WEB-INF/estados.json");
     *
     * Para conseguir um context aqui, crie um objeto ServletContext nesta
     * classe com métodos getter e setter, e na classe EstadosResource, crie um
     * método para capturar o context:
     *
     * @Context public void capturarServletContext(ServletContext context) {
     * EstadoService.getInstance().setServletContext(context); }
     * -------------------------------------------------------------------------
     */
    private void carregarDoArquivoLocal() throws FileNotFoundException {
        InputStream in = getClass().getResourceAsStream("/json/estados.json");
        if (in == null) {
            throw new FileNotFoundException("Arquivo estados.json não encontrado");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            setBaseDados(new Gson().fromJson(reader, Estado[].class));
        } catch (IOException e) {
        }
    }

    private void carregarDoServidorWeb() throws IOException {
        int falhas = 0;
        for (int i = 0; i < estadosWeb.length; i++) {
            try {
                carregarDoServidorWeb(estadosWeb[i]);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                if (++falhas >= TENTATIVAS_CONEXAO) {
                    throw ex;
                }
                i--;
            }
        }
    }

    /**
     * Gera a URL correta para acessar o web service do governo.
     *
     * @param estado estado ao qual deseja-se fazer a consulta
     * @return a URL
     *
     * @throws MalformedURLException
     */
    private URL gerarURL(Estado estado) throws MalformedURLException {
        return new URL("http://dadosabertos.almg.gov.br/ws/brasil/localidades/ufs/"
                + estado.getUf() + "/municipios");
    }

    private void carregarDoServidorWeb(Estado estado) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) gerarURL(estado).openConnection();
        conn.connect();
        estado.setMunicipios(new LeitorXML().carregar(conn.getInputStream()));
        conn.disconnect();
    }

    /**
     * Altera a base dos dados. Agora, todas as informações sairão desta nova
     * fonte.
     *
     * @param estados
     */
    private void setBaseDados(Estado[] estados) {
        this.estados = estados;
        this.municipios.clear();
        for (Estado e : estados) {
            corrigirAcentuacao(e.getMunicipios());
            municipios.addAll(e.getMunicipios());
        }
    }

    /**
     * Corrige a acentuação de algumas cidades, como por exemplo: "Olho
     * d\u0027Água das Flores".
     */
    private void corrigirAcentuacao(List<Municipio> lista) {
        lista.stream().forEach((m) -> {
            m.setNome(m.getNome().replace("\\u0027", "'"));
        });
    }

    /**
     * Consulta o banco de dados do governo e atualiza os municípios de todos os
     * estados. Caso o servidor esteja offline, agenda a consulta para daqui 30
     * minutos.
     */
    public void atualizar() {
        new Thread(() -> {
            try {
                carregarDoServidorWeb();
                setBaseDados(estadosWeb);
                // Atualizar o arquivo json local
            } catch (IOException ex) {
                // Agendar para daqui 30 min?
            }
        }).start();
    }

    /**
     * Atributo estático da classe BaseDados que armazona a referência para a
     * única instância desta classe.
     */
    private static final BaseDados INSTANCE = new BaseDados();

    /**
     * Retorna a única instância desta classe (Singleton).
     *
     * @return a instância da classe BaseDados
     */
    public static BaseDados getInstance() {
        return INSTANCE;
    }

    /**
     * @return the estados
     */
    public Estado[] getEstados() {
        return estados;
    }

    /**
     * @return the estados
     */
    public List<Municipio> getMunicipios() {
        return municipios;
    }
}
