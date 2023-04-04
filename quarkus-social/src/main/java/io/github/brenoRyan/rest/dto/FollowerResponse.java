package io.github.brenoRyan.rest.dto;

import io.github.brenoRyan.domain.model.Follower;
import lombok.Data;

@Data
public class FollowerResponse {

	private Long id;
	private String name;
	
	public FollowerResponse() {
		
	}
	
	public FollowerResponse(Follower follower) {
		this (follower.getId(), follower.getFollower().getName());
	}

	public FollowerResponse(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
}

