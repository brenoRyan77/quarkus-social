package io.github.brenoRyan.domain.repositories;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import io.github.brenoRyan.domain.model.Follower;
import io.github.brenoRyan.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower>{

	public boolean follows(User follower, User user) {
		
		var params = Parameters.with("follower", follower)
				.and("user", user);
		
		PanacheQuery<Follower> query = find("follower =:follower and user =:user", params);
		Optional<Follower> result = query.firstResultOptional();
		
		return result.isPresent();
	}
	
	public List<Follower> findByUser(Long userId){
		
		PanacheQuery<Follower> query = find("user.id", userId);
		return query.list();
	}
	
	public void deleteByFollowerAndUser(Long followerId, Long userId) {
		
		var params = Parameters
				.with("userId", userId)
				.and("followerId", followerId)
				.map();	
		
		delete("follower.id =:followerId and user.id =:userId", params);
	}
}
