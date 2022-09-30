<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
</section>
<footer>
	<nav class="footer1">
		<div class="service-center">
			<span>모농모농 고객센터</span>
			<span style="color: #EA5C2B">&#128222;02-5959-8282</span><br />
			<span>월요일 ~ 금요일</span>
			<span>10:00 ~ 18:00 (점심시간 12:00 ~ 13:00)</span><br />
			<span>토 · 일 · 공휴일</span>
			<span>휴무</span>
		</div>
		<div>
			<input type="button" class="btn btn-EA5C2B inquireAdmin" value="문의하기">
			<input type="button" class="btn btn-EA5C2B-reverse" onclick="faq()" value="자주 묻는 질문">
		</div>
	</nav>
	<nav class="footer2">
		<img src="${pageContext.request.contextPath}/resources/images/logo.PNG" class="logo" alt="모농모농 로고">
		<div>
			<span>상호명: 모농모농</span>
			<span>대표: 이파리들</span>
			<span>사업자등록번호: 123-45-67890</span>
			<span>소재지: 서울특별시 강남구 테헤란로14길 6 남도빌딩</span>
		</div>
		<div>
			<span>통신판매업 신고번호: 2022-서울강남-0101</span>
			<span class="pointer">제휴 및 입점 문의: monong@monong.co.kr</span>
			<span class="pointer">농산물 납품 및 긴급구출 제보: help@monong.co.kr</span>
		</div>
		<div>
			<span>서비스 이용약관</span>
			<span>개인정보 처리방침</span>
			<span>Copyrithgⓒ 2022 주식회사 모농모농 All rights reserved.</span>
		</div>
	</nav>
	
	<div class="faqContainer faqHide">
		<div class="row faqWrapperTitle">자주 묻는 질문(FAQ)</div>
		<div class="faqWrapper"></div>
	</div>
	<div id="faqIcon" class="pointer">&#127808;</div>
</footer>

<script>
document.querySelector("#faqIcon").addEventListener('click', () => {
	const container = document.querySelector(".faqContainer");
	container.classList.toggle('faqHide');
	getFaqListType();
	
});
const getFaqListType = () => {
	const wrapper = document.querySelector(".faqWrapper");
	wrapper.innerHTML = '';
	$.ajax({
		url: `${pageContext.request.contextPath}/common/getFaqListType.do`,
		contentType : 'application/json',
		beforeSend: function(){
			wrapper.innerHTML = `
			<div class="d-flex justify-content-center">
				<div class="spinner-border text-light" role="status">
					<span class="visually-hidden">Loading...</span>
				</div>
			</div>
			`;
		},
		success(faqTypeList){
			wrapper.innerHTML = "";
			for(let i = 0; i < faqTypeList.length; i++){
				let _faqType = faqTypeList[i].faqType;
				wrapper.insertAdjacentHTML("beforeend", changeType(_faqType));
			};
		},
		error: console.log
	});
};

$(document).on('click', '[data-faqtype]', function(e){
	const container = document.querySelector(".faqContainer");
	const faqType = e.target.dataset.faqtype;
	let type = ''
	switch(faqType){
	case 's' : type = "구독"; break;
	case 'o' : type = "결제"; break;
	case 'd' : type = "직거래"; break;
	case 'm' : type = "회원"; break;
	}
	
	const wrapper = document.querySelector(".faqWrapper");
	wrapper.innerHTML = '';
	
	const selectTitle = '<div class="row faqSelectTitle" onclick="getFaqListType();"><<&nbsp&nbsp&nbsp&nbsp FAQ - ' + type + '</div>';
	wrapper.insertAdjacentHTML("beforeend", selectTitle);
	
	$.ajax({
		url: `${pageContext.request.contextPath}/common/findType.do`,
		data: {faqType},
		success(data){
			let html = ``;
			if(data.length){
				data.forEach((faq) => {
					const {faqTitle, faqContent} = faq;
					html = `
					<div class="faq-menu-title faq-bot-title">\${faqTitle}
						<p class="faq-menu-content faq-bot-content">\${faqContent}</p>
					</div>
					`;
					wrapper.insertAdjacentHTML("beforeend", html);
				});
			}
			else {
				html = `
					<div class="text-center">조회결과가 없습니다.</div>
				`;
				wrapper.insertAdjacentHTML("beforeend", html);
			};
		},
		error: console.log
	});
});

const changeType = (_faqType) => {
	let faqType = '';
	switch(_faqType){
	case 's' : faqType = "구독"; break;
	case 'o' : faqType = "결제"; break;
	case 'd' : faqType = "직거래"; break;
	case 'm' : faqType = "회원"; break;
	}
	return '<div class="row" data-faqtype="' + _faqType + '">' + faqType + '</div>';
};

$(document).on('click', '.faq-bot-title', function(e){
	$(e.target).children().slideToggle();
	$('.faq-menu-content').not($(e.target).children()).slideUp();
});

// 문의하기
document.querySelector(".inquireAdmin").addEventListener('click', () => {
	const url = '${pageContext.request.contextPath}/inquire/inquireAdmin.do';
	window.open(url, '문의하기', 'width=550, height=550');
});

const faq = () => {
	location.href = `${pageContext.request.contextPath}/common/faqList.do`;
};
</script>
<script>
if(document.querySelector('#enrollHeader')) {
	document.querySelector('#enrollHeader').addEventListener('click', (e) => {
	    clickEnrollModal();
	});
}

 function clickEnrollModal(){
		const container = document.querySelector("#enrollType-modal-container");
		const modal = `<div class="modal fade" id="enroll-type-modal" data-bs-keyboard="false" tabindex="-1" role="dialog">
				<div class="modal-dialog modal-dialog-centered" role="document" style="width: 390px;">
 					<div class="modal-content">
 						<div style="padding: 10px 1rem 0px; border-bottom: none;" class="modal-header">
 							<div style="width: 100%; display: flex; justify-content: flex-end; align-items: center;">
 								<button style="border: none; background-color: transparent; font-size: 30px; color: #333; height: 40px; width: 30px;" type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
 									<span aria-hidden="true">&times;</span>
 								</button>
 							</div>
 						</div>
 						<div class="modal-footer" style="justify-content: center; align-content: center; border-top: none; height: 185px;">
 							<button type="button" class="btn btn-EA5C2B" style="width: 180px; height: 54px;" data-bs-dismiss="modal" onclick="location.href='${pageContext.request.contextPath}/member/memberEnroll.do'">일반회원 가입</button>
 							<button type="button" class="btn btn-116530" style="width: 180px; height: 54px;"  data-bs-dismiss="modal" onclick="location.href='${pageContext.request.contextPath}/member/sellerEnroll.do'">판매자회원 가입</button>
 						</div>
 					</div>
 				</div>
 			</div>`;
 			
	 
 		 container.innerHTML = modal;
 		$('#enroll-type-modal').modal('show');
 }
</script>
<!-- footer 끝 -->
</body>
</html>