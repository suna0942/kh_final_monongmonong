package com.kh.monong.direct.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.monong.common.MonongUtils;
import com.kh.monong.direct.model.dao.DirectDao;
import com.kh.monong.direct.model.dto.Cart;
import com.kh.monong.direct.model.dto.DirectInquire;
import com.kh.monong.direct.model.dto.DirectOrder;
import com.kh.monong.direct.model.dto.DirectProduct;
import com.kh.monong.direct.model.dto.DirectProductAttachment;
import com.kh.monong.direct.model.dto.DirectProductOption;
import com.kh.monong.notice.model.dao.NotificationDao;
import com.kh.monong.notice.model.dto.MemberNotification;

import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class DirectServiceImpl implements DirectService {

	@Autowired
	private DirectDao directDao;
	
	@Autowired
	private NotificationDao notificationDao;
	//----------------- 재경 시작
	// 상품 목록
	@Override
	public List<DirectProduct> selectDirectProductList(Map<String, Integer> param) {
		// mybatis에서 제공하는 페이징처리객체 RowBounds
		// offset limit
		int limit = param.get("limit");
		int offset = (param.get("cPage") - 1) * limit;
//		RowBounds rowBounds = new RowBounds(offset, limit);
		List<DirectProduct> list  = directDao.selectDirectProductList(param);
		List<DirectProduct> subList = (List<DirectProduct>) MonongUtils.customRowBounds(offset, limit, list);
		for(DirectProduct directProduct : subList) {
			directProduct.setDirectProductAttachments(selectDirectProductAttachmentList(directProduct.getDProductNo()));
		}
		return subList;
	}
	
	@Override
	public List<DirectProductAttachment> selectDirectProductAttachmentList(String dProductNo) {
		return directDao.selectDirectProductAttachmentList(dProductNo);
	}
	
	// 최근 등록순 정렬
	@Override
	public List<DirectProduct> orderByCreatedAt(Map<String, Integer> param) {
		// mybatis에서 제공하는 페이징처리객체 RowBounds
		// offset limit
		int limit = param.get("limit");
		int offset = (param.get("cPage") - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		List<DirectProduct> list  = directDao.orderByCreatedAt(param, rowBounds);
		for(DirectProduct directProduct : list) {
			directProduct.setDirectProductAttachments(selectDirectProductAttachmentList(directProduct.getDProductNo()));
		}
			return list;
	}
	
	// 가격 높은순 정렬
	@Override
	public List<DirectProduct> orderByPriceDesc(Map<String, Integer> param) {
		// mybatis에서 제공하는 페이징처리객체 RowBounds
		// offset limit
		int limit = param.get("limit");
		int offset = (param.get("cPage") - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		List<DirectProduct> list  = directDao.orderByPriceDesc(param, rowBounds);
		for(DirectProduct directProduct : list) {
			directProduct.setDirectProductAttachments(selectDirectProductAttachmentList(directProduct.getDProductNo()));
		}
			return list;
	}
	
	@Override
	public List<DirectProduct> orderByPriceAsc(Map<String, Integer> param) {
		// mybatis에서 제공하는 페이징처리객체 RowBounds
		// offset limit
		int limit = param.get("limit");
		int offset = (param.get("cPage") - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		List<DirectProduct> list  = directDao.orderByPriceAsc(param, rowBounds);
		for(DirectProduct directProduct : list) {
			directProduct.setDirectProductAttachments(selectDirectProductAttachmentList(directProduct.getDProductNo()));
		}
			return list;
	}

	@Override
	public int getTotalContent() {
		return directDao.getTotalContent();
	}
	
	// 상품 등록
	@Override
	public int insertDirectProduct(DirectProduct directProduct) {
		// insert directProduct
		int result = directDao.insertDirectProduct(directProduct);
		log.debug("directProduct#no = {}", directProduct.getDProductNo());
		
		// insert option
		List<DirectProductOption> options = directProduct.getDirectProductOptions();
		if(options != null && !options.isEmpty()) {
			for(DirectProductOption dOpt : options) {
				dOpt.setDProductNo(directProduct.getDProductNo());
				log.debug("directProductOption#no = {}", dOpt.getDOptionNo());
				result = insertDirectProductOption(dOpt);
			}
		}
		
		// insert attachment * 4
		List<DirectProductAttachment> attachments = directProduct.getDirectProductAttachments();
		if(!attachments.isEmpty()) {
			for(DirectProductAttachment attach : attachments) {
				attach.setDProductNo(directProduct.getDProductNo());
				result = insertDirectProductAttachment(attach);
			}
		}
		return result;
	}
	
	@Override
	public int insertDirectProductAttachment(DirectProductAttachment attach) {
		return directDao.insertDirectProductAttachment(attach);
	}
	
	@Override
	public int insertDirectProductOption(DirectProductOption opt) {
		return directDao.insertDirectProductOption(opt);
	}
	
	@Override
	public List<Map<String, Object>> selectDirectProductReviewList(Map<String, Object> param) {
		int limit = (int) param.get("limit");
		int offset = ((int)param.get("cPage") - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		return directDao.selectDirectProductReviewList(param, rowBounds);
	}
	
	@Override
	public int getTotalDirectReviewByDProductNo(String dProductNo) {
		return directDao.getTotalDirectReviewByDProductNo(dProductNo);
	}
	
	// 후기 추천
	@Override
	public int getRecommendedYn(Map<String, String> param) {
		return directDao.getRecommendedYn(param);
	}
	
	@Override
	public int updateDirectReviewRecommendAdd(Map<String, String> param) {
		return directDao.updateDirectReviewRecommendAdd(param);
	}
	
	@Override
	public int updateDirectReviewRecommendCancel(Map<String, String> param) {
		return directDao.updateDirectReviewRecommendCancel(param);
	}
	
	
	//----------------- 재경 끝
	//----------------- 민지 시작
	@Override
	public DirectProduct selectOneDirectProduct(String dProductNo) {
		return directDao.selectOneDirectProductCollection(dProductNo);
	}
	
	@Override
	public List<Cart> selectCartListByMemberId(String memberId) {
		return directDao.selectCartListByMemberId(memberId);
	}
	
	@Override
	public Cart checkCartDuplicate(Map<String, Object> cart) {
		return directDao.checkCartDuplicate(cart);
	}
	
	@Override
	public int addCart(Map<String, Object> addList) {
		int result = 0;
		
		// select cartCount
		int checkCount = directDao.checkCountCartDuplicate(addList);
		log.debug("checkCount = {}", checkCount);
		// update cart (이미 카트에 담겨있을 경우)
		if(checkCount > 0) {
			result = directDao.updateCart(addList);
		}
		// insert cart (카트에 없는 경우)
		else {
			result = directDao.insertCart(addList);
		}
		return result;
	}
	
	@Override
	public int deleteCartAll(String memberId) {
		return directDao.deleteCartAll(memberId);
	}
	
	@Override
	public int deleteCartTarget(int cartNo) {
		return directDao.deleteCartTarget(cartNo);
	}
	
	@Override
	public int deleteCartChecked(int checked) {
		return directDao.deleteCartChecked(checked);
	}
	
	@Override
	public int updateCartProductCount(Map<String, Object> param) {
		return directDao.updateCartProductCount(param);
	}
		
	@Override
	public DirectProduct buyIt(Map<String, Object> param) {
		DirectProduct orderList = null;
		// select cartList
		int checkCount = directDao.checkCountCartDuplicate(param);
		log.debug("checkCount = {}", checkCount);
		// update cart (이미 카트에 담겨있을 경우)
		if(checkCount > 0) {
			// update cart
			int result = directDao.updateCartBuyIt(param);
			log.debug("result = {}", result);
			// select orderList
			orderList = directDao.selectOrderListByCartNo(param.get("cartNo"));
		}
		else {
			// insert cart
			int result = directDao.insertCartBuyIt(param);
			log.debug("result = {}", result);
			// select orderList
			orderList = directDao.selectOrderListByCartNo(param.get("cartNo"));
		}
		return orderList;
	}
	
	@Override
	public int insertDirectOrder(DirectOrder directOrder) {
		return directDao.insertDirectOrder(directOrder);
	}
	
	@Override
	public int insertMemberDirectOrder(Map<String, Object> param) {
		int result = 0;
		// insert member_direct_order
		result = directDao.insertMemberDirectOrder(param);
		// update d_stock
		result = directDao.updateStockByOptionNo(param);
		// delete cart
		result = directDao.deleteCartByOptionNoAndMemberId(param);
		// update d_sale_status where d_stock = 0
		result = directDao.updateStatusByStock();
		
		return result;
	}
	
	@Override
	public String selectReviewAvgScoreByProductNo(String dProductNo) {
		return directDao.selectReviewAvgScoreByProductNo(dProductNo);
	}
	
	@Override
	public int enrollInquire(Map<String, Object> param) {
		int result = directDao.enrollInquire(param);
		log.debug("dInqNo={}", param.get("dInquireNo"));
		result = insertNotice(param);
		return result;
	}

	@Override
	public List<DirectInquire> findInquireAll(Map<String, Object> map) {
		int limit = (int) map.get("limit");
		int offset = ((int) map.get("cPage") - 1) * limit;
		List<DirectInquire> directInquireList = directDao.findInquireAll(map);
		List<DirectInquire> subList = new ArrayList<>();
		if(!directInquireList.isEmpty()) {
			subList = (List<DirectInquire>) MonongUtils.customRowBounds(offset, limit, directInquireList);
			log.debug("subList = {}", subList);
		}
		return subList;
	}
	
	
	@Override
	public int getInquireTotalContent(String dProductNo) {
		return directDao.getInquireTotalContent(dProductNo);
	}
	
	@Override
	public int deleteInquire(int dInquireNo) {
		int result = 0;
		
		// 판매자 답변 조회
		int checkCount = directDao.checkInquireAnswer(dInquireNo);
		// 답변이 있는 경우
		if(checkCount > 0) {
			result = directDao.deleteInquireAnswer(dInquireNo);
		}
		
		result = directDao.deleteInquire(dInquireNo);
				
		log.debug("result = {}", result);
		
		return result;
	}
	//----------------- 민지 끝
	
	//----------------- 수진 시작
	
	private int insertNotice(Map<String, Object> param) {
		int result = 0;
		Integer no = (Integer) param.get("dInquireNo");
		MemberNotification noti = (MemberNotification) param.get("notice");
		noti.setDInquireNo(no);
		log.debug("noti={}", noti);
		result = notificationDao.insertNotification(noti);
		return result;
	}
	
	@Override
	public List<DirectProduct> adminSelectPordList(Map<String, Object> param) {
		int limit = (int) param.get("limit");
		int offset = ((int)param.get("cPage") - 1) * limit;
		List<DirectProduct> prodList  = directDao.adminSelectProdList(param);
		List<DirectProduct> subList = (List<DirectProduct>) MonongUtils.customRowBounds(offset, limit, prodList);
		for(DirectProduct prod : prodList) {
			prod.setDirectProductAttachments(selectDirectAttachments(prod.getDProductNo()));
		}
		return subList;
	}

	@Override
	public List<DirectProductAttachment> selectDirectAttachments(String dProductNo) {
		return directDao.selectDirectAttachments(dProductNo);
	}
	
	@Override
	public int getTotalProdCntByStatus(Map<String, Object> param) {
		return directDao.getTotalProdCntByStatus(param);
	}
	
	@Override
	public DirectProductAttachment selectOneDPAttachment(int attachNo) {
		return directDao.selectOneDPAttachment(attachNo);
	}
	
	@Override
	public int deleteDPAttachment(int attachNo) {
		return directDao.deleteDPAttachment(attachNo);
	}
	
	@Override
	public int updateDirectProduct(DirectProduct directProduct) {
		int result = directDao.updateDirectProduct(directProduct);
		
		List<DirectProductOption> options = directProduct.getDirectProductOptions();
		if(options != null && !options.isEmpty()) {
			for(DirectProductOption dOpt : options) {
				result = mergeIntoDOption(dOpt);
			}
		}
		
		List<DirectProductAttachment> attachments = directProduct.getDirectProductAttachments();
		if(attachments != null && !attachments.isEmpty()) {
			for(DirectProductAttachment attach : attachments) {
				result = insertDPAttachment(attach);
			}
		}
		return result;
	}
	
	@Override
	public int mergeIntoDOption(DirectProductOption dOpt) {
		return directDao.mergeIntoDOption(dOpt);
	}
	
	@Override
	public int insertDPAttachment(DirectProductAttachment attach) {
		return directDao.insertDPAttachment(attach);
	}
	
	@Override
	public String selectSellerIdByProdNo(String no) {
		return directDao.selectSellerIdByProdNo(no);
	}
	//----------------- 수진 끝

}
	
	