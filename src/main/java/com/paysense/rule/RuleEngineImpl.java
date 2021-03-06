package com.paysense.rule;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.drools.core.event.DefaultAgendaEventListener;
import org.drools.core.io.impl.UrlResource;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.paysense.dao.DBManager;
import com.paysense.entity.TranObjectContainer;

@Component
public class RuleEngineImpl implements RuleEngine {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Value("${rules.url}")
	private String rulesUrl;

	@Value("${rules.user}")
	private String rulesUser;

	@Value("${rules.password}")
	private String rulesPassword;

	@Value("${kiescanner.interval}")
	private int kiescannerInterval;
	
	@Autowired
	private DBManager dbManager;

	private KieContainer kContainer;

	private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);

	@Override
	public int process(TranObjectContainer container) {

		if(kContainer == null) {
			kContainer = initKieContainer();
			scheduledThreadPool.scheduleAtFixedRate(new InitKieContainerWorker(), kiescannerInterval, kiescannerInterval, TimeUnit.SECONDS);
		}
		
		KieSession session = kContainer.newKieSession();
		
		session.insert(container);

		session.fireAllRules();

		session.dispose();

		return container.getNewTransaction().getStatus();
	}

	private synchronized KieContainer initKieContainer() {

		logger.info("Inside RuleEngineImpl initKieContainer");

		try {

			KieServices ks = KieServices.Factory.get();
			KieRepository kr = ks.getRepository();
			UrlResource urlResource = (UrlResource) ks.getResources().newUrlResource(rulesUrl);
			urlResource.setUsername(rulesUser);
			urlResource.setPassword(rulesPassword);
			urlResource.setBasicAuthentication("enabled");
			InputStream is = urlResource.getInputStream();
			KieModule kModule = kr.addKieModule(ks.getResources().newInputStreamResource(is));

			return ks.newKieContainer(kModule.getReleaseId());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private class InitKieContainerWorker implements Runnable {

		@Override
		public void run() {
			kContainer = initKieContainer();
		}
	}
}
