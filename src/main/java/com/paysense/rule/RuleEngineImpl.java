package com.paysense.rule;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.paysense.entity.TranObjectContainer;

@Component
public class RuleEngineImpl implements RuleEngine {

	private static KieContainer kContainer;
	
	@Value("${rules.groupId}")
	private String rulesGroupId;

	@Value("${rules.artifactId}")
	private String rulesArtifactId;

	@Value("${rules.version}")
	private String rulesVersion;	

	@Value("${kiescanner.interval}")
	private int kiescannerInterval;	
	
	@Override
	public int process(TranObjectContainer container) {

		if(kContainer==null) {
			
			KieServices kieServices = KieServices.Factory.get();
			ReleaseId releaseId = kieServices.newReleaseId(rulesGroupId, rulesArtifactId, rulesVersion);
			kContainer = kieServices.newKieContainer( releaseId );
			
			KieScanner kScanner = kieServices.newKieScanner( kContainer );
			// Start the KieScanner polling the Maven repository every 10 seconds
			kScanner.start( kiescannerInterval * 1000 );
		}
		
		KieSession session = kContainer.newKieSession();
		session.insert(container);
		
		session.fireAllRules();
		
		return container.getNewTransaction().getStatus();
	}
}
