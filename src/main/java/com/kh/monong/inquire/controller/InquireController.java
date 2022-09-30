package com.kh.monong.inquire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.monong.inquire.model.dto.Inquire;
import com.kh.monong.inquire.model.service.InquireService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/inquire")
public class InquireController {

	@Autowired InquireService inquireService;
	
	@GetMapping("/inquireAdmin.do")
	public void inquireAdmin() {};
	
	/**
	 * 회원 -> 관리자 문의
	 * @param inquire
	 * @param redirectAttr
	 * @return
	 */
	@PostMapping("/inquireAdmin.do")
	public ResponseEntity<?> insertInquire(Inquire inquire, RedirectAttributes redirectAttr) {
		int result = inquireService.insertInquire(inquire);
		String msg = "";
		if(result == 1)
			msg = "close";
		return ResponseEntity.ok().body(msg);
	}
}
