package com.fcgl.madrid.stock.model;

public interface IArticleBody {


    public String getArticleUrl();

    public void setArticleUrl(String articleUrl);

    public String getArticleHeadline();

    public void setArticleHeadline(String articleHeadline);

    public String getName();

    public String toStringMax(Integer length);

}
