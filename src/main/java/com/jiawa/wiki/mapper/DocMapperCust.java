package com.jiawa.wiki.mapper;

import com.jiawa.wiki.domain.Test;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocMapperCust {
    public void increaseViewCount(@Param("id")Long id);
}
