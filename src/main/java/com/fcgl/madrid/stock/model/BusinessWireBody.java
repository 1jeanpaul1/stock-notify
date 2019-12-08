package com.fcgl.madrid.stock.model;

import com.fcgl.madrid.stock.Service.BusinessWire;

public class BusinessWireBody implements IArticleBody {

    private String articleUrl;
    private String articleHeadline;
    public static final String NAME = "business_wire";

    public BusinessWireBody(String articleUrl, String articleHeadline) {
        this.articleHeadline = articleHeadline;
        this.articleUrl = articleUrl;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getArticleHeadline() {
        return articleHeadline;
    }

    public void setArticleHeadline(String articleHeadline) {
        this.articleHeadline = articleHeadline;
    }

    public String getName() {
        return NAME;
    }
}
