package com.fcgl.madrid.stock.Service;

import com.fcgl.madrid.stock.Payload.response.InternalStatus;
import com.fcgl.madrid.stock.Payload.response.Response;
import com.fcgl.madrid.stock.Service.model.NewArticleResponse;
import com.fcgl.madrid.stock.model.BusinessWireBody;
import com.fcgl.madrid.stock.model.GlobalNewsWireBody;
import com.fcgl.madrid.stock.model.IArticleBody;
import com.fcgl.madrid.stock.repository.ArticlesRepository;
import net.swisstech.bitly.BitlyClient;
import net.swisstech.bitly.model.v3.ShortenResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GlobalNewsWire implements IArticleParser {
    private List<String> keyWords;

    private Log logger;
    private static final Integer RETRY = 3;
    private ArticlesRepository articlesRepository;
    private static final String DOMAIN_NAME = "https://www.globenewswire.com";
    private static final Integer NUMBER_OF_PAGES = 5;

    @Autowired
    public GlobalNewsWire(ArticlesRepository articlesRepository) {
        this.logger = LogFactory.getLog(BusinessWire.class);
        this.articlesRepository = articlesRepository;
        this.keyWords = new ArrayList<>();
        keyWords.add("phase");
        keyWords.add("guidance");
        keyWords.add("fda");
        keyWords.add("outperform");
        keyWords.add("clinical");
        keyWords.add("report");
    }


    public ResponseEntity<Response<List<IArticleBody>>> getAllImportantArticles() {
        NewArticleResponse newArticleResponse = getNewArticles();
        List<IArticleBody> importantList = new ArrayList<>();
        if (newArticleResponse.getInternalStatus().getHttpCode().equals(HttpStatus.NOT_FOUND)) {
            Response response = new Response<List<IArticleBody>>(InternalStatus.NOT_FOUND, importantList);
            return new ResponseEntity<Response<List<IArticleBody>>>(response, HttpStatus.NOT_FOUND);
        }
        List<IArticleBody> globalNewsWireBodyList = newArticleResponse.getNewArticles();
        int count = 0;
        for (IArticleBody globalNewsWireBody : globalNewsWireBodyList) {
            for (String keyWord : this.keyWords) {
                if (globalNewsWireBody.getArticleHeadline().contains(keyWord)) {
                    IArticleBody temp = new GlobalNewsWireBody(
                            shortenUrl(globalNewsWireBody.getArticleUrl()),
                            globalNewsWireBody.getArticleHeadline()
                    );
                    importantList.add(temp);
                    break;
                }
            }
            count++;
        }
        System.out.println("NUMBER OF NEW ARTICLES PARSED: " + count);
        //Puts the first item in the list into the cache
        if (globalNewsWireBodyList.size() > 0) {
            articlesRepository.put(globalNewsWireBodyList.get(0));
        }
        Response response = new Response<>(InternalStatus.OK, importantList);
        return new ResponseEntity<Response<List<IArticleBody>>>(response, HttpStatus.OK);
    }


    /**
     * GlobalNewsWire requires some more intense parsing to search by category... So will be doing a generic search
     * Will go through 5 pages to make up for the extra data that we will be receiving... If 5 pages is ever not enough
     * a log will be made
     * @return List<GlobalNewsWireBody>
     */
    public NewArticleResponse getNewArticles() {
        String lastArticle = articlesRepository.getLastArticle(GlobalNewsWireBody.NAME);
        System.out.println("LAST ARTICLE: " + lastArticle);
        List<IArticleBody> globalNewsWireBodyList = new ArrayList<>();
        Integer retryCount = 0;
        Integer pageCount = 1;
        while (retryCount < RETRY) {
            try {
                while (pageCount <= NUMBER_OF_PAGES) {
                    Document doc = Jsoup.connect("https://www.globenewswire.com/en/Index?page=" + pageCount).get();
                    Elements listOfArticles = doc.select(".results-link");
                    for (Element el : listOfArticles) {
                        String url = DOMAIN_NAME + el.select("a").first().attr("href");
                        String headline = el.select("a").first().text().toLowerCase();
                        if (url.equals(lastArticle)) {
                            return new NewArticleResponse(globalNewsWireBodyList, InternalStatus.OK);
                        }
                        globalNewsWireBodyList.add(new GlobalNewsWireBody(url, headline));
                    }
                    pageCount++;
                }

                return new NewArticleResponse(globalNewsWireBodyList, InternalStatus.OK);
            } catch (IOException e) {
                System.out.println("ERROR: " + e.getMessage());
                retryCount++;
            }
        }
        return new NewArticleResponse(globalNewsWireBodyList, InternalStatus.NOT_FOUND);
    }

    private String shortenUrl(String url) {
        BitlyClient client = new BitlyClient("");
        net.swisstech.bitly.model.Response<ShortenResponse> resp = client.shorten()
                .setLongUrl(url)
                .call();
        ShortenResponse shortUrl = resp.data;
        if (shortUrl == null) {
            System.out.println("******ERROR WITH BITLY*******");
            System.out.println("status code: " + resp.status_code);
            System.out.println("status text: " + resp.status_txt);
            return DOMAIN_NAME;
        } else {
            return shortUrl.url;
        }
    }
}

