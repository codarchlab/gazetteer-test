<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="l"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=utf-8" session="false"%>

<s:message code="ui.search.result" var="pageTitle"/>
<s:message code="ui.search.hits" var="pageSubtitleHits"/>

<s:url var="searchAction" value="/search" />

<l:page title="${pageTitle}">

	<jsp:attribute name="subtitle">
		${hits} ${pageSubtitleHits}
	</jsp:attribute>

	<jsp:body>
	
		<div class="subnav">
			<ul class="nav nav-pills">
				<li>
					<a href="${searchAction}?limit=10&offset=0&view=${view}&q=${q}" style="border: none"><i class="icon-stop"></i> 10</a>
				</li>
				<li>
					<a href="${searchAction}?limit=100&offset=0&view=${view}&q=${q}" style="border: none"><i class="icon-th-large"></i> 100</a>
				</li>
				<li>
					<a href="${searchAction}?limit=1000&offset=0&view=${view}&q=${q}" style="border-left: none"><i class="icon-th"></i> 1000</a>
				</li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						<s:message code="ui.list.view" text="Darstellung"/>
						<b class="caret"></b>
					</a>
					<ul class="dropdown-menu">
						<li>
							<a href="${searchAction}?limit=${limit}&offset=${offset}&q=${q}">
								<i class="icon-globe"></i> <i class="icon-list"></i> <s:message code="ui.search.view.mapAndTable" />
							</a>
						</li>
						<li>
							<a href="${searchAction}?limit=${limit}&offset=${offset}&view=map&q=${q}">
								<i class="icon-globe"></i> <s:message code="ui.search.view.map" />
							</a>
						</li>
						<li>
							<a href="${searchAction}?limit=${limit}&offset=${offset}&view=table&q=${q}">
								<i class="icon-list"></i> <s:message code="ui.search.view.table" />
							</a>
						</li>
					</ul>
				</li>
				<li class="pull-right">
					<ul class="pagination">
						<c:choose>
							<c:when test="${offset-limit >= 0}">
								<li><a
							href="${searchAction}?limit=${limit}&offset=${offset-limit}&view=${view}&q=${q}">&larr; <s:message
									code="ui.previous" /></a></li>
							</c:when>
							<c:otherwise>
								<li class="disabled"><a href="#">&larr; <s:message
									code="ui.previous" /></a></li>
							</c:otherwise>
						</c:choose>
						<li class="disabled">
							<a href="#">
								<s:message code="ui.page" text="Seite" />
								<%--
									<c:set var="currentPage" value="${fn:substringBefore(offset/limit + 1, '.')}"/>
									<c:set var="totalPages" value="${fn:substringBefore(hits/limit + 0.999, '.')}"/>
									<select style="width: auto; display: inline">
										<c:forEach var="i" begin="1" end="${totalPages}">
											<c:choose>
												<c:when test="${i == currentPage}">
													<option selected="selected">${i}</option>
												</c:when>
												<c:otherwise>
													<option>${i}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								--%>
								${fn:substringBefore(offset/limit + 1, '.')} / ${fn:substringBefore(hits/limit + 0.999, '.')}
							</a>
						</li>
						<c:choose>
							<c:when test="${offset+limit < hits}">
								<li>
									<a href="${searchAction}?limit=${limit}&offset=${offset+limit}&view=${view}&q=${q}">
									<s:message code="ui.next" text="Vor" /> &rarr;</a>
								</li>
							</c:when>
							<c:otherwise>
								<li class="disabled">
									<a href="#"><s:message code="ui.next" text="Vor" /> &rarr;</a>
								</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</li>
			</ul>
			
		</div>
		
		<div class="row-fluid">
			
			<c:if test="${fn:contains(view, 'map')}">
				<c:set var="mapSpan" value="12"/>
				<c:if test="${fn:contains(view, 'table')}">
					<c:set var="mapSpan" value="5"/>
				</c:if>
				<div class="span${mapSpan} well">
					<l:map places="${places}" height="500px"/>	
				</div>
			</c:if>
			
			<c:if test="${fn:contains(view, 'table')}">
				<c:set var="tableSpan" value="12"/>
				<c:if test="${fn:contains(view, 'map')}">
					<c:set var="tableSpan" value="7"/>
				</c:if>
				<div class="span${tableSpan}">
					<c:choose>
						<c:when test="${fn:length(places) > 0}">
							
							<table class="table table-striped">
								<thead>
									<tr>
										<td>#</td>
										<td><s:message code="domain.placename.title" text="domain.placename.title"/></td>
										<c:if test="${!fn:contains(view, 'map')}">
											<td><s:message code="domain.place.type" text="domain.place.type"/></td>
											<td><s:message code="domain.location.coordinates"/></td>
										</c:if>
										<td><s:message code="domain.thesaurus" text="domain.thesaurus"/></td>
										<td><s:message code="domain.place.uri" text="domain.place.uri"/></td>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="place" items="${places}">				
										<tr>
											<td>${place.id}</td>
											<td><a href="doc/${place.id}.html?limit=${limit}&offset=${offset}&q=${q}&view=${view}">${place.prefName.title}</a></td>
											<c:if test="${!fn:contains(view, 'map')}">
												<td>${place.type}</td>
												<td>
													<c:forEach var="location" items="${place.locations}" varStatus="status">
														${location.lat}, ${location.lng}
														<c:if test="${status.count lt fn:length(place.locations)}">,</c:if>
													</c:forEach>
												</td>
											</c:if>
											<td>${place.thesaurus}</td>
											<td>
												<s:message code="ui.copyToClipboard" var="copyMsg"/>
												<div class="modal hide" id="copyUriModal">
													<div class="modal-header">
														<button type="button" class="close" data-dismiss="modal">×</button>
														<h3><s:message code="ui.copyToClipboardHeading"/></h3>
													</div>
													<div class="modal-body">
														<label>${copyMsg}</label>
														<input class="input-xxlarge" type="text" value="${baseUri}place/${place.id}" id="copyUriInput">
													</div>
												</div>
												<script type="text/javascript">
													$("#copyUriModal").on("shown",function() {
														$("#copyUriInput").focus().select();
													});
												</script>
												<a data-toggle="modal" href="#copyUriModal"><i class="icon-share"></i></a>
											</td>				
										</tr>				
									</c:forEach>
								</tbody>		
							</table>
						
						</c:when>
						<c:otherwise>
							<div class="row-fluid">
								<div class="span12 lead">
									<s:message code="ui.search.emptyResult" />
								</div>
							</div>
						</c:otherwise>					
					</c:choose>
				</div>
			</c:if>
			
		</div>
				
	</jsp:body>

</l:page>