package com.fcgl.madrid.stock.Service.model;

import com.fcgl.madrid.stock.Payload.response.InternalStatus;
import com.fcgl.madrid.stock.model.IArticleBody;

import java.util.List;

public class NewArticleResponse {

    private List<IArticleBody> newArticles;
    private InternalStatus internalStatus;

    public NewArticleResponse(List<IArticleBody> newArticles, InternalStatus internalStatus) {
        this.newArticles = newArticles;
        this.internalStatus = internalStatus;
    }

    public List<IArticleBody> getNewArticles() {
        return newArticles;
    }

    public void setNewArticles(List<IArticleBody> newArticles) {
        this.newArticles = newArticles;
    }

    public InternalStatus getInternalStatus() {
        return internalStatus;
    }

    public void setInternalStatus(InternalStatus internalStatus) {
        this.internalStatus = internalStatus;
    }
}
