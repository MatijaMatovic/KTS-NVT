package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserType;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    @EqualsAndHashCode.Include
    private Integer id;
    private String username;
    private UserType type;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String imagePath;

    public UserDTO(User user) {
       this.id = user.getId();
       this.username = user.getUsername();
       this.type = user.getType();
       this.firstName = user.getFirstName();
       this.lastName = user.getLastName();
       this.address = user.getAddress();
       this.phoneNumber = user.getPhoneNumber();
       this.imagePath = user.getImagePath();
    }
}
