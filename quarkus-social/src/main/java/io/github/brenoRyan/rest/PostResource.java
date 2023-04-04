package io.github.brenoRyan.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.github.brenoRyan.domain.model.Post;
import io.github.brenoRyan.domain.model.User;
import io.github.brenoRyan.domain.repositories.FollowerRepository;
import io.github.brenoRyan.domain.repositories.PostRepository;
import io.github.brenoRyan.domain.repositories.UserRepository;
import io.github.brenoRyan.rest.dto.PostRequest;
import io.github.brenoRyan.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {
	
	private UserRepository userRepository;
	private PostRepository postRepository;
	private FollowerRepository followerRepository;
	
	@Inject
	public PostResource(UserRepository userRepository,
			PostRepository postRepository,
			FollowerRepository followerRepository) {
		
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.followerRepository = followerRepository;
	}
	
	@POST
	@Transactional
	public Response savePost(@PathParam("userId") Long userId,
			PostRequest postRequest) {
		
		User user = userRepository.findById(userId);
		
		if(user ==null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		Post post = new Post();
		post.setText(postRequest.getText());
		post.setUser(user);
		
		postRepository.persist(post);
		
		return Response.status(Response.Status.CREATED).build();
	}
	
	@GET
	public Response listPost(@PathParam("userId") Long userId,
			@HeaderParam("followerId") Long followerId) {
		
		User user = userRepository.findById(userId);
		
		if(user ==null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		if(followerId == null) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Your forgor the header followerId")
					.build();
		}
		
		User follower = userRepository.findById(followerId);
		
		if(follower == null) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Inexistent followerId")
					.build();

		}
		
		boolean follows = followerRepository.follows(follower, user);
		
		if(!follows) {
			return Response
					.status(Response.Status.FORBIDDEN)
					.entity("Your can't see these posts")
					.build();
		}
		
		PanacheQuery<Post> query =  postRepository.find("user",
				Sort.by("dateTime", Sort.Direction.Descending) , user);
		
		List<Post> list = query.list();
		
		List<PostResponse> postResponse = list.stream()
					.map(PostResponse::fromEntity)
					.collect(Collectors.toList());
		
		return Response.ok(postResponse).build();
	}
}
