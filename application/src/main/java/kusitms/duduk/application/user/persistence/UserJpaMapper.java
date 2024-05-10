package kusitms.duduk.application.user.persistence;

import java.time.LocalDateTime;
import kusitms.duduk.application.user.persistence.entity.UserJpaEntity;
import kusitms.duduk.common.annotation.Mapper;
import kusitms.duduk.domain.global.Id;
import kusitms.duduk.domain.user.User;
import kusitms.duduk.domain.user.vo.Email;
import kusitms.duduk.domain.user.vo.Nickname;
import kusitms.duduk.domain.user.vo.RefreshToken;

@Mapper
public class UserJpaMapper {

    public UserJpaEntity toJpaEntity(User user) {
        return UserJpaEntity.builder()
            // 새로 생성하는 경우에는 ID가 없을 수 있음
            .id(user.getId() != null ? user.getId().getValue() : null)
            .email(user.getEmail().getValue())
            .nickname(user.getNickname().getValue())
            .refreshToken(user.getRefreshToken().getValue())
            .gender(user.getGender())
            .birthday(user.getBirthday())
            .role(user.getRole())
            .provider(user.getProvider())
            .category(user.getCategory())
            .goal(user.getGoal())
            .build();
    }

    /**
     * 기존에 있는 값은 유지한 채 변경되는 값만 업데이트 합니다.
     */
    public UserJpaEntity toJpaEntity(User user, UserJpaEntity persistedUser) {
        return persistedUser.toBuilder()
            .nickname(user.getNickname().getValue())
            .refreshToken(user.getRefreshToken().getValue())
            .birthday(user.getBirthday())
            .build();
    }

    public User toDomain(UserJpaEntity userJpaEntity) {
        return User.builder()
            .id(Id.of(userJpaEntity.getId()))
            .email(Email.of(userJpaEntity.getEmail()))
            .nickname(Nickname.of(userJpaEntity.getNickname()))
            .refreshToken(RefreshToken.of(userJpaEntity.getRefreshToken()))
            .gender(userJpaEntity.getGender())
            .birthday(userJpaEntity.getBirthday())
            .role(userJpaEntity.getRole())
            .provider(userJpaEntity.getProvider())
            .category(userJpaEntity.getCategory())
            .goal(userJpaEntity.getGoal())
            .build();
    }
}