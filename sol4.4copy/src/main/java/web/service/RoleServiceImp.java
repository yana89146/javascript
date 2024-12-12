package web.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.Role;
import web.model.User;
import web.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImp implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByName(String name){
        Role role = null;
        for(Role r:roleRepository.findAll()){
            if(r.getName().equals(name)){
                role=r;
            }
        }
        return role;
    }

    @Override
    public Role save(String name){
        Role r = new Role();
        r.setName(name);
        roleRepository.save(r);
        return r;
    }


}
