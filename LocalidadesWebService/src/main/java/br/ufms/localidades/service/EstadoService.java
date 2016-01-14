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

import br.ufms.localidades.model.Estado;
import br.ufms.localidades.model.UF;
import br.ufms.localidades.util.LeitorXML;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author kleberkruger
 */
public class EstadoService {

    private static final int TENTATIVAS_CONEXAO = 3;

    private final Estado[] estadosWeb;
    private Estado[] estados;

    private EstadoService() {
        estadosWeb = new Estado[] // vetor com todos os estados
        {
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
        inicializar();
    }

    /**
     * Inicializa primeiramente os dados deste servidor web com os dados já
     * existente no arquivo padrão "estados.json", em seguida, agenda uma
     * atualização no banco de dados do governo.
     */
    private void inicializar() {
        carregarEstadosDoArquivo();
        atualizarDados();
        
        agendarAtualizacoesDiarias();
    }
    
    private void agendarAtualizacoesDiarias() {
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
    private void carregarEstadosDoArquivo() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/json/estados.json")));
        Gson gson = new Gson();
        estados = gson.fromJson(reader, Estado[].class);
    }

    private void carregarEstadosDoServidor() throws IOException {
        int falhas = 0;
        for (int i = 0; i < estadosWeb.length; i++) {
            try {
                carregarEstadoDoServidor(estadosWeb[i]);
            } catch (IOException ex) {
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

    private void carregarEstadoDoServidor(Estado estado) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) gerarURL(estado).openConnection();
        conn.connect();
        estado.setMunicipios(new LeitorXML().carregar(conn.getInputStream()));
        conn.disconnect();
    }

    /**
     * Consulta o banco de dados do governo e atualiza os municípios de todos os
     * estados. Caso o servidor esteja offline, agenda a consulta para daqui 
     * 30 minutos.
     */
    public void atualizarDados() {
        new Thread(() -> {
            try {
                System.out.println("Consultando webservice para atualizar os dados...");
                carregarEstadosDoServidor();
                estados = estadosWeb;
                // Atualizar o arquivo json local
            } catch (IOException ex) {
                // Agendar para daqui 30 min
            }
        }).start();
    }

    /**
     * Retorna o estado da UF recebida por parâmetro. Caso a UF seja
     * inexistente, retorna null.
     *
     * @param uf UF - Unidade Federativa do estado
     * @return o estado
     */
    public Estado getEstado(String uf) {
        for (Estado e : estados) {
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
        return estados;
    }
    
    /**
     * Atributo estático da classe EstadoService que armazona a referência para 
     * a única instância desta classe.
     */
    private static final EstadoService INSTANCE = new EstadoService();

    /**
     * Retorna a única instância desta classe (Singleton).
     * @return a instância da classe EstadoService
     */
    public static EstadoService getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        getInstance();
    }
}
