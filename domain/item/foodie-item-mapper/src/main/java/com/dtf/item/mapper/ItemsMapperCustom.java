package com.dtf.item.mapper;

import com.dtf.item.pojo.vo.ItemCommentVO;
import com.dtf.item.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {

    public List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);

    // TODO 迁移到foodie-search模块
//    public List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String, Object> map);
//
//    public List<SearchItemsVO> searchItemsByThirdCat(@Param("paramsMap") Map<String, Object> map);

    public List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List specIdsList);

    public int decreaseItemSpecStock(@Param("specId") String specId,
                                     @Param("pendingCounts") int pendingCounts);
}