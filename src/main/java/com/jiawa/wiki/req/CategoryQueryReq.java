package com.jiawa.wiki.req;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;

public class CategoryQueryReq extends PageReq{
    @Override
    public String toString() {
        return "CategoryQueryReq{} " + super.toString();
    }
}