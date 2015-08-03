package com.paysense.web;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.paysense.dao.DBManager;
import com.paysense.rule.RuleEngine;
import com.paysense.util.PSResponse;
import com.paysense.util.TimeUtil;
import com.paysense.entity.Merchant;
import com.paysense.entity.TranObjectContainer;
import com.paysense.entity.Transaction;
import com.paysense.entity.User;


@RestController
public class RuleManagerController {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private DBManager dbManager;
	
	@Autowired
	private RuleEngine ruleEngine; 
	
	@Value("${rules.velocity.interval}")
	private int velocityInterval;
    
    @RequestMapping(value = "/transact", method = RequestMethod.POST)
    public ResponseEntity<String> transact(@RequestBody Transaction tran) {
    
    	TranObjectContainer container = createTranContainer(tran);
    	
    	ruleEngine.process(container);
    	
    	dbManager.insertTransaction(tran);
    	
    	PSResponse res = PSResponse.getPSResponseByCode(tran.getStatus());
    	
        return new ResponseEntity<String>(res.toString(), HttpStatus.OK);
    }
    
    private TranObjectContainer createTranContainer(Transaction tran){
    	TranObjectContainer container = new TranObjectContainer();
    	
    	//populate new transaction with latest user/merchant data
    	User user = dbManager.retreiveUser(tran.getUser().getId());
    	tran.setUser(user);
    	
    	Merchant merchant = dbManager.retreiveMerchant(tran.getMerchant().getId());
    	tran.setMerchant(merchant);
    	
    	container.setNewTransaction(tran);
    	
    	//populate historic transactions before the cutoff time
    	Date date = TimeUtil.parseDate(tran.getDateAsString());
    	List<Transaction> histTrans = dbManager.retrieveHistoricTransactions(tran.getUser().getId(), TimeUtil.getCutoffDate(date,velocityInterval));
    	container.setOldTransactions(histTrans);
 
    	logger.debug("histTrans size: "+histTrans.size());
    	
    	return container;
    }
}
