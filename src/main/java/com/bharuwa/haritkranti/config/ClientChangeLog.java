package com.bharuwa.haritkranti.config;
//package com.viithiisys.annadata.config;
//
//import com.github.cloudyrock.mongock.ChangeLog;
//import com.github.cloudyrock.mongock.ChangeSet;
//import com.viithiisys.annadata.models.*;
//import City;
//import State;
//import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
//
///**
// * @author anuragdhunna
// */
//@ChangeLog
//public class ClientChangeLog {
//
//    @ChangeSet(order = "001", id = "organiseFamilyMember", author = "anurag")
//    public void organiseFamilyMember(MongoTemplate mongoTemplate) {
//
//        System.out.println("======organiseFamilyMember======");
//        List<Family> families = mongoTemplate.findAll(Family.class);
//        for (Family family : families) {
//            String farmerId = family.getUserId();
//            List<User> familyMembers = family.getFamilyMembers();
//
//            int count = 0;
//            for (User user: familyMembers) {
//                FamilyMember member = new FamilyMember();
//                Address address = new Address();
//
//                String stateId = user.getStateId();
//                System.out.println("==========================="+stateId);
//                State state;
//                Query query;
//                if (!StringUtils.isEmpty(stateId)) {
//                    query = new Query(Criteria.where("_id").is(new ObjectId(user.getStateId())));
//                    state = mongoTemplate.findOne(query, State.class);
//                } else {
//                    query = new Query(Criteria.where("name").is(user.getState()));
//                    state = mongoTemplate.findOne(query, State.class);
//                }
//
//
//                address.setState(state);
//
//                String districtId = user.getDistrictId();
//                City city;
//
//                if (!StringUtils.isEmpty(districtId)) {
//                    query = new Query(Criteria.where("_id").is(new ObjectId(districtId)));
//                    city = mongoTemplate.findOne(query, City.class);
//                } else {
//                    query = new Query(Criteria.where("name").is(user.getDistrict()));
//                    city = mongoTemplate.findOne(query, City.class);
//                }
//                address.setCity(city);
//
//                address.setVillage(user.getVillage());
//                address.setAddress(user.getAddress());
//                member.setAddress(address);
//
//                member.setFirstName(user.getFirstName());
//                member.setLastName(user.getLastName());
//                member.setSameAddress(true);
//
//                member.setFarmerId(farmerId);
//
//                query = new Query(Criteria.where("users.id").is(farmerId));
//                UserAgent userAgent = mongoTemplate.findOne(query, UserAgent.class);
//                if (userAgent != null) {
//                    member.setAgentId(userAgent.getAgentId());
//                }
//                System.out.println("========================================================================"+count++);
//
//                // Check if user exists
//                query = new Query(Criteria.where("agentId").is(userAgent.getAgentId()).and("farmerId").is(farmerId)
//                        .and("firstName").is(user.getFirstName()).and("lastName").is(user.getLastName()));
//                List<FamilyMember> existFamilyMembers = mongoTemplate.find(query, FamilyMember.class);
//                if (existFamilyMembers.isEmpty()) {
//                    mongoTemplate.insert(member);
//                }
//            }
//        }
//    }
//
//}
