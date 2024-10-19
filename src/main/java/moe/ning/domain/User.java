package moe.ning.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;
    private String password;
    private String service;
    private QueryString queryString;


    public User(String userId, String password, String service) {
        this.userId = userId;
        this.password = password;
        this.service = service;
    }
}
