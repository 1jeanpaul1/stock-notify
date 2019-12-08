package com.fcgl.madrid.stock.Service;

import com.fcgl.madrid.stock.Payload.response.InternalStatus;
import com.fcgl.madrid.stock.Payload.response.Response;
import com.fcgl.madrid.stock.Service.model.NewArticleResponse;
import com.fcgl.madrid.stock.model.BusinessWireBody;
import com.fcgl.madrid.stock.model.IArticleBody;
import com.fcgl.madrid.stock.repository.ArticlesRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessWire implements IArticleParser {
    private List<String> keyWords;

    private Log logger;
    private static final Integer RETRY = 3;
    private ArticlesRepository articlesRepository;
    @Autowired
    public BusinessWire(ArticlesRepository articlesRepository) {
        this.logger = LogFactory.getLog(BusinessWire.class);
        this.articlesRepository = articlesRepository;
        this.keyWords = new ArrayList<>();
        keyWords.add("phase");
        keyWords.add("guidance");
        keyWords.add("fda");
    }


    public ResponseEntity<Response<List<IArticleBody>>> getAllImportantArticles() {
        NewArticleResponse newArticleResponse = getNewArticles();
        if (newArticleResponse.getInternalStatus().getHttpCode().equals(HttpStatus.NOT_FOUND)) {
            Response response = new Response<List<IArticleBody>>(InternalStatus.NOT_FOUND, null);
            return new ResponseEntity<Response<List<IArticleBody>>>(response, HttpStatus.NOT_FOUND);
        }
        List<IArticleBody> businessWireBodyList = newArticleResponse.getNewArticles();
        List<IArticleBody> importantList = new ArrayList<>();
        int count = 0;
        for (IArticleBody businessWireBody : businessWireBodyList) {
            for (String keyWord : this.keyWords) {
                if (businessWireBody.getArticleHeadline().contains(keyWord)) {
                    importantList.add(businessWireBody);
                }
            }
            count++;
        }
        System.out.println("NUMBER OF NEW ARTICLES PARSED: " + count);
        //Puts the first item in the list into the cache
        if (businessWireBodyList.size() > 0) {
            articlesRepository.put(businessWireBodyList.get(0));
        }
        Response response = new Response<>(InternalStatus.OK, importantList);
        return new ResponseEntity<Response<List<IArticleBody>>>(response, HttpStatus.OK);
    }

    public NewArticleResponse getNewArticles() {
        String lastArticle = articlesRepository.getLastArticle(BusinessWireBody.NAME);
        System.out.println("LAST ARTICLE: " + lastArticle);
        List<IArticleBody> businessWireBodyList = new ArrayList<>();
        Integer retryCount = 0;
        while (retryCount < RETRY) {
            try {
                Document doc = Jsoup.connect("https://www.businesswire.com/portal/site/home/news/industry/?vnsId=31250").get();
                Elements listOfArticles = doc.select(".bwNewsList").first().select("li");
                for (Element el : listOfArticles) {
                    String url = el.select("div").first().attr("abs:itemid");
                    String headline = el.select("span[itemprop$=headline]").text().toLowerCase();
                    //We've already seen this article so no need on going on
                    if (url.equals(lastArticle)) {
                        return new NewArticleResponse(businessWireBodyList, InternalStatus.OK);
                    }
                    businessWireBodyList.add(new BusinessWireBody(url, headline)
                    );
                }
                return new NewArticleResponse(businessWireBodyList, InternalStatus.OK);
            } catch (IOException e) {
                retryCount++;
            }
        }
        return new NewArticleResponse(businessWireBodyList, InternalStatus.NOT_FOUND);
    }
}
