<%-- 
    Document   : index
    Created on : 13/01/2016, 22:45:47
    Author     : kleberkruger
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Localidades WebService</title>
    </head>
    <body>
        <h1>Documentação</h1>
        <h4>Como fazer consultas (?):</h4>
        <ul>
            <li><a href="<%=request.getContextPath()%>/rest/estados">/rest/estados</a> --> retorna uma lista com todos os estados do Brasil</li>
            <li><a href="<%=request.getContextPath()%>/rest/estados/MS">/rest/estados/MS</a> --> retorna os dados do estado requisitado</li>
            <li><a href="<%=request.getContextPath()%>/rest/estados/MS,SP">/rest/estados/MS,SP</a> --> retorna os dados dos estados requisitados</li>
        </ul>
        <ul>
            <li><a href="<%=request.getContextPath()%>/rest/municipios">/rest/municipios</a> --> retorna uma lista com todos os municípios do Brasil</li>
            <li><a href="<%=request.getContextPath()%>/rest/municipios/Coxim">/rest/municipios/Coxim</a> --> retorna uma lista com todos os municípios do Brasil com este nome</li>
            <li><a href="<%=request.getContextPath()%>/rest/municipios/Coxim/MS">/rest/municipios/Coxim/MS</a> --> retorna os dados do município específicado</li>
        </ul>
    </body>
</html>
