package com.example.AOManager.request;

import com.example.AOManager.common.Function;
import com.example.AOManager.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String id;
    private String avatar;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private long birthday;
    private String gender;
    private String address;
    private String phone;
    private boolean status;
    private String roleId;

    public UserRequest(UsersEntity usersEntity) {
        this.id = usersEntity.getId().toString();
        this.avatar = usersEntity.getAvatar();
        this.email = usersEntity.getEmail();
        this.password = usersEntity.getPassword();
        this.firstName = usersEntity.getFirstName();
        this.lastName = usersEntity.getLastName();
        this.birthday = usersEntity.getBirthday();
        this.gender = usersEntity.getGender();
        this.address = usersEntity.getAddress();
        this.phone = usersEntity.getPhone();
        this.status =usersEntity.getStatus();
    }

    public UsersEntity toEntity() {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setAvatar(this.avatar);
        usersEntity.setFirstName(Function.normalizeName(this.firstName));
        usersEntity.setLastName(Function.normalizeName(this.lastName));
        usersEntity.setBirthday(this.birthday);
        usersEntity.setGender(this.gender);
        usersEntity.setAddress(this.address);
        usersEntity.setPhone(this.phone);
        usersEntity.setStatus(this.isStatus());
        return usersEntity;
    }
}
