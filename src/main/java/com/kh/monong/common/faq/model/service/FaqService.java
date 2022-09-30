package com.kh.monong.common.faq.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.kh.monong.common.faq.model.dto.Faq;

public interface FaqService {
	
	List<Faq> getFaqList(Map<String, Integer> param);

	List<Faq> searchLikeKeyword(String keyword);

	int getTotalContent();

	List<Faq> getFaqAllList();
	
	List<Faq> searchType(String type);

	List<Faq> getFaqListType();

	List<Faq> findType(String faqType);


	
}
