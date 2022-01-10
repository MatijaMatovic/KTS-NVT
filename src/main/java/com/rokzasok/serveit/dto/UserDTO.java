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
    protected Integer id;
    protected String username;
    protected String email;
    protected UserType type;
    protected String firstName;
    protected String lastName;
    protected String address;
    protected String phoneNumber;
    protected String imagePath;

    public UserDTO(User user) {
       this.id = user.getId();
       this.username = user.getUsername();
       this.email = user.getEmail();
       this.type = user.getType();
       this.firstName = user.getFirstName();
       this.lastName = user.getLastName();
       this.address = user.getAddress();
       this.phoneNumber = user.getPhoneNumber();
       this.imagePath = user.getImagePath();
    }
}
