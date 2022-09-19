<%@page import="com.kh.monong.member.model.dto.Member"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<fmt:requestEncoding value="utf-8" />
<jsp:include page="/WEB-INF/views/member/memberMyPage.jsp">
	<jsp:param name="title" value="모농모농-마이페이지"></jsp:param>
</jsp:include>
<div id="member-orderList-container" class="mx-auto mt-5 text-center">
<c:if test="${empty directOrderList}">
	<h3>주문 내역이 없습니다.</h3>
</c:if>
<c:if test="${not empty directOrderList}">
	<c:forEach items="${directOrderList}" var="oList" varStatus="vs">			
	 <c:forEach items="${prodList}" var="pList">
		<table id="member-orderList-tbl" class="table table-borderless text-center">
			<thead>
			  <tr class="table-active">
			    <th>
			     <fmt:parseDate value="${oList.DOrderDate}" pattern="yyyy-MM-dd'T'HH:mm" var="orderDate"/>
				 <fmt:formatDate value="${orderDate}" pattern="yyyy-MM-dd"/>
			    </th>
			    <th></th>
			    <th></th>
			    <th></th>
			    <th><a href="${pageContext.request.contextPath}/member/memberDirectDetail.do?dOrderNo=${oList.DOrderNo}">주문 상세보기</a></th>
			  </tr>
			</thead>
			<tbody>
			  <tr>
			  <c:if test="${not empty prodAttachList}">
				  <c:forEach items="${prodAttachList}" var="aList">
				    <td rowspan="3">
				    	<img src="${pageContext.request.contextPath}/resources/upload/product/${aList.DProductRenamedFilename}" alt="" />
				    </td>
				  </c:forEach>
			  </c:if>
			  <c:if test="${empty prodAttachList}">
				  <td rowspan="3">
				    	<img src="${pageContext.request.contextPath}/resources/images/logo.PNG" alt="" />
				  </td>
			  </c:if>
			    <td>상품명</td>
			    <td>${pList.DProductName}</td>
			    <td></td>
			    <td>
			    </td>
			  </tr>
			  <tr>
			    <td>주문번호</td>
			    <td>${oList.DOrderNo}</td>
			    <td></td>
			    <td class="member-mypage-active-color">
			    	<c:choose>
				    	<c:when test="${oList.DOrderStatus == 'P'}">
				    		<c:out value="결제완료"/>
				    	</c:when>
				    	<c:when test="${oList.DOrderStatus == 'R'}">
				    		<c:out value="상품준비중"/>
				    	</c:when>
				    	<c:when test="${oList.DOrderStatus == 'C'}">
				    		<c:out value="주문취소"/>
				    	</c:when>
				    	<c:when test="${oList.DOrderStatus == 'D'}">
				    		<c:out value="배송중"/>
				    	</c:when>
				    	<c:otherwise>
				    		<c:out value="배송완료"/>
				    	</c:otherwise>
				    </c:choose>
			    </td>
			  </tr>
			  <tr>
			    <td>결제수단</td>
			    <td>${oList.DPayments}</td>
			    <td></td>
			    <td>
			    </td>
			  </tr>
			  <tr>
			    <td></td>
			    <td>결제금액</td>
			    <td>${oList.DTotalPrice} 원</td>
			    <td></td>
			    <td></td>
			  </tr>
			</tbody>
		</table>
		 
		</c:forEach> 
	</c:forEach> 
	
</c:if>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>