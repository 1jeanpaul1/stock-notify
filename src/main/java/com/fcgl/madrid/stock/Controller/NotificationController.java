package com.fcgl.madrid.stock.Controller;

import com.fcgl.madrid.stock.Payload.response.Response;
import com.fcgl.madrid.stock.Service.BusinessWire;
import com.fcgl.madrid.stock.Service.GlobalNewsWire;
import com.fcgl.madrid.stock.model.IArticleBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/stock/notify/v1")
public class NotificationController {

        private BusinessWire businessWire;
        private GlobalNewsWire globalNewsWire;

        @Autowired
        public void setNotificationController(BusinessWire businessWire, GlobalNewsWire globalNewsWire) {
            this.businessWire = businessWire;
            this.globalNewsWire = globalNewsWire;
        }

        @GetMapping(path = "/bw")
        public ResponseEntity<Response<List<IArticleBody>>> getPostComments() {
            return businessWire.getAllImportantArticles();
        }

        @GetMapping(path = "/gnw")
        public ResponseEntity<Response<List<IArticleBody>>> getGNWPostComments() {
            return globalNewsWire.getAllImportantArticles();
        }
}
