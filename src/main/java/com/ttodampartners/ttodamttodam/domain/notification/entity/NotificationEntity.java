package com.ttodampartners.ttodamttodam.domain.notification.entity;

import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.user.dto.model.NotificationBaseEntity;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "notification")
public class NotificationEntity extends NotificationBaseEntity {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Type type;

  @NotBlank
  private String message;

  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  private PostEntity post;

  @Getter
  public enum Type {
    KEYWORD("키워드"),
    POST("게시글"),
    REQUEST("요청"),
    GROUPCHAT("단체 채팅방");

    private final String label;
    Type(String label) {

      this.label = label;
    }
  }
}