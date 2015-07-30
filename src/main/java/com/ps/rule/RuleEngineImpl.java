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
			
			KieServices ks = KieServices.Factory.get();
			kContainer = ks.getKieClasspathContainer();
						
			sch.scheduleAtFixedRate(new ReloadRules(), 0, 10, TimeUnit.SECONDS);
			
			/*
			KieServices kieServices = KieServices.Factory.get();
			ReleaseId releaseId = kieServices.newReleaseId( "com.paysense", "rules", "1.0.1" );
			kContainer = kieServices.newKieContainer( releaseId );
			
			KieScanner kScanner = kieServices.newKieScanner( kContainer );
			// Start the KieScanner polling the Maven repository every 10 seconds
			kScanner.start( 5000L );
			*/
		}
		
		KieSession session = this.newRuleSession();
		session.insert(container);
		
		session.fireAllRules();
		
		return container.getNewTransaction().getStatus();
	}

	private KieSession newRuleSession(){
		return kContainer.newKieSession("ksession-rules"); 
	}
	
	private static class ReloadRules implements Runnable {

		@Override
		public void run() {
			System.out.println("************Reloading Rules1****************");
			
			KieServices ks = KieServices.Factory.get();
			KieRepository kr = ks.getRepository();
	
			File file = new File("/Users/yanhchen/.m2/repository/com/paysense/rules/1.0.1/rules-1.0.1.jar");
			KieModule kModule = kr.addKieModule(ks.getResources().newFileSystemResource(file));
			
			kContainer = ks.newKieContainer(kModule.getReleaseId());
		}
	
	}
}
