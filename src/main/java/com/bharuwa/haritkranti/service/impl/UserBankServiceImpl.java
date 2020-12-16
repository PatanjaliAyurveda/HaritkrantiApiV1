package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.payments.VerifyAccountReqBody;
import com.bharuwa.haritkranti.repositories.*;
import com.bharuwa.haritkranti.service.RoleService;
import com.bharuwa.haritkranti.service.UserBankService;
import com.bharuwa.haritkranti.service.UserService;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.bharuwa.haritkranti.utils.Constants.ROLE_AGENT;
import static com.bharuwa.haritkranti.utils.Constants.ROLE_USER;

/**
 * @author anuragdhunna
 */
@Service
public class UserBankServiceImpl implements UserBankService {

    private final MongoTemplate mongoTemplate;

    public UserBankServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FinancialDetailRepo financialDetailRepo;

    @Autowired
    private IncomeSourcesRepo incomeSourcesRepo;

    @Autowired
    private LoanDetailRepo loanDetailRepo;

    @Autowired
    private InsuranceDetailsRepo insuranceDetailsRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    @Override
    public FinancialDetails addFinancialDetails(FinancialDetails financialDetails) {
        Role farmer = roleService.roleName(ROLE_USER);
        Role agent =  roleService.roleName(ROLE_AGENT);
        if(financialDetails.getUser() != null) {
            User user = financialDetails.getUser();
            if (user == null) {
                throw new ResourceNotFoundException("User not found!");
            }
            if (user.getRoles().contains(farmer)) {
                financialDetails.setUserType(FinancialDetails.UserType.Farmer);
            } else if (user.getRoles().size() == 1 && user.getRoles().contains(agent)) {
                financialDetails.setUserType(FinancialDetails.UserType.Agent);
            } else {
                financialDetails.setUserType(FinancialDetails.UserType.Manager);
            }
        }
        if(financialDetails.isPrimaryAccount()){
            // get earlier primary Account
            FinancialDetails primaryAccount = getPrimaryBankDetails(financialDetails.getUserId());
            if (primaryAccount != null){
                primaryAccount.setPrimaryAccount(false);
                mongoTemplate.save(primaryAccount);
            }
        }
        return financialDetailRepo.save(financialDetails);
    }

    @Override
    public List<FinancialDetails> getFinancialDetails(String userId) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("user.$id").is(new ObjectId(userId)));
        return mongoTemplate.find(query, FinancialDetails.class);
    }

    @Override
    public IncomeSources addIncomeSources(IncomeSources incomeSources) {
        return incomeSourcesRepo.save(incomeSources);
    }

    @Override
    public IncomeSources getIncomeSources(String userId) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, IncomeSources.class);
    }

    @Override
    public LoanDetails addLoanDetails(LoanDetails loanDetails) {
        return loanDetailRepo.save(loanDetails);
    }

    @Override
    public List<LoanDetails> getLoanDetails(String userId) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, LoanDetails.class);
    }

    @Override
    public InsuranceDetails addInsuranceDetails(InsuranceDetails insuranceDetails) {
        return insuranceDetailsRepo.save(insuranceDetails);
    }

    @Override
    public List<InsuranceDetails> getInsuranceDetails(String userId) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, InsuranceDetails.class);
    }

    @Override
    public List<String> getUserBankList(String userId) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.findDistinct(query, "bankName", FinancialDetails.class, String.class);
    }

    @Override
    public FinancialDetails getBankDetailsById(String bankId) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(bankId)));
        return mongoTemplate.findOne(query, FinancialDetails.class);
    }

    @Override
    public LoanDetails getLoanDetailsById(String loanId) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(loanId)));
        return mongoTemplate.findOne(query, LoanDetails.class);
    }

    @Override
    public InsuranceDetails getInsuranceDetailsById(String insuranceId) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(insuranceId)));
        return mongoTemplate.findOne(query, InsuranceDetails.class);
    }

    @Override
    public FinancialDetails getPrimaryBankDetails(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId).and("primaryAccount").ne(false).exists(true));
        return mongoTemplate.findOne(query, FinancialDetails.class);
    }

    @Override
    public String setPrimaryBank(String financialDetailId, String userId) {
        // get earlier primary Account
        FinancialDetails oldPrimaryAccount = getPrimaryBankDetails(userId);
        if (oldPrimaryAccount != null){
            oldPrimaryAccount.setPrimaryAccount(false);
            mongoTemplate.save(oldPrimaryAccount);
        }

        // set new account as primary
        FinancialDetails newPrimaryAccount = getBankDetailsById(financialDetailId);
        newPrimaryAccount.setPrimaryAccount(true);
        mongoTemplate.save(newPrimaryAccount);
        return "Success";
    }

    @Override
    public FinancialDetails verifyBankAccount(VerifyAccountReqBody verifyAccountReqBody) {
        FinancialDetails financialDetails = getBankDetailsById(verifyAccountReqBody.getBankId());
        if(financialDetails == null){
            throw new ResourceNotFoundException("Bank Details not found");
        }
        financialDetails.setAccountVerified(true);
        financialDetails.setVerifiedByUserId(verifyAccountReqBody.getVerifiedByUserId());
        return mongoTemplate.save(financialDetails);
    }

    @Override
    public Page<FinancialDetails> getBankAccountsByStatus(Boolean verified, int page, int size) {
        Criteria criteria = new Criteria();
        if(verified != null) {
            if(verified) {
                criteria = criteria.and("accountVerified").ne(false).exists(true);
            } else {
                criteria = criteria.and("accountVerified").ne(true).exists(true);
            }
        }

        criteria = criteria.and("userType").nin(FinancialDetails.UserType.Farmer);

        Query query = new Query(criteria);
        return genericMongoTemplate.paginationWithQuery(page, size, query, FinancialDetails.class);
    }
}