package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.model.Role;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {

    Role findByName(String name);
    Role save(String name);
}

