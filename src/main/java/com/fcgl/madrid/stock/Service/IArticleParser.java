package com.fcgl.madrid.stock.Service;

import com.fcgl.madrid.stock.Payload.response.Response;
import com.fcgl.madrid.stock.Service.model.NewArticleResponse;
import com.fcgl.madrid.stock.model.IArticleBody;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IArticleParser {
    public ResponseEntity<Response<List<IArticleBody>>> getAllImportantArticles();
    public NewArticleResponse getNewArticles();
}