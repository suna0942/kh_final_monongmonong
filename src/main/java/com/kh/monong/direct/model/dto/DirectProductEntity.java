package com.kh.monong.direct.model.dto;

import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
//@Builder
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DirectProductEntity {
	@NonNull
	private String dProductNo;
	@NonNull
	private String memberId;
	@NonNull
	private String dProductName;
	@NonNull
	private String dProductContent;
	private LocalDateTime dProductCreatedAt;
	private LocalDateTime dProductUpdatedAt;
	private int dDefaultPrice;
	private int dDeliveryFee;
}
