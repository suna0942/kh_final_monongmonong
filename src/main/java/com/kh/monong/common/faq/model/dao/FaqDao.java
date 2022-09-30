package com.kh.monong.common.faq.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.http.ResponseEntity;

import com.kh.monong.common.faq.model.dto.Faq;

public interface FaqDao {
	
	@Select("select * from faq order by faq_no")
	List<Faq> getFaqList(RowBounds rowBounds);
	
	@Select("select * from faq where faq_title like '%' || #{keyword} || '%' or faq_content like '%' || #{keyword} || '%'")
	List<Faq> searchLikeKeyword(String keyword);
	
	@Select("select count(*) from faq")
	int getTotalContent();
	
	@Select("select * from faq order by faq_no")
	List<Faq> getFaqAllList();
	
	@Select("select * from faq where faq_type = #{type}")
	List<Faq> searchType(String type);
	
	@Select("select faq_type from faq group by faq_type")
	List<Faq> getFaqListType();

	@Select("select * from faq where faq_type = #{faqType}")
	List<Faq> findType(String faqType);

	
	
	
}
