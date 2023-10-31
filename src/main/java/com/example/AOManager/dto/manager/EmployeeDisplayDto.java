package com.example.AOManager.dto.manager;

import com.example.AOManager.common.Function;
import com.example.AOManager.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDisplayDto {

    private String id;
    private String avatar;
    private String name;
    private String email;
    private String address;
    private String phone;
    private long createAt;
    private long updateAt;

    public EmployeeDisplayDto(UsersEntity usersEntity) {
        this.setId(usersEntity.getId().toString());
        this.setAvatar(usersEntity.getAvatar());
        this.setName(usersEntity.getLastName() + " " + usersEntity.getFirstName());
        this.setEmail(usersEntity.getEmail());
        this.setAddress(usersEntity.getAddress());
        this.setPhone(usersEntity.getPhone());
        this.setCreateAt(null != usersEntity.getCreatedAt() ? Function.toLongFromTimeStamp(usersEntity.getCreatedAt()) : 0);
        this.setUpdateAt(null != usersEntity.getUpdatedAt() ? Function.toLongFromTimeStamp(usersEntity.getUpdatedAt()) : 0);
    }
}
