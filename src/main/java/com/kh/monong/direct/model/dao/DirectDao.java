package com.kh.monong.direct.model.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.kh.monong.direct.model.dto.DirectProduct;

public interface DirectDao {

	List<DirectProduct> selectProductList(RowBounds rowBounds);

}
