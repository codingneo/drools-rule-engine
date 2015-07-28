package com.ps.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ps.dao.DBManager;
import com.ps.dao.TestDBManagerStub;
import com.ps.entity.Merchant;
import com.ps.entity.Transaction;
import com.ps.entity.User;
import com.ps.rule.RuleEngine;
import com.ps.rule.RuleEngineImpl;
import com.ps.util.TimeUtil;
import com.ps.util.TranObjectContainer;
import com.ps.util.TransactionStatus;


@RestController
public class RuleManagerController {
	
	private DBManager dbManager = new TestDBManagerStub();
	
	private RuleEngine ruleEngine = new RuleEngineImpl(); 
    
    @RequestMapping(value = "/transact", method = RequestMethod.POST)
    public ResponseEntity<TransactionStatus> transact(@RequestBody Transaction tran) {
    
    	TranObjectContainer container = createTranContainer(tran);
    	
    	//System.out.println("before rule processing: "+tran);
    	
    	ruleEngine.process(container);
    	
    	//System.out.println("After rule processing: "+tran);
    	
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
    	List<Transaction> histTrans = dbManager.retrieveHistoricTransactions(tran.getUser().getId(), TimeUtil.getCutoffDate(tran.getDate()));
    	container.setOldTransactions(histTrans);
 
    	System.out.println("histTrans size: "+histTrans.size());
    	
    	return container;
    }
}
