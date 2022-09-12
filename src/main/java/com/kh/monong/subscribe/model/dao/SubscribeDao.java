package com.kh.monong.subscribe.model.dao;

import org.apache.ibatis.annotations.Insert;

import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.kh.monong.subscribe.model.dto.CardInfo;
import com.kh.monong.subscribe.model.dto.Subscription;
import com.kh.monong.subscribe.model.dto.SubscriptionOrder;

import lombok.NonNull;
import com.kh.monong.subscribe.model.dto.SubscriptionProduct;
import com.kh.monong.subscribe.model.dto.SubscriptionReview;
import com.kh.monong.subscribe.model.dto.Vegetables;


@Mapper
public interface SubscribeDao {

	// 선아코드 시작
	@Insert("insert into card_info values(seq_card_info_no.nextval, #{cardNo}, #{cardExpireDate}, #{cardBirthDate}, #{cardPassword}, #{customerUid})")
	int insertCardInfo(CardInfo cardInfo);
	
	@Select("select card_info_no from card_info where customer_uid = #{customerUid}")
	int findCardInfoNoByUid(String customerUid);
	
	@Insert("insert into subscription values(#{sNo}, #{cardInfoNo}, #{memberId}, #{sProductCode}, #{sExcludeVegs}, #{sDeliveryCycle}, #{sNextDeliveryDate}, default, #{sRecipient}, #{sPhone}, #{sAddress}, #{sAddressEx}, #{sDeliveryRequest})")
	int insertSubscription(Subscription subscription);
	
	@Insert("insert into subscription_order values(#{sOrderNo}, #{sNo}, default, #{sPrice}, default, default)")
	int insertSubscriptionOrder(SubscriptionOrder subscriptionOrder);
	
	SubscriptionOrder selectSubscriptionOrderRecent(String sNo);
	
	@Select("select * from subscription_product where s_product_code = #{sProductCode}")
	SubscriptionProduct selectProductInfoByCode(String sProductCode);
	
	@Select("select s.*, ci.* from subscription s left join card_info ci on s.card_info_no = ci.card_info_no where customer_uid = #{customerUid}")
	Subscription findNextDeliveryDateByUid(String customerUid);
	
	
	
	// 선아코드 끝
	
	// 미송코드 시작
	@Select("select * from subscription_product")
	List<SubscriptionProduct> getSubscriptionProduct();
	
	@Select("select * from vegetables")
	List<Vegetables> getVegetables();
	// 미송코드 끝


	

	List<SubscriptionReview> selectSubscriptionReviewListCollection();

}
