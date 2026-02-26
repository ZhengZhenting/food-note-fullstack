package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User Entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * user ID
     */
    private Integer id;

    /**
     * user account
     */
    private String userAccount;

    /**
     * user name
     */
    private String userName;

    /**
     * user password
     */
    private String userPwd;

    /**
     * user avatar
     */
    private String userAvatar;

    /**
     * user email
     */
    private String userEmail;

    /**
     * user role
     */
    private Integer userRole;

    /**
     * is logged in(0:yes；1：no)
     */
    private Boolean isLogin;

    /**
     * allowed for comments(0:yes；1：no)
     */
    private Boolean isWord;

    /**
     * create time
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
