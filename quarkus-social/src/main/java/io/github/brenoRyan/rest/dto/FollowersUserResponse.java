package io.github.brenoRyan.rest.dto;

import java.util.List;

import lombok.Data;

@Data
public class FollowersUserResponse {

	private Integer followersCount;
	private List<FollowerResponse> content;
}
