package com.ps.rule;

import com.ps.util.TranObjectContainer;
import com.ps.util.TransactionStatus;

public interface RuleEngine {
	public TransactionStatus process(TranObjectContainer container);
}