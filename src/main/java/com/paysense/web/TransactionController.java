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
import com.paysense.entity.PaySenseTransaction;
import com.paysense.entity.TranObjectContainer;
import com.paysense.entity.Transaction;
import com.paysense.entity.User;
import com.paysense.entity.WhiteListObject;
import com.paysense.rule.RuleEngine;
import com.paysense.util.PSResponse;
import com.paysense.util.TimeUtil;


@RestController
public class TransactionController {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private DBManager dbManager;
	
	@Autowired
	private RuleEngine ruleEngine; 
	
	@Value("${rules.velocity.interval}")
	private int velocityInterval;
	
    @RequestMapping(value = "/transact", method = RequestMethod.POST)
    public ResponseEntity<String> transact(@RequestBody PaySenseTransaction psTran) {
    
    	Transaction tran = psTran.getTransaction();
    	
    	logger.debug("***tran is: "+tran);
    	
    	TranObjectContainer container = createTranContainer(tran);
    	
    	ruleEngine.process(container);
    	
    	dbManager.insertTransaction(tran);
    	
    	PSResponse res = PSResponse.getPSResponseByCode(tran.getStatus());
    	
        return new ResponseEntity<String>(res.toString(), HttpStatus.OK);
    }
    
    private TranObjectContainer createTranContainer(Transaction tran){
    	TranObjectContainer container = new TranObjectContainer();
    	
    	//populate new transaction with latest user/merchant data
    	User user = dbManager.retreiveUser(tran.getUserId());
    	
    	container.setUser(user);
    	
    	container.setNewTransaction(tran);
    	
    	//populate historic transactions before the cutoff time
    	Date date = TimeUtil.parseDate(tran.getDateAsString());
    	//List<Transaction> histTrans = dbManager.retrieveHistoricTransactions(tran, TimeUtil.getCutoffDate(date,velocityInterval));
    	List<Transaction> histTrans = dbManager.retrieveHistoricTransactions(tran);
    	container.setOldTransactions(histTrans);
 
    	logger.debug("histTrans size: "+histTrans.size());
    	
		List<WhiteListObject> whiteList = dbManager.retrieveWhiteList();
		
		container.setWhiteList(whiteList);
    	
    	return container;
    }
}
