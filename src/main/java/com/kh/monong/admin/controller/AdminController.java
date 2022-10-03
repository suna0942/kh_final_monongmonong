package com.kh.monong.admin.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.monong.common.MonongUtils;
import com.kh.monong.common.MailUtils;
import com.kh.monong.direct.model.dto.DirectProduct;
import com.kh.monong.direct.model.service.DirectService;
import com.kh.monong.inquire.model.dto.Inquire;
import com.kh.monong.inquire.model.dto.InquireAnswer;
import com.kh.monong.inquire.model.service.InquireService;
import com.kh.monong.member.model.dto.Member;
import com.kh.monong.member.model.dto.Seller;
import com.kh.monong.member.model.dto.SellerInfoAttachment;
import com.kh.monong.member.model.service.MemberService;
import com.kh.monong.notice.model.dto.MemberNotification;
import com.kh.monong.notice.model.dto.MessageType;
import com.kh.monong.notice.model.service.NotificationService;
import com.kh.monong.subscribe.model.dto.Subscription;
import com.kh.monong.subscribe.model.dto.SubscriptionOrder;
import com.kh.monong.subscribe.model.dto.SubscriptionWeekVegs;
import com.kh.monong.subscribe.model.dto.Vegetables;
import com.kh.monong.subscribe.model.service.SubscribeService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	MemberService memberService;
	
	@Autowired
	ServletContext application;
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	InquireService inquireService;
	
	@Autowired
	DirectService directService;

	@Autowired
	SubscribeService subscribeService;
	
	@Autowired
	NotificationService notificationService;
	//--------------------------------------------------------수아시작
	@GetMapping("/memberList.do")
	public void memberList(@RequestParam(defaultValue = "1") int cPage, Model model, HttpServletRequest request) {
		Map<String, Integer> param = new HashMap<>();
		int limit = 5;
		param.put("cPage", cPage);
		param.put("limit", limit);
		int totalContent = memberService.getTotalMember();
		List<Member> memberList = memberService.findAllMember(param);
		log.debug("list={}", memberList);
				
		String url = request.getRequestURI(); 
		String pagebar = MonongUtils.getPagebar(cPage, limit, totalContent, url);
		
		model.addAttribute(memberList);
		model.addAttribute("pagebar", pagebar);
	}
	
	@GetMapping("/sellerList.do")
	public void sellerList(@RequestParam(defaultValue = "1") int cPage, Model model, HttpServletRequest request) {
		Map<String, Integer> param = new HashMap<>();
		int limit = 5;
		param.put("cPage", cPage);
		param.put("limit", limit);
		int totalContent = memberService.getTotalSeller();
		List<Seller> sellerList = memberService.findAllSeller(param);
		log.debug("list={}", sellerList);
		
		String totalWaitSeller = Integer.toString(memberService.getTotalWaitSeller());
		log.debug("totalWaitSeller={}", totalWaitSeller);

		String url = request.getRequestURI(); 
		String pagebar = MonongUtils.getPagebar(cPage, limit, totalContent, url);
		
		model.addAttribute("totalWaitSeller",totalWaitSeller);
		model.addAttribute("sellerList",sellerList);
		model.addAttribute("pagebar", pagebar);
	}
	
	@GetMapping("/sellerWaitList.do")
	public void sellerWaitList(@RequestParam(defaultValue = "1") int cPage, Model model, HttpServletRequest request) {
		Map<String, Integer> param = new HashMap<>();
		int limit = 10;
		param.put("cPage", cPage);
		param.put("limit", limit);
		List<Seller> sellerWaitList = memberService.findWaitSeller(param);
		int totalContent = memberService.getTotalWaitSeller();
		
		log.debug("sellerWaitList={}", sellerWaitList);
		String totalWaitSeller = Integer.toString(memberService.getTotalWaitSeller());
		log.debug("totalWaitSeller={}", totalWaitSeller);
		String totalSellerEnroll = Integer.toString(memberService.getTotalSellerEnrollByMonth());
		String url = request.getRequestURI(); 
		String pagebar = MonongUtils.getPagebar(cPage, limit, totalContent, url);
		
		model.addAttribute("totalSellerEnroll",totalSellerEnroll);
		model.addAttribute("totalWaitSeller",totalWaitSeller);
		model.addAttribute("sellerWaitList",sellerWaitList);
		model.addAttribute("pagebar", pagebar);
	}
	
	@GetMapping(path= "/fileDownload.do", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public Resource fileDownload(@RequestParam int no, HttpServletResponse response) throws IOException {
		SellerInfoAttachment attach = memberService.selectSellerAttach(no);
		log.debug("attach= {}", attach);
		String saveDirectory = application.getRealPath("/resources/upload/sellerRegFiles");
		File downFile = new File(saveDirectory, attach.getRenamedFilename());
		String location = "file:"+downFile;
		Resource resource = resourceLoader.getResource(location);
		
		response.setContentType("application/octet-stream; charset=utf-8");
		String filename = new String(attach.getOriginalFilename().getBytes("utf-8"), "iso-8859-1");
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +filename+ "\"");
		
		return resource;
	}
	
	@PostMapping("/updateSellerStatus.do")
	public String updateSellerStatus(@RequestParam String id,RedirectAttributes redirectAttr) {
		int result = memberService.updateSellerStatus(id);
		redirectAttr.addFlashAttribute("msg", "승인 처리가 완료되었습니다.");
		return "redirect:/admin/sellerWaitList.do";
	}
	
	@Inject
	private JavaMailSender mailSender;
	
	@PostMapping("deleteSellerStatus.do")
	public String deleteSellerStatus(@RequestParam String name, @RequestParam String email,RedirectAttributes redirectAttr) {
		try {
			MailUtils sendMail = new MailUtils(mailSender);
			Map<String, Object> map = new HashMap<>();
			map.put("email", email);
			map.put("name", name);
			Member member = memberService.findMemberId(map);
			sendMail.setSubject("모농모농 판매자 승인 거절");
			sendMail.setText("<h1>안녕하세요 모농모농입니다.</h1>"
					+ "<br /> 개인농산물판매자 자격 검토 결과, 적합하지 않은 가입조건으로 확인되어"
					+ "<br />" + member.getMemberName()+"님의 판매자 승인이 거절되었습니다."
							+ "<br />적합한 가입조건으로 다시 가입을 진행해주시기 바랍니다."
							+ "<br />감사합니다!");
			sendMail.setFrom("sooappeal31@gmail.com", "모농모농");
			sendMail.setTo(member.getMemberEmail());
			sendMail.send();
			
			int deleteSeller = memberService.deleteSeller(member.getMemberId());
			
		} catch (Exception e) {
			log.error("가입거절 이메일 전송오류 : " + e.getMessage(), e);
		}
		redirectAttr.addFlashAttribute("msg", "가입거절 처리가 완료되었습니다.");
		return "redirect:/admin/sellerWaitList.do";
	}
	//--------------------------------------------------------수아끝
	//--------------------------------------------------------수진시작
	@GetMapping("/adminInquireList.do")
	public void adminInquireList(@RequestParam String memberType, 
								@RequestParam(defaultValue = "1") int cPage,
								Model model, HttpServletRequest request) {
		Map<String, Object> param = new HashMap<>();
		int limit = 10;
		param.put("cPage", cPage);
		param.put("limit", limit);
		String auth = "";
		switch (memberType) {
		case "seller":
			auth = "ROLE_SELLER";
			break;
		case "member":
			auth = "ROLE_MEMBER";
			break;
		}
		param.put("auth", auth);
		List<Inquire> inqList = inquireService.selectInquireListByMemberType(param);
		model.addAttribute("inqList", inqList);
		
		int totalContent = inquireService.getTotalInqureContent(param);
		String url = request.getRequestURI();
		String pagebar = MonongUtils.getPagebar(cPage, limit, totalContent, url);
		model.addAttribute("pagebar",pagebar);
	};
	
	/**
	 * 관리자 -> 회원 답변
	 */
	@PostMapping("/inquireAnswer.do")
	public ResponseEntity<?> insertInquireAnswer(InquireAnswer inqAnswer, MemberNotification notice) {		
		//답변저장
		int result = inquireService.insertInquireAnswer(inqAnswer);
		
		//알림정보 저장
		String content = "문의글 ["+MonongUtils.subStrContent(notice.getNotiContent())+"]에 관리자가 답변을 달았습니다.";	
		notice.setNotiContent(content);
		notice.setMessageType(MessageType.INQ_ANSWERD);
		
		result = notificationService.insertNotification(notice);
				
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@GetMapping("/directProductList.do")
	public void adminDirectProdList(@RequestParam(defaultValue = "1") int cPage, 
			@RequestParam(defaultValue = "판매중") String dSaleStatus,
			Model model, HttpServletRequest request) {
		Map<String, Object> param = new HashMap<>();
		int limit = 5;
		param.put("cPage", cPage);
		param.put("limit", limit);
		param.put("dSaleStatus", dSaleStatus);
		List<DirectProduct> prodList = directService.adminSelectPordList(param);
		
		model.addAttribute("prodList", prodList);
		
		int totalContent = directService.getTotalProdCntByStatus(param);
		
		String url = request.getRequestURI(); 
		url += "?dSaleStatus=" + dSaleStatus;
		String pagebar = MonongUtils.getPagebar(cPage, limit, totalContent, url);
		model.addAttribute("pagebar", pagebar);
	}
	//--------------------------------------------------------수진끝
	//--------------------------------------------------------선아 시작
	/**
	 * 구독 신청 리스트
	 */
	@GetMapping("/subscriptionList.do")
	public void subscriptionList(@RequestParam(defaultValue = "1") int cPage, Model model, HttpServletRequest request) {
		Map<String, Integer> param = new HashMap<>();
		int limit = 5;
		param.put("cPage", cPage);
		param.put("limit", limit);
		List<Subscription> subscriptionList = subscribeService.getSubscriptionListAll(param);
		int totalContent = subscribeService.getTotalSubscriptionListAll();

		String url = request.getRequestURI(); 
		String pagebar = MonongUtils.getPagebar(cPage, limit, totalContent, url);
		
		model.addAttribute("subscriptionList",subscriptionList);
		model.addAttribute("pagebar", pagebar);
		model.addAttribute("totalContent", totalContent);
	}
	
	@GetMapping("/subscriptionListDetail.do")
	public void subscriptionListBySubNo(
			@RequestParam String subNo, Model model,
			@RequestParam(defaultValue = "1") int cPage, HttpServletRequest request
			) {
		Map<String, Object> param = new HashMap<>();
		int limit = 5;
		param.put("cPage", cPage);
		param.put("limit", limit);
		param.put("subNo", subNo);
		
		List<SubscriptionOrder> subscriptionListDetail = subscribeService.getSubscriptionListBySubNo(param);
		int totalContent = subscribeService.getTotalSubscriptionListBySubNo(subNo);

		String url = request.getRequestURI();
		url += "?subNo=" + subNo;
		String pagebar = MonongUtils.getPagebar(cPage, limit, totalContent, url);
		
		model.addAttribute("subscriptionListDetail",subscriptionListDetail);
		model.addAttribute("pagebar", pagebar);
		model.addAttribute("totalContent", totalContent);
	}
	
	
	/**
	 * 구독 여부 조회
	 */
	@GetMapping("/findByQuitYn.do")
	public ResponseEntity<?> findByQuitYnSubscriptionList(@RequestParam(defaultValue = "1") int cPage, @RequestParam String selectOption, Model model, HttpServletRequest request) {
		Map<String, Integer> param = new HashMap<>();
		int limit = 5;
		param.put("cPage", cPage);
		param.put("limit", limit);
		
		List<Subscription> findSubscriptionList = subscribeService.findByQuitYnSubList(selectOption, param);
		int findTotalContent = subscribeService.getTotalFindByQuitYnSubList(selectOption);
		
		Map<Object, Object> map = new HashMap<>();
		map.put("findSubscriptionList",findSubscriptionList);
		map.put("cPage", cPage);
		map.put("findTotalContent", findTotalContent);
		
		return ResponseEntity.ok().body(map);
	}
	
	/**
	 * 구독 결제 완료 리스트
	 */
	@GetMapping("/subscriptionOrderList.do")
	public void subscriptionOrderList(
			@RequestParam(defaultValue = "1") int cPage,
			@RequestParam(defaultValue = "상품준비중") String deliveryStatus,
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchStartDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchEndDate,
			Model model, HttpServletRequest request) {
		log.debug("searchStartDate, searchEndDate = {}, {}", searchStartDate, searchEndDate);
		Map<String, Object> param = new HashMap<>();
		int limit = 5;
		param.put("cPage", cPage);
		param.put("limit", limit);
		param.put("deliveryStatus", deliveryStatus);
		
		List<SubscriptionOrder> subscriptionOrderList = null;
		int totalContent = 0;
		
		if(searchStartDate == null && searchEndDate == null) {
			subscriptionOrderList = subscribeService.getSubscriptionOrderListAll(param);
			totalContent = subscribeService.getTotalSubscriptionOrderListAll(deliveryStatus);
		}
		if(searchStartDate != null && searchEndDate != null) {
			param.put("searchStartDate", searchStartDate);
			param.put("searchEndDate", searchEndDate);
			subscriptionOrderList = subscribeService.searchPeriodData(param);
			totalContent = subscribeService.getTotalsearchPeriodData(param);
			
			model.addAttribute("searchStartDate", searchStartDate);
			model.addAttribute("searchEndDate", searchEndDate);
		}
		String url = request.getRequestURI();
		url += "?deliveryStatus=" + deliveryStatus;
		
		String pagebar = MonongUtils.getPagebar(cPage, limit, totalContent, url);
		
		model.addAttribute("subscriptionOrderList",subscriptionOrderList);
		model.addAttribute("pagebar", pagebar);
		model.addAttribute("deliveryStatus", deliveryStatus);
		model.addAttribute("totalContent", totalContent);
	}
	
	/**
	 * 구독 배송상태 변경
	 */
	@PostMapping("/subDeliveryUpdate.do")
	public ResponseEntity<?> subDeliveryUpdate(@RequestParam String changeDStatus, @RequestParam String subOrderNo) {
		Map<String, Object> param = new HashMap<>();
		param.put("subOrderNo", subOrderNo);
		param.put("changeDStatus", changeDStatus);
		int result = subscribeService.updateSubDelivery(param);
		
		String content = "정기구독상품이 " +changeDStatus + ("배송완료".equals(changeDStatus) ? "되었습니다." : "입니다.");
		String memberId = subscribeService.selectMemberIdBySoNo(subOrderNo);
		MemberNotification notice = MemberNotification.builder()
				.memberId(memberId)
				.notiContent(content)
				.dOrderNo(subOrderNo)
				.messageType(MessageType.SO_STATUS)
				.build();	
		result = notificationService.insertNotification(notice);
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	/**
	 * 구독 배송상태 스케쥴(상품준비중 -> 배송중) 매주 금요일 진행
	 */
	public void dStatusschedule(){
		LocalDate today = LocalDate.now();
		int todayDay = today.getDayOfWeek().getValue();
		// 오늘이 금요일 경우 스케쥴 진행되며 한 번 더 확인
		if(todayDay == 5) {
			// 현재 날짜와 배송예정일이 일치하는 주문건 조회
			List<SubscriptionOrder> orderLists = subscribeService.getSubOrderList(today);
			if(orderLists != null) {
				for(SubscriptionOrder orderList : orderLists) {
					String status = orderList.getSOrderStatus();
					// 혹시라도 상품준비중이 아닐 경우
					if(!"상품준비중".equals(status)) return;
					
					Map<String, Object> param = new HashMap<>();
					param.put("subOrderNo", orderList.getSOrderNo());
					param.put("changeDStatus", "배송중");
					int result = subscribeService.updateSubDelivery(param);
				}
			}
		}
	}
	
	
	//--------------------------------------------------------선아 끝
	//------------------------------------------수아 시작
	
		@GetMapping("/noticeWeekVegs.do")
		public void noticeWeekVegs(Model model) {
			List<Vegetables> vegetables = subscribeService.getVegetables();
			log.debug("vegetables = {}", vegetables);
			model.addAttribute("vegetables", vegetables);
			
		}
		
		@PostMapping("/noticeWeekVegs.do")
		public String noticeWeekVegs(SubscriptionWeekVegs subscriptionWeekVegs, RedirectAttributes redirectAttr) {
			int result = subscribeService.insertSubscriptionWeekVegs(subscriptionWeekVegs);
			
			redirectAttr.addFlashAttribute("msg", "채소 공지가 등록되었습니다.");
			return "redirect:/admin/noticeWeekVegsList.do";
		}
		
		@GetMapping("/noticeWeekVegsList.do")
		public void noticeWeekVegsList(Model model, @RequestParam(defaultValue = "1") int cPage, HttpServletRequest request) {
			Map<String, Integer> param = new HashMap<>();
			int limit = 10;
			param.put("cPage", cPage);
			param.put("limit", limit);
			int totalContent = subscribeService.getTotalSubscriptionWeekVegsContent();
			log.debug("totalContent = {}", totalContent);
			List<SubscriptionWeekVegs> noticeVegsList = subscribeService.selectSubscriptionWeekVegsList(param);
			String url = request.getRequestURI();
			String pagebar = MonongUtils.getPagebar(cPage, limit, totalContent, url);
			model.addAttribute("noticeVegsList", noticeVegsList);
			model.addAttribute("pagebar", pagebar);
		}
		
		@GetMapping("/noticeVegs.do")
		public String noticeVegs(@RequestParam String weekCriterion) {
			SubscriptionWeekVegs subscriptionWeekVegs = subscribeService.selectOneSubscriptionWeekVegs(weekCriterion);
			if(subscriptionWeekVegs == null) {
				return "redirect:/admin/noticeWeekVegs.do";
			}
			return "redirect:/admin/noticeWeekVegsUpdateForm.do?weekCriterion="+weekCriterion;
		}
		@GetMapping("/noticeWeekVegsUpdateForm.do")
		public void noticeWeekVegsUpdateForm(@RequestParam String weekCriterion, Model model) {
			List<Vegetables> vegetables = subscribeService.getVegetables();
			log.debug("vegetables = {}", vegetables);
			model.addAttribute("vegetables", vegetables);
			SubscriptionWeekVegs subscriptionWeekVegs = subscribeService.selectOneSubscriptionWeekVegs(weekCriterion);
			model.addAttribute("subscriptionWeekVegs", subscriptionWeekVegs);
		}
		
		@PostMapping("/noticeWeekVegsUpdate.do")
		public String noticeWeekVegsUpdate(SubscriptionWeekVegs subscriptionWeekVegs, RedirectAttributes redirectAttr) {
			int result = subscribeService.updateSubscriptionWeekVegs(subscriptionWeekVegs);
			if(result != 0) {
				
				redirectAttr.addFlashAttribute("msg", "주간채소 공지가 수정되었습니다.");	
			}
			return "redirect:/admin/noticeWeekVegsList.do";
		}
		
		
		
		
		//----------------------------------------수아 끝
}