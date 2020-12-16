package com.bharuwa.haritkranti.config;
//package com.viithiisys.annadata.config;
//
//import com.github.cloudyrock.mongock.Mongock;
//import com.github.cloudyrock.mongock.SpringMongockBuilder;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
///**
// * @author anuragdhunna
// */
//@Component
//public class MongockConfig {
//
//    private static final String MONGO_URI = "mongodb://127.0.0.1:27017";
//    private static final String SPRING_BOOT_MONGO_DB = "annadata";
//    private static final String SPRING_MONGO_DB = "mongock_db_spring_5_x_x";
//
//    public MongockConfig() {}
//
//
////    @Bean("mongock-spring")
////    public Mongock mongock() {
////        System.out.println("--------1-----------------------"+ ClientChangeLog.class.getPackage().getName());
////        MongoClient mongoclient = new MongoClient(new MongoClientURI(MONGO_URI));
////        return new SpringMongockBuilder(mongoclient, SPRING_MONGO_DB, ClientChangeLog.class.getPackage().getName())
////                .setLockQuickConfig()
////                .build();
////    }
//
//    @Bean("mongock-spring-boot")
//    public Mongock mongockSpringBoot() {
//        System.out.println("--------2-----------------------"+ ClientChangeLog.class.getPackage().getName());
//        MongoClient mongoclient = new MongoClient(new MongoClientURI(MONGO_URI));
//        return new SpringMongockBuilder(mongoclient, SPRING_BOOT_MONGO_DB, ClientChangeLog.class.getPackage().getName())
//                .setLockQuickConfig()
//                .build();
//    }
//}
