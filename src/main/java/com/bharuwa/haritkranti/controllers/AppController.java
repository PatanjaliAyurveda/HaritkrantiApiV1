package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.models.App;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import static com.bharuwa.haritkranti.utils.Constants.APP_NAME;

/**
 * @author anuragdhunna
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AppController {

    private final MongoTemplate mongoTemplate;

    public AppController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    @RequestMapping(value = "/storeApp", method = RequestMethod.POST)
    @ResponseBody
    public App storeApp(@RequestParam String type, @RequestParam int appVersion) {

        App app = genericMongoTemplate.findByKey("name", APP_NAME, App.class);
        if (type.equals("IOS")) {
            app.setIosAppVersion(appVersion);
        } else if (type.equals("ANDROID")) {
            app.setAndroidAppVersion(appVersion);
        }
        return mongoTemplate.save(app);
    }

    @RequestMapping(value = "/getApp", method = RequestMethod.GET)
    @ResponseBody
    public App getApp() {
        return genericMongoTemplate.findByKey("name", APP_NAME, App.class);
    }

}