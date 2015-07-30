package com.ps.rule;

import com.ps.entity.TranObjectContainer;
import com.ps.entity.TransactionStatus;

public interface RuleEngine {
	public TransactionStatus process(TranObjectContainer container);
}