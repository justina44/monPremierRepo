<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Envoi de fichier</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/inc/style.css"/>" />
</head>
<body>
	<form action="<c:url value="/Upload" />" method="post"
		enctype="multipart/form-data">
		<fieldset>
			<legend>Envoi de fichier</legend>

			<label for="description">Description du fichier</label> 
			<input	type="text" id="description" name="description" value="" /> 
			<span class="success"><c:out value="${description}" /></span> <br /> <br />

			<label for="fichier">Emplacement du fichier <span class="requis">*</span></label> 
			<input type="file" id="fichier" name="fichier" /> 
			<span class="succes"><c:out	value="${fichier}" /></span> <br /> <br /> 
			<input type="submit" value="Envoyer" class="sansLabel" /> <br />
		</fieldset>
	</form>
	
	<c:if test="${ !empty fichier }"><p><c:out value="Le fichier ${ fichier } (${ description }) a été uploadé !" /></p></c:if>
</body>
</html>


   