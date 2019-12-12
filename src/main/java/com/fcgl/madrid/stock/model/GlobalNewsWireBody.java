package com.fcgl.madrid.stock.model;

import com.fcgl.madrid.stock.model.IArticleBody;

public class GlobalNewsWireBody implements IArticleBody {

    private String articleUrl;
    private String articleHeadline;
    public static final String NAME = "global_news_wire";

    public GlobalNewsWireBody(String articleUrl, String articleHeadline) {
        this.articleHeadline = articleHeadline;
        this.articleUrl = articleUrl;
    }

    @Override
    public String getArticleUrl() {
        return this.articleUrl;
    }

    @Override
    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    @Override
    public String getArticleHeadline() {
        return this.articleHeadline;
    }

    @Override
    public void setArticleHeadline(String articleHeadline) {
        this.articleHeadline = articleHeadline;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String toStringMax(Integer length) {
        String full = this.articleUrl + "\n" + this.articleHeadline;
        return full.substring(0, Math.min(length, full.length()));
    }


}
