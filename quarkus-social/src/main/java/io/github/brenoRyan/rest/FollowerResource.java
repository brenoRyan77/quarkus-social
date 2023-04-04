package io.github.brenoRyan.rest;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.github.brenoRyan.domain.model.Follower;
import io.github.brenoRyan.domain.model.User;
import io.github.brenoRyan.domain.repositories.FollowerRepository;
import io.github.brenoRyan.domain.repositories.UserRepository;
import io.github.brenoRyan.rest.dto.FollowerRequest;
import io.github.brenoRyan.rest.dto.FollowerResponse;
import io.github.brenoRyan.rest.dto.FollowersUserResponse;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

	private FollowerRepository followerRepository;
	private UserRepository userRepository;
	
	@Inject
	public FollowerResource(FollowerRepository followerRepository,
			UserRepository userRepository) {
		
		this.followerRepository = followerRepository;
		this.userRepository = userRepository;
	}
	
	@PUT
	@Transactional
	public Response followUser(@PathParam("userId") Long userId, FollowerRequest followerRequest) {
		
		if(userId.equals(followerRequest.getFollowerId())) {
			return Response.status(Response.Status.CONFLICT)
					.entity("You can't follow yourself").build();
		}
		
		User user = userRepository.findById(userId);
		
		if(user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		User follower = userRepository.findById(followerRequest.getFollowerId());
		boolean follow = followerRepository.follows(follower, user);
		
		if (!follow) {
			
			Follower entity = new Follower();
			entity.setUser(user);
			entity.setFollower(follower);
			followerRepository.persist(entity);
			
		}
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@GET
	public Response listFollowers(@PathParam("userId") Long userId) {
		
		User user = userRepository.findById(userId);
		
		if(user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		var list = followerRepository.findByUser(userId);
		FollowersUserResponse responseObject = new FollowersUserResponse();
		
		responseObject.setFollowersCount(list.size());
		
		var followerList = list.stream()
					.map(FollowerResponse::new)
					.collect(Collectors.toList());
		
		responseObject.setContent(followerList);
		return Response.ok(responseObject).build();
		
	}
	
	@DELETE
	@Transactional
	public Response unfollowUser(@PathParam("userId") Long userId,
			@QueryParam("followerId") Long followerId) {
		
		User user = userRepository.findById(userId);
		
		if(user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		followerRepository.deleteByFollowerAndUser(followerId, userId);
		
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
}
