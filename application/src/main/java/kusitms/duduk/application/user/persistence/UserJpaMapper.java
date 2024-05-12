package kusitms.duduk.application.user.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kusitms.duduk.application.archive.persistence.ArchiveJpaMapper;
import kusitms.duduk.application.archive.persistence.entity.ArchiveJpaEntity;
import kusitms.duduk.application.user.persistence.entity.UserJpaEntity;
import kusitms.duduk.common.annotation.Mapper;
import kusitms.duduk.domain.archive.Archive;
import kusitms.duduk.domain.global.Category;
import kusitms.duduk.domain.global.Id;
import kusitms.duduk.domain.user.User;
import kusitms.duduk.domain.user.vo.Email;
import kusitms.duduk.domain.user.vo.Nickname;
import kusitms.duduk.domain.user.vo.RefreshToken;
import lombok.RequiredArgsConstructor;

// todo : 추후에 팩토리 패턴으로 변경 가능성 존재 (UserJpaEntityFactory#create)
@RequiredArgsConstructor
@Mapper
public class UserJpaMapper {

    private final ArchiveJpaMapper archiveJpaMapper;

    public UserJpaEntity toJpaEntity(User user) {
        // 빌더 패턴을 사용하여 UserJpaEntity 인스턴스 생성
        UserJpaEntity userEntity = UserJpaEntity.builder()
            .id(user.getId() != null ? user.getId().getValue() : null)
            .email(user.getEmail().getValue())
            .nickname(user.getNickname().getValue())
            .refreshToken(user.getRefreshToken().getValue())
            .gender(user.getGender())
            .birthday(user.getBirthday())
            .role(user.getRole())
            .provider(user.getProvider())
            .category(user.getCategory())
            .archives(user.getArchives() == null ? new ArrayList<>() : getArchiveJpaEntities(user))
            .goal(user.getGoal())
            .build();

        addDefaultArchives(userEntity);
        return userEntity;
    }

    private void addDefaultArchives(UserJpaEntity userEntity) {
        Arrays.stream(Category.values())
            .forEach(category ->
	userEntity.getArchives().add(ArchiveJpaEntity.create(category))
            );
    }

    /**
     * 기존에 있는 값은 유지한 채 변경되는 값만 업데이트 합니다.
     */
    public UserJpaEntity toJpaEntity(User user, UserJpaEntity persistedUser) {
        return persistedUser.toBuilder()
            .nickname(user.getNickname().getValue())
            .refreshToken(user.getRefreshToken().getValue())
            .birthday(user.getBirthday())
            .category(user.getCategory())
            .archives(getArchiveJpaEntities(user))
            .build();
    }

    private List<ArchiveJpaEntity> getArchiveJpaEntities(User user) {
        return user.getArchives().stream()
            .map(archive -> archiveJpaMapper.toJpaEntity(archive))
            .collect(Collectors.toList());
    }

    // user <-> archive 연관 만들고
    // 비즈니스 로직 작업

    public User toDomain(UserJpaEntity userJpaEntity) {
        return User.builder()
            .id(Id.of(userJpaEntity.getId()))
            .email(Email.from(userJpaEntity.getEmail()))
            .nickname(Nickname.from(userJpaEntity.getNickname()))
            .refreshToken(RefreshToken.of(userJpaEntity.getRefreshToken()))
            .gender(userJpaEntity.getGender())
            .birthday(userJpaEntity.getBirthday())
            .role(userJpaEntity.getRole())
            .provider(userJpaEntity.getProvider())
            .category(userJpaEntity.getCategory())
            .goal(userJpaEntity.getGoal())
            .archives(mapArchives(userJpaEntity.getArchives()))
            .createdAt(userJpaEntity.getCreatedAt())
            .updatedAt(userJpaEntity.getUpdatedAt())
            .build();
    }

    private List<Archive> mapArchives(List<ArchiveJpaEntity> archives) {
        return archives.stream()
            .map(archiveJpaEntity -> Archive.builder()
	.id(archiveJpaEntity.getId())
	.category(archiveJpaEntity.getCategory())
	.newsLetterIds(archiveJpaEntity.getNewsLetterIds())
	.termIds(archiveJpaEntity.getTermIds())
	.build())
            .toList();
    }
}