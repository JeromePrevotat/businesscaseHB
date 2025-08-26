// package com.humanbooster.businesscase.mapper;

// import java.util.ArrayList;

// import org.springframework.stereotype.Component;

// import com.humanbooster.businesscase.dto.RoleDTO;
// import com.humanbooster.businesscase.model.Role;

// @Component
// public class RoleMapper {public RoleDTO toDTO(Role role) {
//         if (role == null) return null;
//         return new RoleDTO(
//             role.getId(),
//             role.getName(),
//             role.getUserList() != null ? role.getUserList()
//                                                 .stream()
//                                                 .map(user -> user.getId())
//                                                 .toList()
//                                         : null
//         );
//     }    public Role toEntity(RoleDTO roleDTO) {
//         if (roleDTO == null) return null;
//         Role role = new Role();
//         role.setId(roleDTO.getId());
//         role.setName(roleDTO.getName());
//         // Pour éviter les dépendances circulaires dans les tests,
//         // on initialise une liste vide. La liste des utilisateurs 
//         // sera gérée séparément si nécessaire.
//         role.setUserList(new ArrayList<>());
//         return role;
//     }
// }
