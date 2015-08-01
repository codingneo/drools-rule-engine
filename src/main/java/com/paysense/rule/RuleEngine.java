package com.paysense.rule;

import com.paysense.entity.TranObjectContainer;

public interface RuleEngine {
	public int process(TranObjectContainer container);
}