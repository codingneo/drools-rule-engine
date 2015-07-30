package com.ps.web;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ps.dao.DBManager;
import com.ps.entity.Merchant;
import com.ps.entity.TranObjectContainer;
import com.ps.entity.Transaction;
import com.ps.entity.TransactionStatus;
import com.ps.entity.User;
import com.ps.rule.RuleEngine;
import com.ps.util.TimeUtil;


@RestController
public class RuleManagerController {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private DBManager dbManager;
	
	@Autowired
	private RuleEngine ruleEngine; 
    
    @RequestMapping(value = "/transact", method = RequestMethod.POST)
    public ResponseEntity<TransactionStatus> transact(@RequestBody Transaction tran) {
    
    	TranObjectContainer container = createTranContainer(tran);
    	
    	ruleEngine.process(container);
    	
    	dbManager.insertTransaction(tran);
    	
        return new ResponseEntity<TransactionStatus>(tran.getStatus(), HttpStatus.OK);
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
    	List<Transaction> histTrans = dbManager.retrieveHistoricTransactions(tran.getUser().getId(), TimeUtil.getCutoffDate(date));
    	container.setOldTransactions(histTrans);
 
    	logger.debug("histTrans size: "+histTrans.size());
    	
    	return container;
    }
}
