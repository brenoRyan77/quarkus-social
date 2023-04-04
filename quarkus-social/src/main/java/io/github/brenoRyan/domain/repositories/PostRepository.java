package io.github.brenoRyan.domain.repositories;

import javax.enterprise.context.ApplicationScoped;

import io.github.brenoRyan.domain.model.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post>{

}
