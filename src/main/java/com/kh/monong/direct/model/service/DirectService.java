package com.kh.monong.direct.model.service;

import java.util.List;
import java.util.Map;

import com.kh.monong.direct.model.dto.Cart;
import com.kh.monong.direct.model.dto.DirectInquire;
import com.kh.monong.direct.model.dto.DirectOrder;
import com.kh.monong.direct.model.dto.DirectProduct;
import com.kh.monong.direct.model.dto.DirectProductAttachment;
import com.kh.monong.direct.model.dto.DirectProductOption;

public interface DirectService {
	//----------------- 재경 시작
	// 상품 목록
	List<DirectProduct> selectDirectProductList(Map<String, Integer> param);
	
	List<DirectProductAttachment> selectDirectProductAttachmentList(String dProductNo);

	int getTotalContent();
	
	// 상품 등록
	int insertDirectProduct(DirectProduct directProduct);
	
	int insertDirectProductAttachment(DirectProductAttachment attachment);
	
	int insertDirectProductOption(DirectProductOption dopt);
	
	// 최근 등록순 정렬
	List<DirectProduct> orderByCreatedAt(Map<String, Integer> param);
	
	// 가격 높은순 정렬
	List<DirectProduct> orderByPriceDesc(Map<String, Integer> param);
	
	// 가격 낮은순 정렬
	List<DirectProduct> orderByPriceAsc(Map<String, Integer> param);
	
	// 상품 후기
	List<Map<String, Object>> selectDirectProductReviewList(Map<String, Object> param);

	int getTotalDirectReviewByDProductNo(String dProductNo);

	// 후기 추천
	int getRecommendedYn(Map<String, String> param);

	int updateDirectReviewRecommendAdd(Map<String, String> param);

	int updateDirectReviewRecommendCancel(Map<String, String> param);
	
	//----------------- 재경 끝
	//----------------- 민지 시작
	DirectProduct selectOneDirectProduct(String dProductNo);
	
	List<Cart> selectCartListByMemberId(String memberId);
	
	Cart checkCartDuplicate(Map<String, Object> cart);
	
	int addCart(Map<String, Object> addList);

	int deleteCartAll(String memberId);
	
	int deleteCartTarget(int cartNo);
	
	int deleteCartChecked(int checked);
	
	int updateCartProductCount(Map<String, Object> param);

	DirectProduct buyIt(Map<String, Object> param);
	
	int insertDirectOrder(DirectOrder directOrder);
		
	int insertMemberDirectOrder(Map<String, Object> param);
	
	String selectReviewAvgScoreByProductNo(String dProductNo);
	
	int enrollInquire(Map<String, Object> param);
	
	List<DirectInquire> findInquireAll(Map<String, Object> map);

	int getInquireTotalContent(String dProductNo);
	
	int deleteInquire(int dInquireNo);
	//----------------- 민지 끝

	//----------------- 수진 시작
	List<DirectProduct> adminSelectPordList(Map<String, Object> param);

	List<DirectProductAttachment> selectDirectAttachments(String dProductNo);
	
	int getTotalProdCntByStatus(Map<String, Object> param);
	
	DirectProductAttachment selectOneDPAttachment(int attachNo);
	
	int deleteDPAttachment(int attachNo);
	
	int updateDirectProduct(DirectProduct directProduct);
	
	int insertDPAttachment(DirectProductAttachment attach);
	
	int mergeIntoDOption(DirectProductOption dOpt);

	String selectSellerIdByProdNo(String no);
	//----------------- 수진 끝


}


