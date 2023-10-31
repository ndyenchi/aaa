package com.example.AOManager.dto.manager;

import com.example.AOManager.common.Function;
import com.example.AOManager.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {

    private String id;
    private String avatar;
    private String email;
    private String firstName;
    private String lastName;
    private long birthday;
    private String gender;
    private String address;
    private String phone;
    private boolean status;
    private long createAt;
    private long updateAt;

    public UsersDto(UsersEntity usersEntity) {
        this.id = usersEntity.getId().toString();
        this.avatar = usersEntity.getAvatar();
        this.email = usersEntity.getEmail();
        this.firstName = usersEntity.getFirstName();
        this.lastName = usersEntity.getLastName();
        this.birthday = usersEntity.getBirthday();
        this.gender = usersEntity.getGender();
        this.address = usersEntity.getAddress();
        this.phone = usersEntity.getPhone();
        this.status = usersEntity.getStatus();
        this.createAt = null != usersEntity.getCreatedAt() ? Function.toLongFromTimeStamp(usersEntity.getCreatedAt()) : 0;
        this.updateAt = null != usersEntity.getUpdatedAt() ? Function.toLongFromTimeStamp(usersEntity.getUpdatedAt()) : 0;
    }
}
