<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta charset="UTF-8">
<title>관리자문의</title>
<script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
<!-- 부트스트랩 -->
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
<!-- boot css -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

<style>
@font-face {
	font-family: 'GmarketSansMedium';
	src:
		url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff')
		format('woff');
	font-weight: normal;
	font-style: normal;
}
.btn-EA5C2B-reverse {
	color: #EA5C2B;
	border: 1px solid #EA5C2B;
}
a {
	text-align: none;
	color: #333;
}
body {
	font-family: 'GmarketSansMedium', sans-serif;
	font-weight: 400;
	font-size: 16px;
	color: #333;
}
.btn-EA5C2B-reverse:hover {
	color: #fff;
	background-color: #EA5C2B;
}

</style>
</head>
<body>
	<div class="mx-auto mt-5 text-center">
		<h3>관리자 문의하기</h3>
	</div>
	<div id="enroll-container" class="mx-auto d-flex justify-content-center">
		<form name="inquireFrm" action="" method="POST" accept-charset="UTF-8" >
			<label for="inquireTitle" class="item">제목 : </label>
			<input type="text" class="form-control item" name="inquireTitle" id="inquireTitle" required/>
			<label for="inquireContent">내용 : </label>
			<textarea name="inquireContent" class="form-control" id="inquireContent" cols="30" rows="10" style="resize: none;" required></textarea>
			<input type="hidden" class="form-control" name="memberId" id="memberId" value='<sec:authentication property="principal.username"/>'/>
			<sec:csrfInput />
			<br />
			<div class="mb-3 text-center">
				<input type="button" class="btn btn-EA5C2B-reverse inquireSubmit" value="제출">&nbsp;
				<input type="reset" class="btn btn-outline-success" value="취소">
			</div>
		</form>
	</div>
</body>
<script>
document.querySelector(".inquireSubmit").addEventListener('click', () => {
	const frm = document.inquireFrm;
	const inquireTitle = frm.inquireTitle.value;
	const inquireContent = frm.inquireContent.value;
	const memberId = frm.memberId.value;
	const headers = {};
 	headers['${_csrf.headerName}'] = '${_csrf.token}';
 	
	$.ajax({
		url: "${pageContext.request.contextPath}/inquire/inquireAdmin.do",
		method: "POST",
		headers,
		data: {memberId, inquireTitle, inquireContent},
		success(response){
			if(response == 'close'){
				alert("관리자 문의가 등록되었습니다.");
				window.close();
			}
		},
		error(jqxhr, statusText, err){
			console.log(jqxhr, statusText, err);
		}
	});
});
</script>
</html>