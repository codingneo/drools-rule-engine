package com.ps.rule;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.ps.util.TranObjectContainer;
import com.ps.util.TransactionStatus;

public class RuleEngineImpl implements RuleEngine {

	private KieContainer kContainer;
	
	@Override
	public TransactionStatus process(TranObjectContainer container) {

		if(kContainer==null) {
			KieServices ks = KieServices.Factory.get();
			kContainer = ks.getKieClasspathContainer();
			
			//TODO: scan for rule jar files for update from maven repository
			//scan for update every 5 seconds
			//KieScanner scanner = ks.newKieScanner(kContainer);
			//scanner.start(5000L);
		}
		
		KieSession session = this.newRuleSession();
		session.insert(container);
		
		session.fireAllRules();
		
		return container.getNewTransaction().getStatus();
	}

	private KieSession newRuleSession(){
		return kContainer.newKieSession("ksession-rules"); 
	}
}
