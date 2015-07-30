package com.ps.rule;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

import com.ps.entity.TranObjectContainer;
import com.ps.entity.TransactionStatus;

@Component
public class RuleEngineImpl implements RuleEngine {

	private static KieContainer kContainer;
	
	private ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(5);
	
	@Override
	public TransactionStatus process(TranObjectContainer container) {

		if(kContainer==null) {
			
			KieServices kieServices = KieServices.Factory.get();
			ReleaseId releaseId = kieServices.newReleaseId( "com.paysense", "rules", "LATEST" );
			kContainer = kieServices.newKieContainer( releaseId );
			
			KieScanner kScanner = kieServices.newKieScanner( kContainer );
			// Start the KieScanner polling the Maven repository every 10 seconds
			kScanner.start( 10000L );
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
