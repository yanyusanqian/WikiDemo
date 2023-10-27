package com.jiawa.wiki.resp;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;

public class DocQueryResp {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long ebook_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parent;

    private String name;

    private Integer sort;

    private Integer view_count;

    private Integer vote_count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEbook_id() {
        return ebook_id;
    }

    public void setEbook_id(Long ebook_id) {
        this.ebook_id = ebook_id;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getView_count() {
        return view_count;
    }

    public void setView_count(Integer view_count) {
        this.view_count = view_count;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    @Override
    public String toString() {
        return "DocQueryResp{" +
                "id=" + id +
                ", ebook_id=" + ebook_id +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", view_count=" + view_count +
                ", vote_count=" + vote_count +
                '}';
    }
}