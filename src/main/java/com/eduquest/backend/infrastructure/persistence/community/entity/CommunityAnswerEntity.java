
package com.eduquest.backend.infrastructure.persistence.community.entity;

import com.eduquest.backend.infrastructure.persistence.common.entity.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "community_answer")
public class CommunityAnswerEntity extends BasicEntity {

	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;

	@Column(name = "is_adopted", nullable = false)
	private Boolean isAdopted;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "community_post_id", nullable = false)
	private Long communityPostId;

	@Builder(access = AccessLevel.PROTECTED)
	public CommunityAnswerEntity(String content, Boolean isAdopted, Long userId, Long communityPostId) {
		this.content = content;
		this.isAdopted = isAdopted;
		this.userId = userId;
		this.communityPostId = communityPostId;
	}

	public static CommunityAnswerEntity of(String content, Long userId, Long communityPostId) {
		return CommunityAnswerEntity.builder()
				.content(content)
				.isAdopted(false)
				.userId(userId)
				.communityPostId(communityPostId)
				.build();
	}

	public void updateContent(String content) {
		this.content = content;
	}

	public void adopt() {
		this.isAdopted = true;
	}

	public void revokeAdopt() {
		this.isAdopted = false;
	}
}

