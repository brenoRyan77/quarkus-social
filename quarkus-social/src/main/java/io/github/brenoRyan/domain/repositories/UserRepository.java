package io.github.brenoRyan.domain.repositories;

import javax.enterprise.context.ApplicationScoped;

import io.github.brenoRyan.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User>{

}
