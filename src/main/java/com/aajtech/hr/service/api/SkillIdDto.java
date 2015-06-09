package com.aajtech.hr.service.api;

import com.google.common.base.Function;

public class SkillIdDto {
	static final Function<SkillIdDto, String> TO_NAME = new Function<SkillIdDto, String>() {
		@Override
		public String apply(SkillIdDto skill) {
			return skill.getSkill().getName();
		}
	};

	private int id;
	private SkillDto skill;

	public int getId() {
		return id;
	}

	public SkillDto getSkill() {
		return skill;
	}
}
